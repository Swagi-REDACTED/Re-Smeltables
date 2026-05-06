package net.blupillcosby.resmeltables;

import net.blupillcosby.resmeltables.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReSmeltables implements ModInitializer {
    public static final String MOD_ID = "resmeltables";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModConfig CONFIG;

    @Override
    public void onInitialize() {
        try {
            CONFIG = me.fzzyhmstrs.fzzy_config.api.ConfigApiJava.registerAndLoadConfig(ModConfig::new);
            LOGGER.info("Re-Smeltables initialized successfully! Config allowResmelting: {}", CONFIG.allowResmelting.get());
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Re-Smeltables config!", e);
        }
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
