package uwu.lopyluna.create_bnz.content.items.zapper.tools;

import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.Lang;

public enum PlacementOptions {

	Merged(AllIcons.I_CENTERED),
	Attached(AllIcons.I_ATTACHED),
	Inserted(AllIcons.I_INSERTED);

	public String translationKey;
	public AllIcons icon;

	private PlacementOptions(AllIcons icon) {
		this.translationKey = Lang.asId(name());
		this.icon = icon;
	}

}
