package uwu.lopyluna.create_bnz.content.items.zapper.tools;

import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.CreateLang;

public enum PlacementOptions {
	Merged(AllIcons.I_CENTERED),
	Attached(AllIcons.I_ATTACHED),
	Inserted(AllIcons.I_INSERTED);

	public final String translationKey;
	public final AllIcons icon;

	PlacementOptions(AllIcons icon) {
		this.translationKey = CreateLang.asId(name());
		this.icon = icon;
	}

}
