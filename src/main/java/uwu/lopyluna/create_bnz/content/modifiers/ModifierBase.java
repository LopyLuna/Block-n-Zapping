package uwu.lopyluna.create_bnz.content.modifiers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import static uwu.lopyluna.create_bnz.CreateBZ.MOD_ID;
import static uwu.lopyluna.create_bnz.registry.BZLangPartial.LANG;

public class ModifierBase {
    public String id;
    public ResourceLocation loc;

    public String langDescription;
    public String langName;
    public String baseDescription;
    public String baseName;

    public int maxLevel;

    public ModifierBase(String baseName, String baseDescription) {
        this(baseName, baseDescription, 1);
    }
    public ModifierBase(String baseName, String baseDescription, int maxLevel) {
        id = baseName.replaceAll(" ", "_").toLowerCase();
        langName = "modifier." + MOD_ID + "." + id;
        langDescription = langName + ".desc";
        this.baseName = baseName;
        this.baseDescription = baseDescription;
        this.maxLevel = maxLevel;
        loc = new ResourceLocation(MOD_ID, id);

        LANG.put(langName, this.baseName);
        LANG.put(langDescription, this.baseDescription);
    }

    public String getId() {
        return id;
    }

    public MutableComponent getDescription() {
        return Component.translatableWithFallback(langDescription, baseDescription);
    }
    public MutableComponent getName() {
        return Component.translatableWithFallback(langName, baseName);
    }

    public void update(CompoundTag nbt) {
        if (!nbt.contains(baseName)) nbt.putInt(baseName, 0);
    }
    public int getLevel(CompoundTag nbt) {
        return nbt.contains(baseName) ? nbt.getInt(baseName) : 0;
    }
}
