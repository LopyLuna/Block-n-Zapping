package uwu.lopyluna.create_bnz.content.items.zapper;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.equipment.zapper.ZapperItemRenderer;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import uwu.lopyluna.create_bnz.CreateBZ;
import uwu.lopyluna.create_bnz.content.modifiers.ModifierTier;
import uwu.lopyluna.create_bnz.content.modifiers.Modifiers;
import uwu.lopyluna.create_bnz.infrastructure.ZapperModel;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static uwu.lopyluna.create_bnz.content.modifiers.Modifiers.*;

public class BlockZapperItemRenderer extends ZapperItemRenderer {

	protected static final PartialModel CORE = new PartialModel(CreateBZ.asResource("item/handheld_block_zapper/core"));
	protected static final PartialModel CORE_GLOW = new PartialModel(CreateBZ.asResource("item/handheld_block_zapper/core_glow"));
	protected static final PartialModel CORE_BULK = new PartialModel(CreateBZ.asResource("item/handheld_block_zapper/augments/glow/core_bulk"));
	protected static final PartialModel CORE_GLOW_BULK = new PartialModel(CreateBZ.asResource("item/handheld_block_zapper/augments/glow/core_glow_bulk"));
	public static List<ZapperModel> MODELS = new ArrayList<>();


	@Override
	protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType,
		PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		super.render(stack, model, renderer, transformType, ms, buffer, light, overlay);
		var mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player != null) {
			CompoundTag nbt = stack.getOrCreateTag();
			float pt = AnimationTickHolder.getPartialTicks();
			float worldTime = AnimationTickHolder.getRenderTime() / 20;
			float cooldown = player.getCooldowns().getCooldownPercent(stack.getItem(), mc.getFrameTime());

			renderer.renderSolid(model.getOriginalModel(), light);
			renderSolidModifierModel(renderer, BODY, nbt, light, false);
			renderSolidModifierModel(renderer, AMPLIFIER, nbt, light, true);
			renderSolidModifierModel(renderer, RETRIEVER, nbt, light, true);
			renderSolidModifierModel(renderer, SCOPE, nbt, light, true);
			renderSolidModifierModel(renderer, REINFORCER, nbt, light, true);

			boolean leftHanded = player.getMainArm() == HumanoidArm.LEFT;
			boolean mainHand = player.getMainHandItem() == stack;
			boolean offHand = player.getOffhandItem() == stack;
			float animation = getAnimationProgress(pt, leftHanded, mainHand);

			// Core glows
			int lightIntensity = (int) (15 * Mth.clamp(Mth.sin(worldTime * 5), 0, 1));
			if (mainHand || offHand) lightIntensity = 15 - ((int) (15 * (Mth.clamp(cooldown, 0, 1))));

			int glowLight = LightTexture.pack(lightIntensity, max(lightIntensity, 4));
			glowLight = (glowLight-1 > glowLight*0.5) ? glowLight-1 : glowLight;
			renderer.renderSolidGlowing(CORE.get(), glowLight);
			renderer.renderGlowing(CORE_GLOW.get(), glowLight);
			if (AMPLIFIER.getLevel(nbt) > 0) {
				renderer.renderSolidGlowing(CORE_BULK.get(), glowLight);
				renderer.renderGlowing(CORE_GLOW_BULK.get(), glowLight);
			}

			// Accelerator spins
			float angle = worldTime * -25;
			if (mainHand || offHand) angle += 360 * animation;

			angle %= 360;
			float offset = -.155f;
			ms.translate(0, offset, 0);
			ms.mulPose(Axis.ZP.rotationDegrees(angle));
			ms.translate(0, -offset, 0);
			renderModifierModel(renderer, ACCELERATOR, nbt, light, false);
        }
	}

	public void renderModifierModel(PartialItemModelRenderer renderer, Modifiers modifiers, CompoundTag nbt, int light, boolean ignoreDefault) {
		ModifierTier tier = modifiers.getTierFromModifier(nbt);
		if (!tier.hasTier() && ignoreDefault) return;
		var z = MODELS.stream().filter(m -> m.get(modifiers, tier)).findFirst();
		z.ifPresent(zModel -> renderer.render(zModel.get(), light));
	}
	public void renderSolidModifierModel(PartialItemModelRenderer renderer, Modifiers modifiers, CompoundTag nbt, int light, boolean ignoreDefault) {
		ModifierTier tier = modifiers.getTierFromModifier(nbt);
		if (!tier.hasTier() && ignoreDefault) return;
		var z = MODELS.stream().filter(m -> m.get(modifiers, tier)).findFirst();
		z.ifPresent(zModel -> renderer.renderSolid(zModel.get(), light));
	}

	@Override
	protected float getAnimationProgress(float pt, boolean leftHanded, boolean mainHand) {
		float animation = CreateClient.ZAPPER_RENDER_HANDLER.getAnimation(mainHand ^ leftHanded, pt);
		return Mth.clamp(animation * 2.5f, 0, 1);
	}

	public static void renderModels() {
		String path = "item/handheld_block_zapper/augments/";
		for (Modifiers modifier : Modifiers.values()) for (ModifierTier tier : ModifierTier.values()) {
			if (modifier == Modifiers.EMPTY) continue;
			if (!((modifier.isUpgrade && tier == ModifierTier.SPECIAL) || (!modifier.isUpgrade && tier != ModifierTier.SPECIAL))) continue;
			String tierPath = tier == ModifierTier.NONE ? "" : tier.id + "/";
			String augPath = path + tierPath + modifier.id;
			MODELS.add(new ZapperModel(new PartialModel(CreateBZ.asResource(augPath)), modifier, tier));
		}
	}
}
