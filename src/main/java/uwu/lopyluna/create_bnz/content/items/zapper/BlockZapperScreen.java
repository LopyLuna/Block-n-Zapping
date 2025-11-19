package uwu.lopyluna.create_bnz.content.items.zapper;

import com.simibubi.create.content.equipment.zapper.ConfigureZapperPacket;
import com.simibubi.create.content.equipment.zapper.PlacementPatterns;
import com.simibubi.create.content.equipment.zapper.ZapperScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.*;
import com.simibubi.create.foundation.gui.widget.Indicator.State;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import uwu.lopyluna.create_bnz.content.items.zapper.tools.Brush;
import uwu.lopyluna.create_bnz.content.items.zapper.tools.PlacementOptions;
import uwu.lopyluna.create_bnz.content.items.zapper.tools.TerrainBrushes;
import uwu.lopyluna.create_bnz.registry.BZGuiTexture;
import uwu.lopyluna.create_bnz.registry.BZPackets;

import java.util.List;
import java.util.Vector;

public class BlockZapperScreen extends ZapperScreen {

	protected BZGuiTexture background;

    protected final Component placementSection = CreateLang.translateDirect("gui.terrainzapper.placement");
	protected final Component toolSection = CreateLang.translateDirect("gui.terrainzapper.tool");
	protected final List<Component> brushOptions =
		CreateLang.translatedOptions("gui.terrainzapper.brush", "cuboid", "sphere", "cylinder", "surface", "cluster");

	protected Vector<IconButton> toolButtons;
	protected Vector<IconButton> placementButtons;

	protected ScrollInput brushInput;
	protected Label brushLabel;
	protected Vector<ScrollInput> brushParams = new Vector<>(3);
	protected Vector<Label> brushParamLabels = new Vector<>(3);
	protected Vector<ScrollInput> offsetParams = new Vector<>(3);
	protected Vector<Label> offsetParamLabels = new Vector<>(3);
	protected IconButton followDiagonals;
	protected IconButton acrossMaterials;
	protected Indicator followDiagonalsIndicator;
	protected Indicator acrossMaterialsIndicator;

	protected TerrainBrushes currentBrush;
	protected int[] currentBrushParams = new int[] { 1, 1, 1 };
	protected int[] currentOffsetParams = new int[] { 0, 0, 0 };
	protected boolean currentFollowDiagonals;
	protected boolean currentAcrossMaterials;
	protected TerrainTools currentTool;
	protected PlacementOptions currentPlacement;

	public BlockZapperScreen(ItemStack zapper, InteractionHand hand) {
		super(AllGuiTextures.TERRAINZAPPER, zapper, hand);
		this.background = BZGuiTexture.BLOCK_ZAPPER;
		fontColor = 0x767676;
		title = zapper.getHoverName();

		CompoundTag nbt = zapper.getOrCreateTag();
		currentBrush = NBTHelper.readEnum(nbt, "Brush", TerrainBrushes.class);
		if (nbt.contains("BrushParams", Tag.TAG_COMPOUND)) {
			BlockPos paramsData = ((BlockZapperItem)this.zapper.getItem()).fixSize(NbtUtils.readBlockPos(nbt.getCompound("BrushParams")), currentBrush.get(), this.zapper);
			currentBrushParams[0] = paramsData.getX();
			currentBrushParams[1] = paramsData.getY();
			currentBrushParams[2] = paramsData.getZ();
			if (currentBrushParams[1] == 0) currentFollowDiagonals = true;
			if (currentBrushParams[2] == 0) currentAcrossMaterials = true;
		}
		if (nbt.contains("BrushOffset", Tag.TAG_COMPOUND)) {
			BlockPos offsetData = NbtUtils.readBlockPos(nbt.getCompound("BrushOffset"));
			currentOffsetParams[0] = offsetData.getX();
			currentOffsetParams[1] = offsetData.getY();
			currentOffsetParams[2] = offsetData.getZ();
		}
		currentTool = NBTHelper.readEnum(nbt, "Tool", TerrainTools.class);
		currentPlacement = NBTHelper.readEnum(nbt, "Placement", PlacementOptions.class);
	}

