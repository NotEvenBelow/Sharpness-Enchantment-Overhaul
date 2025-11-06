package Sharpness_Enchantment_Overhaul.sharpness_enchantment_overhaul;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import Sharpness_Enchantment_Overhaul.sharpness_enchantment_overhaul.config.Sharpness_enchantment_overhaulConfig;

public final class Sharpness_enchantment_overhaul implements ModInitializer {
    public static final String MODID = "sharpness_enchantment_overhaul";
    public static final Logger LOG = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        Sharpness_enchantment_overhaulConfig.load();
        LOG.info("[{}] initialized. excludeSmiteAndBaneTargets={}",
                MODID, Sharpness_enchantment_overhaulConfig.get().excludeSmiteAndBaneTargets);
    }
}
