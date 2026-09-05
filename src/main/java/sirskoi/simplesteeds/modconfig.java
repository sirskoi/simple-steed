package sirskoi.simplesteeds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class modconfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("simplesteeds");
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "simplesteeds.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static modconfig INSTANCE = new modconfig();

    //feat & toggles
    public boolean enableLootTableChanges = true;
    public boolean enableVanillaMountEnchantments = true;

    //speed n leap
    public float momentumSpeedMultiplier = 0.3f;
    public float leapingJumpMultiplier = 0.1f;
    public float happyGhastSpeedMultiplier = 0.3f;

    //prot multipliers
    public float featherFallingReductionPerLevel = 6.0f;
    public float protectionReductionPerLevel = 0.125f;
    public float fireProtectionReductionPerLevel = 0.2f;
    public float blastProtectionReductionPerLevel = 0.2f;
    public float projectileProtectionReductionPerLevel = 0.2f;

    //soul speed
    public float soulSpeedBonusPerLevel = 0.1f;

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try {
                String content = Files.readString(CONFIG_FILE.toPath());
                JsonObject json = JsonParser.parseString(content).getAsJsonObject();

                if (!json.has("enableLootTableChanges")
                        || !json.has("enableVanillaMountEnchantments")
                        || !json.has("happyGhastSpeedMultiplier")
                        || !json.has("leapingJumpMultiplier")
                        || !json.has("momentumSpeedMultiplier")
                        || !json.has("featherFallingReductionPerLevel")
                        || !json.has("protectionReductionPerLevel")
                        || !json.has("fireProtectionReductionPerLevel")
                        || !json.has("blastProtectionReductionPerLevel")
                        || !json.has("projectileProtectionReductionPerLevel")
                        || !json.has("soulSpeedBonusPerLevel")) {
                    Files.deleteIfExists(CONFIG_FILE.toPath());
                } else {
                    INSTANCE = GSON.fromJson(json, modconfig.class);
                    return;
                }
            } catch (Exception e) {
                LOGGER.warn("Failed to load simplesteeds config, regenerating...", e);
                try {
                    Files.deleteIfExists(CONFIG_FILE.toPath());
                } catch (Exception ignored) {}
            }
        }

        INSTANCE = new modconfig();
        save();
    }

    public static void save() {
        try {
            //config directory check
            Path configDir = CONFIG_FILE.getParentFile().toPath();
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }

            String configJson = String.format(Locale.ROOT, """
            {
              "enableLootTableChanges": %b,
              "enableVanillaMountEnchantments": %b,
              "momentumSpeedMultiplier": %s,
              "leapingJumpMultiplier": %s,
              "happyGhastSpeedMultiplier": %s,
              "featherFallingReductionPerLevel": %s,
              "protectionReductionPerLevel": %s,
              "fireProtectionReductionPerLevel": %s,
              "blastProtectionReductionPerLevel": %s,
              "projectileProtectionReductionPerLevel": %s,
              "soulSpeedBonusPerLevel": %s
            }
            """,
                    INSTANCE.enableLootTableChanges,
                    INSTANCE.enableVanillaMountEnchantments,
                    INSTANCE.momentumSpeedMultiplier,
                    INSTANCE.leapingJumpMultiplier,
                    INSTANCE.happyGhastSpeedMultiplier,
                    INSTANCE.featherFallingReductionPerLevel,
                    INSTANCE.protectionReductionPerLevel,
                    INSTANCE.fireProtectionReductionPerLevel,
                    INSTANCE.blastProtectionReductionPerLevel,
                    INSTANCE.projectileProtectionReductionPerLevel,
                    INSTANCE.soulSpeedBonusPerLevel
            );

            Files.writeString(CONFIG_FILE.toPath(), configJson);
        } catch (Exception e) {
            LOGGER.error("Failed to save simplesteeds config", e);
        }
    }
}