	@Override
	protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		int x = guiLeft;
		int y = guiTop;

		background.render(graphics, x, y);
		drawOnBackground(graphics, x, y);

		renderBlock(graphics, x, y);
		renderZapper(graphics, x, y);
	}

	@Override
	protected void renderZapper(GuiGraphics graphics, int x, int y) {
		GuiGameElement.of(zapper)
				.scale(4)
				.at(x + background.width, y + background.height - 48, -200)
				.render(graphics);
	}

	@Override
	protected void init() {
		setWindowSize(background.width, background.height);
		setWindowOffset(-10, 0);

		guiLeft = (width - windowWidth) / 2;
		guiTop = (height - windowHeight) / 2;
		guiLeft += windowXOffset;
		guiTop += windowYOffset;

		animationProgress = 0;

		int x = guiLeft;
		int y = guiTop;

        IconButton confirmButton = new IconButton(x + background.width - 33, y + background.height - 24, AllIcons.I_CONFIRM);
		confirmButton.withCallback(this::onClose);
		addRenderableWidget(confirmButton);

		patternButtons.clear();
		for (int row = 0; row <= 1; row++) {
			for (int col = 0; col <= 2; col++) {
				int id = patternButtons.size();
				PlacementPatterns pattern = PlacementPatterns.values()[id];
				IconButton patternButton = new IconButton(x + background.width - 76 + col * 18, y + 21 + row * 18, pattern.icon);
				patternButton.withCallback(() -> {
					patternButtons.forEach(b -> b.green = false);
					patternButton.green = true;
					currentPattern = pattern;
				});
				patternButton.setToolTip(CreateLang.translateDirect("gui.terrainzapper.pattern." + pattern.translationKey));
				patternButtons.add(patternButton);
			}
		}

		patternButtons.get(currentPattern.ordinal()).green = true;

		addRenderableWidgets(patternButtons);

		brushLabel = new Label(x + 61, y + 25, CommonComponents.EMPTY).withShadow();
		brushInput = new SelectionScrollInput(x + 56, y + 20, 77, 18).forOptions(brushOptions)
			.titled(CreateLang.translateDirect("gui.terrainzapper.brush"))
			.writingTo(brushLabel)
			.calling(brushIndex -> {
				currentBrush = TerrainBrushes.values()[brushIndex];
				initBrushParams(x, y);
			});

		brushInput.setState(currentBrush.ordinal());

		addRenderableWidget(brushLabel);
		addRenderableWidget(brushInput);

		initBrushParams(x, y);
		initOffsetParams(x, y);
	}

	protected void initBrushParams(int x, int y) {
		Brush currentBrush = this.currentBrush.get();
		BlockZapperItem zapperItem = ((BlockZapperItem)zapper.getItem());

		// Brush Params

		removeWidgets(brushParamLabels);
		removeWidgets(brushParams);

		brushParamLabels.clear();
		brushParams.clear();

		for (int index = 0; index < 3; index++) {
			Label label = new Label(x + 65 + 20 * index, y + 45, CommonComponents.EMPTY).withShadow();

			final int finalIndex = index;
			ScrollInput input = new ScrollInput(x + 56 + 20 * index, y + 40, 18, 18)
				.withRange(currentBrush.getMin(index), zapperItem.indexMax(currentBrush.getMax(index), currentBrush, zapper) + 1)
				.writingTo(label)
				.titled(currentBrush.getParamLabel(index)
					.plainCopy())
				.calling(state -> {
					currentBrushParams[finalIndex] = state;
					label.setX(x + 65 + 20 * finalIndex - font.width(label.text) / 2);
				});
			input.setState(currentBrushParams[index]);
			input.onChanged();


			if (index >= currentBrush.amtParams) {
				input.visible = false;
				label.visible = false;
				input.active = false;
			}

			brushParamLabels.add(label);
			brushParams.add(input);
		}

		addRenderableWidgets(brushParamLabels);
		addRenderableWidgets(brushParams);

		// Connectivity Options

		if (followDiagonals != null) {
			removeWidget(followDiagonals);
			removeWidget(followDiagonalsIndicator);
			removeWidget(acrossMaterials);
			removeWidget(acrossMaterialsIndicator);
			followDiagonals = null;
			followDiagonalsIndicator = null;
			acrossMaterials = null;
			acrossMaterialsIndicator = null;
		}

		if (currentBrush.hasConnectivityOptions()) {
			int x1 = x + 7 + 4 * 18;
			int y1 = y + 79;
			followDiagonalsIndicator = new Indicator(x1, y1 - 6, CommonComponents.EMPTY);
			followDiagonals = new IconButton(x1, y1, AllIcons.I_FOLLOW_DIAGONAL);
			x1 += 18;
			acrossMaterialsIndicator = new Indicator(x1, y1 - 6, CommonComponents.EMPTY);
			acrossMaterials = new IconButton(x1, y1, AllIcons.I_FOLLOW_MATERIAL);

			followDiagonals.withCallback(() -> {
				followDiagonalsIndicator.state = followDiagonalsIndicator.state == State.OFF ? State.ON : State.OFF;
				currentFollowDiagonals = !currentFollowDiagonals;
			});
			followDiagonals.setToolTip(CreateLang.translateDirect("gui.terrainzapper.searchDiagonal"));
			acrossMaterials.withCallback(() -> {
				acrossMaterialsIndicator.state = acrossMaterialsIndicator.state == State.OFF ? State.ON : State.OFF;
				currentAcrossMaterials = !currentAcrossMaterials;
			});
			acrossMaterials.setToolTip(CreateLang.translateDirect("gui.terrainzapper.searchFuzzy"));
			addRenderableWidget(followDiagonals);
			addRenderableWidget(followDiagonalsIndicator);
			addRenderableWidget(acrossMaterials);
			addRenderableWidget(acrossMaterialsIndicator);
			if (currentFollowDiagonals)
				followDiagonalsIndicator.state = State.ON;
			if (currentAcrossMaterials)
				acrossMaterialsIndicator.state = State.ON;
		}

		// Tools

		if (toolButtons != null)
			removeWidgets(toolButtons);

		TerrainTools[] toolValues = currentBrush.getSupportedTools();
		toolButtons = new Vector<>(toolValues.length);
		for (int id = 0; id < toolValues.length; id++) {
			TerrainTools tool = toolValues[id];
			IconButton toolButton = new IconButton(x + 7 + id * 18, y + 99, tool.icon);
			toolButton.withCallback(() -> {
				toolButtons.forEach(b -> b.green = false);
				toolButton.green = true;
				currentTool = tool;
			});
			toolButton.setToolTip(CreateLang.translateDirect("gui.terrainzapper.tool." + tool.translationKey));
			toolButtons.add(toolButton);
		}

		int toolIndex = -1;
		for (int i = 0; i < toolValues.length; i++)
			if (currentTool == toolValues[i])
				toolIndex = i;
		if (toolIndex == -1) {
			currentTool = toolValues[0];
			toolIndex = 0;
		}
		toolButtons.get(toolIndex).green = true;

		addRenderableWidgets(toolButtons);

		// Placement Options

		if (placementButtons != null) removeWidgets(placementButtons);

		if (currentBrush.hasPlacementOptions()) {
			PlacementOptions[] placementValues = PlacementOptions.values();
			placementButtons = new Vector<>(placementValues.length);
			for (int id = 0; id < placementValues.length; id++) {
				PlacementOptions option = placementValues[id];
				IconButton placementButton = new IconButton(x + 136 + id * 18, y + 99, option.icon);
				placementButton.withCallback(() -> {
					placementButtons.forEach(b -> b.green = false);
					placementButton.green = true;
					currentPlacement = option;
				});
				placementButton.setToolTip(CreateLang.translateDirect("gui.terrainzapper.placement." + option.translationKey));
				placementButtons.add(placementButton);
			}

			placementButtons.get(currentPlacement.ordinal()).green = true;

			addRenderableWidgets(placementButtons);
		}
	}

	protected void initOffsetParams(int x, int y) {
		Brush currentBrush = this.currentBrush.get();
		BlockZapperItem zapperItem = ((BlockZapperItem)zapper.getItem());

		// Offset Params
		removeWidgets(offsetParamLabels);
		removeWidgets(offsetParams);

		offsetParamLabels.clear();
		offsetParams.clear();

		Component[] offsetLabels = new Component[] {
				Component.translatable("generic.offset_x"),
				Component.translatable("generic.offset_y"),
				Component.translatable("generic.offset_z")
		};


		for (int index = 0; index < 3; index++) {
			var max = zapperItem.indexMax(currentBrush.getMax(index), currentBrush, zapper);
			Label label = new Label(x + 65 + 20 * index, y + 65, CommonComponents.EMPTY).withShadow();

			final int finalIndex = index;
			ScrollInput input = new ScrollInput(x + 56 + 20 * index, y + 60, 18, 18)
					.withRange(max * -1, max + 1)
					.writingTo(label)
					.titled(offsetLabels[index].plainCopy())
					.calling(state -> {
						currentOffsetParams[finalIndex] = state;
						label.setX(x + 65 + 20 * finalIndex - font.width(label.text) / 2);
					});
			input.setState(currentOffsetParams[index]);
			input.onChanged();

			offsetParamLabels.add(label);
			offsetParams.add(input);
		}

		addRenderableWidgets(offsetParamLabels);
		addRenderableWidgets(offsetParams);
	}

	@Override
	protected void drawOnBackground(GuiGraphics graphics, int x, int y) {
		graphics.drawString(font, title, x + (background.width - font.width(title)) / 2, y + 4, 0x592424, false);

		Brush currentBrush = this.currentBrush.get();
		for (int index = 2; index >= currentBrush.amtParams; index--) BZGuiTexture.BLOCK_ZAPPER_INACTIVE_PARAM.render(graphics, x + 56 + 20 * index, y + 40);

		graphics.drawString(font, toolSection, x + 7, y + 89, fontColor, false);
		if (currentBrush.hasPlacementOptions()) graphics.drawString(font, placementSection, x + 136, y + 89, fontColor, false);
	}

	@Override
	protected ConfigureZapperPacket getConfigurationPacket() {
		currentBrushParams = ((BlockZapperItem)zapper.getItem()).fixSize(currentBrushParams, currentBrush.get(), zapper);
		int brushParamX = currentBrushParams[0];
		int brushParamY = followDiagonalsIndicator != null ? followDiagonalsIndicator.state == State.ON ? 0 : 1 : currentBrushParams[1];
		int brushParamZ = acrossMaterialsIndicator != null ? acrossMaterialsIndicator.state == State.ON ? 0 : 1 : currentBrushParams[2];
		int offsetX = currentOffsetParams[0];
		int offsetY = currentOffsetParams[1];
		int offsetZ = currentOffsetParams[2];
		return new ConfigureBlockZapperPacket(hand, currentPattern, currentBrush, brushParamX, brushParamY, brushParamZ, offsetX, offsetY, offsetZ, currentTool, currentPlacement);
	}

	@Override
	public void removed() {
		ConfigureZapperPacket packet = getConfigurationPacket();
		packet.configureZapper(zapper);
		BZPackets.getChannel().sendToServer(packet);
	}
}
