package uwu.lopyluna.create_bnz.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static uwu.lopyluna.create_bnz.CreateBZ.MOD_ID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = MOD_ID)
public class BZKeys {
    
    public static final String CATEGORY = "key.categories." + MOD_ID;
    
    public static final KeyMapping OPEN_BLOCK_ZAPPER_GUI = new KeyMapping(
            "key." + MOD_ID + ".open_block_zapper_gui",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V, // Default key: V
            CATEGORY
    );
    
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(OPEN_BLOCK_ZAPPER_GUI);
    }
}

