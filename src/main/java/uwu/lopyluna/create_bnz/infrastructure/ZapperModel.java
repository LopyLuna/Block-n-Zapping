package uwu.lopyluna.create_bnz.infrastructure;

import com.jozufozu.flywheel.core.PartialModel;
import net.minecraft.client.resources.model.BakedModel;
import uwu.lopyluna.create_bnz.content.modifiers.ModifierTier;
import uwu.lopyluna.create_bnz.content.modifiers.Modifiers;

public class ZapperModel {
    public PartialModel model;
    public Modifiers modifier;
    public ModifierTier tier;

    public ZapperModel(PartialModel model, Modifiers modifier, ModifierTier tier) {
        this.model = model;
        this.modifier = modifier;
        this.tier = tier;
    }

    public BakedModel get() {
        return model.get();
    }
    public boolean get(Modifiers modifier, ModifierTier tier) {
        return this.modifier == modifier && this.tier == tier;
    }

}
