package uwu.lopyluna.create_bnz.content.modifiers;

import java.awt.*;

public enum ModifierTier {
    NONE("none",0, 0, new Color(255, 255, 255)),
    BRASS("Brass",1, 0, new Color(215, 161, 88)),
    ROSE_QUARTZ("RoseQuartz",2, 1, new Color(247, 69, 114)),
    OBSIDIAN("Obsidian",3, 2, new Color(82, 52, 127)),
    ECHO("Echo",4, 3, new Color(0, 146, 149)),
    SPECIAL("Special",0, 0, new Color(138, 226, 90));

    public final int level;
    public final int require_level;
    public final String baseName;
    public final Color color;

    ModifierTier(String name, int level, int require_level, Color color) {
        this.level =level;
        this.baseName = name;
        this.require_level = require_level;
        this.color = color;
    }
}
