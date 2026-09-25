package sirskoi.simplesteeds.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import sirskoi.simplesteeds.modconfig;
import sirskoi.simplesteeds.network.ConfigSyncPayload;

public class SimplesteedsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                // singleplayer ignore payload
                if (context.client().hasSingleplayerServer()) {
                    modconfig.INSTANCE.isServerConfig = false;
                    return;
                }

                // if not on dedicated server lock and sync
                modconfig.INSTANCE.enableLootTableChanges = payload.enableLootTableChanges();
                modconfig.INSTANCE.enableVanillaMountEnchantments = payload.enableVanillaMountEnchantments();
                modconfig.INSTANCE.momentumSpeedMultiplier = payload.momentumSpeedMultiplier();
                modconfig.INSTANCE.leapingJumpMultiplier = payload.leapingJumpMultiplier();
                modconfig.INSTANCE.happyGhastSpeedMultiplier = payload.happyGhastSpeedMultiplier();
                modconfig.INSTANCE.featherFallingReductionPerLevel = payload.featherFallingReductionPerLevel();
                modconfig.INSTANCE.protectionReductionPerLevel = payload.protectionReductionPerLevel();
                modconfig.INSTANCE.fireProtectionReductionPerLevel = payload.fireProtectionReductionPerLevel();
                modconfig.INSTANCE.blastProtectionReductionPerLevel = payload.blastProtectionReductionPerLevel();
                modconfig.INSTANCE.projectileProtectionReductionPerLevel = payload.projectileProtectionReductionPerLevel();
                modconfig.INSTANCE.soulSpeedBonusPerLevel = payload.soulSpeedBonusPerLevel();

                modconfig.INSTANCE.isServerConfig = true;
            });
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (modconfig.INSTANCE.isServerConfig) {
                modconfig.INSTANCE.isServerConfig = false;
                modconfig.load();
            }
        });
    }
}