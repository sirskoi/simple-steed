package sirskoi.simplesteeds.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ConfigSyncPayload(
        boolean enableLootTableChanges,
        boolean enableVanillaMountEnchantments,
        float momentumSpeedMultiplier,
        float leapingJumpMultiplier,
        float happyGhastSpeedMultiplier,
        float featherFallingReductionPerLevel,
        float protectionReductionPerLevel,
        float fireProtectionReductionPerLevel,
        float blastProtectionReductionPerLevel,
        float projectileProtectionReductionPerLevel,
        float soulSpeedBonusPerLevel
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ConfigSyncPayload> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("simplesteeds", "config_sync"));

    public static final StreamCodec<FriendlyByteBuf, ConfigSyncPayload> CODEC = CustomPacketPayload.codec(
            ConfigSyncPayload::write,
            ConfigSyncPayload::new
    );

    public ConfigSyncPayload(FriendlyByteBuf buf) {
        this(
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat()
        );
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(enableLootTableChanges);
        buf.writeBoolean(enableVanillaMountEnchantments);
        buf.writeFloat(momentumSpeedMultiplier);
        buf.writeFloat(leapingJumpMultiplier);
        buf.writeFloat(happyGhastSpeedMultiplier);
        buf.writeFloat(featherFallingReductionPerLevel);
        buf.writeFloat(protectionReductionPerLevel);
        buf.writeFloat(fireProtectionReductionPerLevel);
        buf.writeFloat(blastProtectionReductionPerLevel);
        buf.writeFloat(projectileProtectionReductionPerLevel);
        buf.writeFloat(soulSpeedBonusPerLevel);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}