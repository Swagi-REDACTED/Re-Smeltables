package net.blupillcosby.resmeltables.config;

import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import net.blupillcosby.resmeltables.ReSmeltables;

@Version(version = 1)
public class ModConfig extends Config {

    public ValidatedBoolean allowResmelting = new ValidatedBoolean(true);
    public ValidatedFloat ironXp = new ValidatedFloat(0.1f, 10.0f, 0.0f);
    public ValidatedFloat copperXp = new ValidatedFloat(0.1f, 10.0f, 0.0f);
    public ValidatedFloat goldXp = new ValidatedFloat(0.2f, 10.0f, 0.0f);

    public ModConfig() {
        super(ReSmeltables.id("main"));
    }

    @Override
    public int defaultPermLevel() {
        return 2;
    }
}
