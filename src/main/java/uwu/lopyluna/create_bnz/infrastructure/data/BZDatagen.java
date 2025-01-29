package uwu.lopyluna.create_bnz.infrastructure.data;

import com.tterrag.registrate.providers.ProviderType;
import net.minecraftforge.data.event.GatherDataEvent;
import uwu.lopyluna.create_bnz.CreateBZ;
import uwu.lopyluna.create_bnz.registry.BZLangPartial;

import java.util.function.BiConsumer;

public class BZDatagen {
	public static void gatherData(GatherDataEvent event) {
		addExtraRegistrateData();
	}

	private static void addExtraRegistrateData() {
		BZRegistrateTags.addGenerators();

		CreateBZ.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
			BiConsumer<String, String> langConsumer = provider::add;

			providePartialLang(langConsumer);
			providePonderLang();
		});
	}

	private static void providePartialLang(BiConsumer<String, String> consumer) {
		BZLangPartial.provideLang(consumer);
	}

	private static void providePonderLang() {
	}
}
