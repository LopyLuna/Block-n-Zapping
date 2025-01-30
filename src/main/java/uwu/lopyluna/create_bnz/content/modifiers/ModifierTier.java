package uwu.lopyluna.create_bnz.content.modifiers;

import java.awt.*;

import static uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperItem.TIERS;

public enum ModifierTier {
    NONE("none", "none",0, 0, new Color(255, 255, 255)),
    BRASS("Brass", "brass",1, 0, new Color(215, 161, 88)),
    ROSE_QUARTZ("RoseQuartz", "rose",2, 1, new Color(247, 69, 114)),
    OBSIDIAN("Obsidian", "obsidian",3, 2, new Color(82, 52, 127)),
    ECHO("Echo", "echo",4, 3, new Color(0, 146, 149)),
    SPECIAL("Special", "special",0, 0, new Color(138, 226, 90));

    public final int level;
    public final int require_level;
    public final String baseName;
    public final String id;
    public final Color color;

    ModifierTier(String name, String id, int level, int require_level, Color color) {
        this.level =level;
        this.baseName = name;
        this.require_level = require_level;
        this.color = color;
        this.id = id;
        TIERS.add(this);
    }

    public boolean hasTier() {
        return this != NONE;
    }
}
