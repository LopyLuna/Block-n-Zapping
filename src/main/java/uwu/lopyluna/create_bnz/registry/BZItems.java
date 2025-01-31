package uwu.lopyluna.create_bnz.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.resources.ResourceLocation;
import uwu.lopyluna.create_bnz.CreateBZ;
import uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperItem;
import uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperItemRenderer;
import uwu.lopyluna.create_bnz.content.modifiers.ModifierTier;
import uwu.lopyluna.create_bnz.content.modifiers.Modifiers;

import static uwu.lopyluna.create_bnz.CreateBZ.REGISTRATE;

public class BZItems {

    public static final ItemEntry<BlockZapperItem> BLOCK_ZAPPER = REGISTRATE.item("handheld_block_zapper", BlockZapperItem::new)
            .lang("Handheld Block Zapper")
			.transform(CreateRegistrate.customRenderedItem(() -> BlockZapperItemRenderer::new))
            .model((c, p) -> {
                String path = "item/handheld_block_zapper/augments/";
                for (Modifiers modifier : Modifiers.values()) for (ModifierTier tier : ModifierTier.values()) {
                    if (modifier == Modifiers.EMPTY || tier == ModifierTier.NONE) continue;
                    if (!((modifier.isUpgrade && tier == ModifierTier.SPECIAL) || (!modifier.isUpgrade && tier != ModifierTier.SPECIAL))) continue;

                    String augPath = path + tier.id + "/" + modifier.id;
                    ResourceLocation parentPath = CreateBZ.asResource(path + modifier.id);

                    p.withExistingParent(augPath, parentPath)
                            .texture("0", "create_bnz:item/block_zapper_mesh_" + tier.id)
                            .texture("1", "minecraft:block/smooth_stone_slab_side")
                            .texture("particle", "minecraft:block/obsidian");

                }
                p.withExistingParent("item/" + c.getName(), p.modLoc("item/" + c.getName() + "/item"));
            })
            .register();


    public static void register() {}
}
