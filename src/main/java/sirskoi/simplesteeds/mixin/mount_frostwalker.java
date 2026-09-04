package sirskoi.simplesteeds.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sirskoi.simplesteeds.modconfig;

@Mixin(LivingEntity.class)
public class mount_frostwalker {

    @Inject(method = "tick", at = @At("TAIL"))
    private void applyMountFrostWalker(CallbackInfo ci) {
        if (!modconfig.INSTANCE.enableVanillaMountEnchantments) return;

        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.level().isClientSide()) return;

        String className = entity.getClass().getName();
        boolean isMount = className.contains("Horse") || className.contains("Donkey") || className.contains("Mule")
                || entity instanceof net.minecraft.world.entity.animal.camel.Camel
                || className.contains("Strider") || className.contains("Pig")
                || className.contains("HappyGhast");

        if (!isMount) return;

        int maxFrostWalkerLevel = 0;
        var registryAccess = entity.level().registryAccess();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (stack.isEmpty()) continue;

            String itemPath = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
            if (itemPath.contains("harness")) continue;

            int level = EnchantmentHelper.getItemEnchantmentLevel(
                    registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FROST_WALKER),
                    stack
            );
            if (level > maxFrostWalkerLevel) {
                maxFrostWalkerLevel = level;
            }
        }

        if (maxFrostWalkerLevel <= 0) return;

        ServerLevel level = (ServerLevel) entity.level();
        int radius = Math.min(16, 2 + maxFrostWalkerLevel);
        BlockPos entityPos = entity.blockPosition();
        BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos abovePos = new BlockPos.MutableBlockPos();
        BlockState frostedIce = Blocks.FROSTED_ICE.defaultBlockState();
        int baseY = entityPos.getY();

        for (int dy = -1; dy <= 0; dy++) {
            int checkY = baseY + dy;
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if ((dx * dx) + (dz * dz) <= radius * radius) {
                        targetPos.set(entityPos.getX() + dx, checkY, entityPos.getZ() + dz);
                        abovePos.set(targetPos.getX(), checkY + 1, targetPos.getZ());

                        BlockState targetState = level.getBlockState(targetPos);
                        BlockState aboveState = level.getBlockState(abovePos);

                        if (aboveState.isAir() && targetState.is(Blocks.WATER)) {
                            if (targetState.hasProperty(LiquidBlock.LEVEL) && targetState.getValue(LiquidBlock.LEVEL) == 0) {
                                level.setBlockAndUpdate(targetPos, frostedIce);
                                level.scheduleTick(targetPos, Blocks.FROSTED_ICE, Mth.nextInt(entity.getRandom(), 60, 120));
                            }
                        }
                    }
                }
            }
        }
    }
}