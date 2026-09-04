package sirskoi.simplesteeds.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sirskoi.simplesteeds.modconfig;

@Mixin(LivingEntity.class)
public class mount_soulspeed {

    private int getMountSoulSpeedLevel(LivingEntity entity) {
        if (!modconfig.INSTANCE.enableVanillaMountEnchantments) return 0;

        String className = entity.getClass().getName();
        boolean isMount = className.contains("Horse") || className.contains("Donkey") || className.contains("Mule")
                || entity instanceof net.minecraft.world.entity.animal.camel.Camel
                || className.contains("Strider") || className.contains("Pig")
                || className.contains("HappyGhast");

        if (!isMount) return 0;

        int maxLevel = 0;
        var registryAccess = entity.level().registryAccess();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (stack.isEmpty()) continue;

            String itemPath = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
            if (itemPath.contains("harness")) continue;

            int level = EnchantmentHelper.getItemEnchantmentLevel(
                    registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SOUL_SPEED),
                    stack
            );
            if (level > maxLevel) {
                maxLevel = level;
            }
        }
        return maxLevel;
    }

    @Inject(method = "getBlockSpeedFactor", at = @At("RETURN"), cancellable = true)
    private void applyMountSoulSpeedBoost(CallbackInfoReturnable<Float> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        int soulSpeedLevel = getMountSoulSpeedLevel(entity);

        if (soulSpeedLevel > 0 && entity.getBlockStateOn().is(BlockTags.SOUL_SPEED_BLOCKS)) {
            float boost = 1.0f + (soulSpeedLevel * modconfig.INSTANCE.soulSpeedBonusPerLevel);
            cir.setReturnValue(boost);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void spawnSoulParticles(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.level().isClientSide() || !entity.onGround()) return;

        int soulSpeedLevel = getMountSoulSpeedLevel(entity);
        if (soulSpeedLevel > 0 && entity.getBlockStateOn().is(BlockTags.SOUL_SPEED_BLOCKS)) {
            if (entity.getDeltaMovement().horizontalDistanceSqr() > 0.0001 || entity.getControllingPassenger() != null) {
                if (entity.getRandom().nextFloat() < 0.25f) {
                    ServerLevel serverLevel = (ServerLevel) entity.level();
                    serverLevel.sendParticles(
                            ParticleTypes.SOUL,
                            entity.getX(), entity.getY() + 0.1, entity.getZ(),
                            2, 0.2, 0.1, 0.2, 0.02
                    );
                }
            }
        }
    }
}