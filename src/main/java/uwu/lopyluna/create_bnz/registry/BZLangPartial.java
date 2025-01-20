package uwu.lopyluna.create_bnz.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@SuppressWarnings({"unused", "SameParameterValue"})
public class BZLangPartial {
    public static Map<String, String> LANG = new HashMap<>();

    public static void provideLang(BiConsumer<String, String> consumer) {
        if (!LANG.isEmpty()) LANG.forEach((key, enUS) -> consume(consumer, key, enUS));

        consume(consumer, "itemGroup.create_bnz.base", "Create: Block n' Zapping");

    }

    private static void consume(BiConsumer<String, String> consumer, String key, String enUS) {
        consumer.accept(key, enUS);
    }
}
