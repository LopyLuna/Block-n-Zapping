package uwu.lopyluna.create_bnz.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class BZConfig {

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BZCommonConfig.SPEC);
    }

    public static class BZCommonConfig {
        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec SPEC;

        // Amplifier (Placement Size) Config
        public static final ForgeConfigSpec.IntValue AMPLIFIER_LEVEL_1_MAX_SIZE;
        public static final ForgeConfigSpec.IntValue AMPLIFIER_LEVEL_2_MAX_SIZE;
        public static final ForgeConfigSpec.IntValue AMPLIFIER_LEVEL_3_MAX_SIZE;
        public static final ForgeConfigSpec.IntValue AMPLIFIER_LEVEL_4_MAX_SIZE;
        public static final ForgeConfigSpec.IntValue AMPLIFIER_DEFAULT_MAX_SIZE;

        public static final ForgeConfigSpec.IntValue AMPLIFIER_LEVEL_1_MAX_RADIUS;
        public static final ForgeConfigSpec.IntValue AMPLIFIER_LEVEL_2_MAX_RADIUS;
        public static final ForgeConfigSpec.IntValue AMPLIFIER_LEVEL_3_MAX_RADIUS;
        public static final ForgeConfigSpec.IntValue AMPLIFIER_LEVEL_4_MAX_RADIUS;
        public static final ForgeConfigSpec.IntValue AMPLIFIER_DEFAULT_MAX_RADIUS;

        // Scope (Range) Config
        public static final ForgeConfigSpec.IntValue SCOPE_RANGE_MULTIPLIER;
        public static final ForgeConfigSpec.IntValue SCOPE_BASE_RANGE;

        // Reinforcer (Durability) Config
        public static final ForgeConfigSpec.IntValue REINFORCER_LEVEL_1_MULTIPLIER;
        public static final ForgeConfigSpec.IntValue REINFORCER_LEVEL_2_MULTIPLIER;
        public static final ForgeConfigSpec.IntValue REINFORCER_LEVEL_3_MULTIPLIER;
        public static final ForgeConfigSpec.IntValue REINFORCER_LEVEL_4_MULTIPLIER;
        public static final ForgeConfigSpec.IntValue REINFORCER_BASE_DURABILITY;
        public static final ForgeConfigSpec.BooleanValue REINFORCER_LEVEL_4_UNBREAKABLE;

        // Accelerator (Cooldown) Config
        public static final ForgeConfigSpec.IntValue ACCELERATOR_LEVEL_1_COOLDOWN;
        public static final ForgeConfigSpec.IntValue ACCELERATOR_LEVEL_2_COOLDOWN;
        public static final ForgeConfigSpec.IntValue ACCELERATOR_LEVEL_3_COOLDOWN;
        public static final ForgeConfigSpec.IntValue ACCELERATOR_LEVEL_4_COOLDOWN;
        public static final ForgeConfigSpec.IntValue ACCELERATOR_DEFAULT_COOLDOWN;

        // Body (Hardness Support) Config
        public static final ForgeConfigSpec.IntValue BODY_LEVEL_1_HARDNESS;
        public static final ForgeConfigSpec.IntValue BODY_LEVEL_2_HARDNESS;
        public static final ForgeConfigSpec.IntValue BODY_LEVEL_3_HARDNESS;
        public static final ForgeConfigSpec.IntValue BODY_LEVEL_4_HARDNESS;
        public static final ForgeConfigSpec.IntValue BODY_DEFAULT_HARDNESS;

        // Applicator (Modifier Slots) Config
        public static final ForgeConfigSpec.IntValue APPLICATOR_BASE_SLOTS;
        public static final ForgeConfigSpec.IntValue APPLICATOR_UPGRADED_SLOTS;

        static {
            BUILDER.push("Block Zapper Configuration");

            // Amplifier Configuration
            BUILDER.push("Amplifier - Placement Size");
            AMPLIFIER_DEFAULT_MAX_SIZE = BUILDER
                    .comment("Default max placement size (no amplifier)")
                    .defineInRange("defaultMaxSize", 3, 1, 32);
            AMPLIFIER_LEVEL_1_MAX_SIZE = BUILDER
                    .comment("Max placement size at Amplifier Level 1")
                    .defineInRange("level1MaxSize", 4, 1, 32);
            AMPLIFIER_LEVEL_2_MAX_SIZE = BUILDER
                    .comment("Max placement size at Amplifier Level 2")
                    .defineInRange("level2MaxSize", 5, 1, 32);
            AMPLIFIER_LEVEL_3_MAX_SIZE = BUILDER
                    .comment("Max placement size at Amplifier Level 3")
                    .defineInRange("level3MaxSize", 6, 1, 32);
            AMPLIFIER_LEVEL_4_MAX_SIZE = BUILDER
                    .comment("Max placement size at Amplifier Level 4")
                    .defineInRange("level4MaxSize", 8, 1, 32);

            AMPLIFIER_DEFAULT_MAX_RADIUS = BUILDER
                    .comment("Default max radius (no amplifier)")
                    .defineInRange("defaultMaxRadius", 2, 1, 32);
            AMPLIFIER_LEVEL_1_MAX_RADIUS = BUILDER
                    .comment("Max radius at Amplifier Level 1")
                    .defineInRange("level1MaxRadius", 3, 1, 32);
            AMPLIFIER_LEVEL_2_MAX_RADIUS = BUILDER
                    .comment("Max radius at Amplifier Level 2")
                    .defineInRange("level2MaxRadius", 4, 1, 32);
            AMPLIFIER_LEVEL_3_MAX_RADIUS = BUILDER
                    .comment("Max radius at Amplifier Level 3")
                    .defineInRange("level3MaxRadius", 5, 1, 32);
            AMPLIFIER_LEVEL_4_MAX_RADIUS = BUILDER
                    .comment("Max radius at Amplifier Level 4")
                    .defineInRange("level4MaxRadius", 6, 1, 32);
            BUILDER.pop();

            // Scope Configuration
            BUILDER.push("Scope - Zapping Range");
            SCOPE_BASE_RANGE = BUILDER
                    .comment("Base zapping range (blocks)")
                    .defineInRange("baseRange", 6, 1, 128);
            SCOPE_RANGE_MULTIPLIER = BUILDER
                    .comment("Range multiplier per Scope level (baseRange * (level + 1))")
                    .defineInRange("rangeMultiplier", 6, 1, 128);
            BUILDER.pop();

            // Reinforcer Configuration
            BUILDER.push("Reinforcer - Durability");
            REINFORCER_BASE_DURABILITY = BUILDER
                    .comment("Base durability (no reinforcer)")
                    .defineInRange("baseDurability", 2306, 1, 100000);
            REINFORCER_LEVEL_1_MULTIPLIER = BUILDER
                    .comment("Durability multiplier at Reinforcer Level 1 (percentage, 150 = 1.5x)")
                    .defineInRange("level1Multiplier", 150, 100, 1000);
            REINFORCER_LEVEL_2_MULTIPLIER = BUILDER
                    .comment("Durability multiplier at Reinforcer Level 2 (percentage, 200 = 2x)")
                    .defineInRange("level2Multiplier", 200, 100, 1000);
            REINFORCER_LEVEL_3_MULTIPLIER = BUILDER
                    .comment("Durability multiplier at Reinforcer Level 3 (percentage, 250 = 2.5x)")
                    .defineInRange("level3Multiplier", 250, 100, 1000);
            REINFORCER_LEVEL_4_MULTIPLIER = BUILDER
                    .comment("Durability multiplier at Reinforcer Level 4 (percentage, 300 = 3x)")
                    .defineInRange("level4Multiplier", 300, 100, 1000);
            REINFORCER_LEVEL_4_UNBREAKABLE = BUILDER
                    .comment("Make Reinforcer Level 4 unbreakable")
                    .define("level4Unbreakable", true);
            BUILDER.pop();

            // Accelerator Configuration
            BUILDER.push("Accelerator - Cooldown");
            ACCELERATOR_DEFAULT_COOLDOWN = BUILDER
                    .comment("Default cooldown in ticks (no accelerator)")
                    .defineInRange("defaultCooldown", 150, 1, 1200);
            ACCELERATOR_LEVEL_1_COOLDOWN = BUILDER
                    .comment("Cooldown at Accelerator Level 1 (ticks)")
                    .defineInRange("level1Cooldown", 120, 1, 1200);
            ACCELERATOR_LEVEL_2_COOLDOWN = BUILDER
                    .comment("Cooldown at Accelerator Level 2 (ticks)")
                    .defineInRange("level2Cooldown", 90, 1, 1200);
            ACCELERATOR_LEVEL_3_COOLDOWN = BUILDER
                    .comment("Cooldown at Accelerator Level 3 (ticks)")
                    .defineInRange("level3Cooldown", 60, 1, 1200);
            ACCELERATOR_LEVEL_4_COOLDOWN = BUILDER
                    .comment("Cooldown at Accelerator Level 4 (ticks)")
                    .defineInRange("level4Cooldown", 30, 1, 1200);
            BUILDER.pop();

            // Body Configuration
            BUILDER.push("Body - Hardness Support");
            BODY_DEFAULT_HARDNESS = BUILDER
                    .comment("Default hardness support (no body upgrade)")
                    .defineInRange("defaultHardness", 2, 0, 100);
            BODY_LEVEL_1_HARDNESS = BUILDER
                    .comment("Hardness support at Body Level 1")
                    .defineInRange("level1Hardness", 4, 0, 100);
            BODY_LEVEL_2_HARDNESS = BUILDER
                    .comment("Hardness support at Body Level 2")
                    .defineInRange("level2Hardness", 8, 0, 100);
            BODY_LEVEL_3_HARDNESS = BUILDER
                    .comment("Hardness support at Body Level 3")
                    .defineInRange("level3Hardness", 25, 0, 100);
            BODY_LEVEL_4_HARDNESS = BUILDER
                    .comment("Hardness support at Body Level 4")
                    .defineInRange("level4Hardness", 50, 0, 100);
            BUILDER.pop();

            // Applicator Configuration
            BUILDER.push("Applicator - Modifier Slots");
            APPLICATOR_BASE_SLOTS = BUILDER
                    .comment("Base modifier slots (no applicator)")
                    .defineInRange("baseSlots", 3, 1, 10);
            APPLICATOR_UPGRADED_SLOTS = BUILDER
                    .comment("Modifier slots with applicator upgrade")
                    .defineInRange("upgradedSlots", 5, 1, 10);
            BUILDER.pop();

            BUILDER.pop();
            SPEC = BUILDER.build();
        }
    }
}

