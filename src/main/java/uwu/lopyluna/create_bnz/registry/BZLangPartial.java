package uwu.lopyluna.create_bnz.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@SuppressWarnings({"unused", "SameParameterValue"})
public class BZLangPartial {
    public static Map<String, String> LANG = new HashMap<>();

    public static void provideLang(BiConsumer<String, String> consumer) {
        if (!LANG.isEmpty()) LANG.forEach((key, enUS) -> consume(consumer, key, enUS));
        String zapper = "handheld_block_zapper";

        consume(consumer, "item", zapper + ".tooltip.behaviour1", "_Targeted block_ will become the _material_ placed by the shaper.");
        consume(consumer, "item", zapper + ".tooltip.behaviour2", "Applies currently selected _Brush_ and _Tool_ at the targeted location.");
        consume(consumer, "item", zapper + ".tooltip.behaviour3", "Opens the _Configuration Interface_");
        consume(consumer, "item", zapper + ".tooltip.condition1", "L-Click at Block");
        consume(consumer, "item", zapper + ".tooltip.condition2", "R-Click at Block");
        consume(consumer, "item", zapper + ".tooltip.condition3", "R-Click while Sneaking");
        consume(consumer, "item", zapper + ".tooltip.summary", "Survival mode_ tool for medium-scale _landscaping_ from a decent distance.");

        consume(consumer, zapper + ".modifiers", "Modifiers:");
        consume(consumer, zapper + ".no_modifiers", "You have no modifiers applied.");
        consume(consumer, zapper + ".ctrl", " for Modifiers");
        consume(consumer, "tooltip.holdForModifiers", "Hold ");
        consume(consumer, zapper + ".not_enough_blocks", "Not holding enough blocks!");
        consume(consumer, zapper + ".too_hard", "Something is too strong in selection!");
        consume(consumer, zapper + ".failed", "Something is invalid in selection!");

        consume(consumer, "itemGroup.create_bnz.base", "Create: Block n' Zapping", true);
    }

    private static void consume(BiConsumer<String, String> consumer, String key, String enUS) {
        consume(consumer, key, enUS, false);
    }

    private static void consume(BiConsumer<String, String> consumer, String type, String key, String enUS) {
        boolean flag = type.isEmpty();
        consumer.accept((flag ? "create_bnz." : type + ".create_bnz.") + key, enUS);
    }

    private static void consume(BiConsumer<String, String> consumer, String key, String enUS, boolean original) {
        if (original) consumer.accept(key, enUS);
        else consumer.accept("create_bnz." + key, enUS);
    }
}
