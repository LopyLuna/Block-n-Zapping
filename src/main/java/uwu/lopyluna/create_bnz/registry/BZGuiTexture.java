package uwu.lopyluna.create_bnz.registry;

import com.simibubi.create.foundation.gui.element.ScreenElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static uwu.lopyluna.create_bnz.CreateBZ.MOD_ID;

public enum BZGuiTexture implements ScreenElement {
    BLOCK_ZAPPER("zapper", 234, 103),
    BLOCK_ZAPPER_INACTIVE_PARAM("zapper", 238, 0, 18, 18);

    public final ResourceLocation location;
    public final int width, height;
    public final int startX, startY;

    BZGuiTexture(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    BZGuiTexture(String location, int startX, int startY, int width, int height) {
        this(MOD_ID, location, startX, startY, width, height);
    }

    BZGuiTexture(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics graphics, int x, int y) {
        graphics.blit(location, x, y, startX, startY, width, height);
    }
}
