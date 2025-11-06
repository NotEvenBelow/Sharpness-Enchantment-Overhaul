package Sharpness_Enchantment_Overhaul.sharpness_enchantment_overhaul.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Sharpness_enchantment_overhaulConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File("config/sharpness_enchantment_overhaul.json");
    private static final Logger LOG = LoggerFactory.getLogger("sharpness_enchantment_overhaul");

    /* -------- General Settings -------- */


    public float sharpnessMultiplier = 1.20f;


    public boolean equalWeightForDamageEnchantments = true;


    /* -------- Sharpness Settings -------- */


    public boolean excludeSmiteAndBaneTargets = true;


    public List<String> sharpnessExcludedEntityIds = new ArrayList<>();


    public boolean sharpnessApplyOffTargetEffect = true;


    public float sharpnessUndeadChance = 0.02f;

    public int sharpnessUndeadWeaknessLevel = 1;

    public int sharpnessUndeadWeaknessDuration = 4;


    public float sharpnessArthropodChance = 0.04f;

    public int sharpnessArthropodSlownessLevel = 1;

    public int sharpnessArthropodSlownessDuration = 6;


    public float sharpnessExcludedListChance = 0.02f;

    public int sharpnessExcludedListSlownessLevel = 1;

    public int sharpnessExcludedListSlownessDuration = 7;


    /* -------- Smite Settings -------- */


    public boolean smiteApplyBuffOnNonUndead = true;


    public float smiteNonUndeadHitChance = 0.03f;

    public int smitePlayerRegenLevel = 1;

    public int smitePlayerRegenDuration = 4;


    /* -------- Bane of Arthropods Settings (Updated) -------- */


    public boolean baneApplyDebuffOnNonArthropod = true;


    public float baneAllSlownessChance = 0.04f;

    public int baneAllSlownessLevel = 2;

    public int baneAllSlownessDuration = 5;


    public float baneAllWeaknessChance = 0.02f;

    public int baneAllWeaknessLevel = 1;

    public int baneAllWeaknessDuration = 5;


    /* -------- Runtime Cache (Not Serialized) -------- */

    private transient Set<String> excludedIdSet = new HashSet<>();

    private static Sharpness_enchantment_overhaulConfig INSTANCE = new Sharpness_enchantment_overhaulConfig();

    public static Sharpness_enchantment_overhaulConfig get() { return INSTANCE; }

    public boolean isSharpnessExcluded(EntityType<?> type) {
        if (type == null) return false;
        Identifier id = Registries.ENTITY_TYPE.getId(type);
        return id != null && excludedIdSet.contains(id.toString().toLowerCase());
    }

    public static void load() {
        try {
            if (FILE.exists()) {
                try (FileReader r = new FileReader(FILE)) {
                    INSTANCE = GSON.fromJson(r, Sharpness_enchantment_overhaulConfig.class);
                }
            } else {
                if (FILE.getParentFile() != null) FILE.getParentFile().mkdirs();
                try (FileWriter w = new FileWriter(FILE)) {
                    GSON.toJson(INSTANCE, w);
                }
            }
            // rebuild cache
            INSTANCE.excludedIdSet = new HashSet<>();
            if (INSTANCE.sharpnessExcludedEntityIds != null) {
                for (String s : INSTANCE.sharpnessExcludedEntityIds) {
                    if (s == null || s.isBlank()) continue;
                    String processedId = s.toLowerCase().trim();
                    if (!processedId.contains(":")) {
                        processedId = "minecraft:" + processedId;
                    }
                    INSTANCE.excludedIdSet.add(processedId);
                }
            }
        } catch (Exception e) {
            LOG.error("Failed to load config, using defaults.", e);
        }
    }
}