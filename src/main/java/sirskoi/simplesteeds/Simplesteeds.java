package sirskoi.simplesteeds;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import sirskoi.simplesteeds.network.ConfigSyncPayload;

public class Simplesteeds implements ModInitializer {
    @Override
    public void onInitialize() {
        modconfig.load();
        loottable.register();

        // payload reg
        PayloadTypeRegistry.clientboundPlay().register(ConfigSyncPayload.TYPE, ConfigSyncPayload.CODEC);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ConfigSyncPayload payload = new ConfigSyncPayload(
                    modconfig.INSTANCE.enableLootTableChanges,
                    modconfig.INSTANCE.enableVanillaMountEnchantments,
                    modconfig.INSTANCE.momentumSpeedMultiplier,
                    modconfig.INSTANCE.leapingJumpMultiplier,
                    modconfig.INSTANCE.happyGhastSpeedMultiplier,
                    modconfig.INSTANCE.featherFallingReductionPerLevel,
                    modconfig.INSTANCE.protectionReductionPerLevel,
                    modconfig.INSTANCE.fireProtectionReductionPerLevel,
                    modconfig.INSTANCE.blastProtectionReductionPerLevel,
                    modconfig.INSTANCE.projectileProtectionReductionPerLevel,
                    modconfig.INSTANCE.soulSpeedBonusPerLevel
            );
            ServerPlayNetworking.send(handler.getPlayer(), payload);
        });
    }
}