package sirskoi.simplesteeds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.Locale;

public class modconfig {
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "simplesteeds.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static modconfig INSTANCE = new modconfig();

    //feat & toggles
    public boolean enableLootTableChanges = true;
    public boolean enableVanillaMountEnchantments = true;

    //speed n leap
    public float momentumSpeedMultiplier = 0.3f;
    public float leapingJumpMultiplier = 0.10f;
    public float camelLeapingMultiplier = 0.025f;
    public float happyGhastSpeedMultiplier = 0.50f;

    //prot multipliers
    public float featherFallingReductionPerLevel = 6.0f;
    public float protectionReductionPerLevel = 0.125f;
    public float fireProtectionReductionPerLevel = 0.20f;
    public float blastProtectionReductionPerLevel = 0.20f;
    public float projectileProtectionReductionPerLevel = 0.20f;

    //soul speed
    public float soulSpeedBonusPerLevel = 0.20f;

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try {
                String content = Files.readString(CONFIG_FILE.toPath());

                // read check so // and /* */ comments do not trigger syntax errors
                JsonReader reader = new JsonReader(new StringReader(content));
                reader.setLenient(true);
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

                if (!json.has("enableLootTableChanges")
                        || !json.has("enableVanillaMountEnchantments")
                        || !json.has("happyGhastSpeedMultiplier")
                        || !json.has("leapingJumpMultiplier")
                        || !json.has("camelLeapingMultiplier")
                        || !json.has("momentumSpeedMultiplier")
                        || !json.has("featherFallingReductionPerLevel")
                        || !json.has("protectionReductionPerLevel")
                        || !json.has("fireProtectionReductionPerLevel")
                        || !json.has("blastProtectionReductionPerLevel")
                        || !json.has("projectileProtectionReductionPerLevel")
                        || !json.has("soulSpeedBonusPerLevel")) {
                    CONFIG_FILE.delete();
                } else {
                    INSTANCE = GSON.fromJson(json, modconfig.class);
                    return;
                }
            } catch (Exception e) {
                CONFIG_FILE.delete();
            }
        }

        INSTANCE = new modconfig();
        save();
    }

    public static void save() {
        try {
            String configJson = String.format(Locale.ROOT, """
            {
              // add enchantment books to loot chests
              "enableLootTableChanges": %b,

              // allow vanilla enchantments on mount gear
              "enableVanillaMountEnchantments": %b,

              // speed bonus per momentum level (0.1 = 10%%)
              "momentumSpeedMultiplier": %s,

              // jump bonus per leaping level (0.1 = 10%%)
              "leapingJumpMultiplier": %s,

              // camel jump bonus per leaping level (0.1 = 10%%)
              "camelLeapingMultiplier": %s,

              // speed multiplier when riding happy ghasts (0.1 = 10%%)
              "happyGhastSpeedMultiplier": %s,

              // fall damage absorbed per feather falling level (6.0 = 3 hearts)
              "featherFallingReductionPerLevel": %s,

              // damage reduction per protection level (0.125 = 12.5%%)
              "protectionReductionPerLevel": %s,

              // damage reduction per fire protection level (0.2 = 20%%)
              "fireProtectionReductionPerLevel": %s,

              // damage reduction per blast protection level (0.2 = 20%%)
              "blastProtectionReductionPerLevel": %s,

              // damage reduction per projectile protection level (0.2 = 20%%)
              "projectileProtectionReductionPerLevel": %s,

              // speed bonus on soul blocks per soul speed level
              "soulSpeedBonusPerLevel": %s
            }
            """,
                    INSTANCE.enableLootTableChanges,
                    INSTANCE.enableVanillaMountEnchantments,
                    INSTANCE.momentumSpeedMultiplier,
                    INSTANCE.leapingJumpMultiplier,
                    INSTANCE.camelLeapingMultiplier,
                    INSTANCE.happyGhastSpeedMultiplier,
                    INSTANCE.featherFallingReductionPerLevel,
                    INSTANCE.protectionReductionPerLevel,
                    INSTANCE.fireProtectionReductionPerLevel,
                    INSTANCE.blastProtectionReductionPerLevel,
                    INSTANCE.projectileProtectionReductionPerLevel,
                    INSTANCE.soulSpeedBonusPerLevel
            );

            Files.writeString(CONFIG_FILE.toPath(), configJson);
        } catch (Exception ignored) {}
    }
}