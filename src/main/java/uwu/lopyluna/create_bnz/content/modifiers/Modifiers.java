package uwu.lopyluna.create_bnz.content.modifiers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import static uwu.lopyluna.create_bnz.CreateBZ.MOD_ID;
import static uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperItem.MODIFIERS;
import static uwu.lopyluna.create_bnz.registry.BZLangPartial.LANG;

public enum Modifiers {
    EMPTY,
    CANISTER("Canister", "Adds Block Storage Slots", 4),
    BODY("Body", "Reinforce to use Stronger Blocks", 4),
    AMPLIFIER("Amplifier", "Increases Radius", 4),
    ACCELERATOR("Accelerator", "Reduces Cooldown", 4),
    RETRIEVER("Retriever", "Ability to Obtain Blocks", 4),
    SCOPE("Scope", "Increases Zapper Range", 4),
    REINFORCER("Reinforcer", "Reinforce Durability", 4),
    STASIS("Stasis", "Disallow Block Updates"),
    APPLICATOR("Applicator", "Adds Modifier Slots", 2),
    GENERATOR("Generator", "Renewable Blocks are Infinite")
    ;

    public final String id;
    public final ResourceLocation loc;

    public final String langDescription;
    public final String langName;
    public final String baseDescription;
    public final String baseName;

    public final int maxLevel;

    Modifiers() {
        this("", "");
    }
    Modifiers(String baseName, String baseDescription) {
        this(baseName, baseDescription, 1);
    }
    Modifiers(String baseName, String baseDescription, int maxLevel) {
        id = baseName.replaceAll(" ", "_").toLowerCase();
        langName = "modifier." + MOD_ID + "." + id;
        langDescription = langName + ".desc";
        this.baseName = baseName;
        this.baseDescription = baseDescription;
        this.maxLevel = maxLevel;
        loc = new ResourceLocation(MOD_ID, id);

        LANG.put(langName, this.baseName);
        LANG.put(langDescription, this.baseDescription);

        MODIFIERS.add(this);
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
