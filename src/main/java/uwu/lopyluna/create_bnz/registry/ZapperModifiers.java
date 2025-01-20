package uwu.lopyluna.create_bnz.registry;

import uwu.lopyluna.create_bnz.content.modifiers.ModifierBase;

public class ZapperModifiers {

    public static ModifierBase CANISTER = new ModifierBase("Canister", "Adds Block Storage Slots", 4);
    public static ModifierBase BODY = new ModifierBase("Body", "Reinforce to use Stronger Blocks", 4);
    public static ModifierBase AMPLIFIER = new ModifierBase("Amplifier", "Increases Radius", 4);
    public static ModifierBase ACCELERATOR = new ModifierBase("Accelerator", "Reduces Cooldown", 4);
    public static ModifierBase RETRIEVER = new ModifierBase("Retriever", "Ability to Obtain Blocks", 4);
    public static ModifierBase SCOPE = new ModifierBase("Scope", "Increases Zapper Range", 4);
    public static ModifierBase REINFORCER = new ModifierBase("Reinforcer", "Reinforce Durability", 4);

    public static ModifierBase STASIS = new ModifierBase("Stasis", "Disallow Block Updates");
    public static ModifierBase APPLICATOR = new ModifierBase("Applicator", "Adds Modifier Slots", 2);
    public static ModifierBase GENERATOR = new ModifierBase("Generator", "Renewable Blocks are Infinite");


    public static void register() {}
}
