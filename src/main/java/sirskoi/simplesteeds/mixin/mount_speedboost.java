package sirskoi.simplesteeds.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sirskoi.simplesteeds.modconfig;
import sirskoi.simplesteeds.enchantments;

@Mixin(LivingEntity.class)
public class mount_speedboost {

    @Inject(method = "getAttributeValue", at = @At("RETURN"), cancellable = true)
    private void applyMomentumAttributeSpeed(Holder<Attribute> attribute, CallbackInfoReturnable<Double> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        //playercheck
        boolean riddenByPlayer = entity.isVehicle() && entity.getFirstPassenger() instanceof Player;
        if (!riddenByPlayer) {
            return;
        }
        String className = entity.getClass().getName();

        boolean isCamel = entity instanceof Camel;
        boolean isPig = className.contains(".Pig") || className.endsWith("Pig");
        boolean isStrider = entity instanceof Strider;
        boolean isHappyGhast = className.contains("HappyGhast") || className.contains("happyghast");

        if (isCamel || isPig || isStrider) {
            if (attribute.equals(Attributes.MOVEMENT_SPEED)) {
                ItemStack saddle = entity.getItemBySlot(EquipmentSlot.SADDLE);
                if (!saddle.isEmpty()) {
                    applyBoost(entity, saddle, cir, modconfig.INSTANCE.momentumSpeedMultiplier);
                }
            }
        } else if (isHappyGhast) {
            //happy ghast flying speed no movement speed
            if (attribute.equals(Attributes.FLYING_SPEED) || attribute.equals(Attributes.MOVEMENT_SPEED)) {
                ItemStack harness = ItemStack.EMPTY;
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    ItemStack stack = entity.getItemBySlot(slot);
                    if (!stack.isEmpty()) {
                        String path = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
                        if (path.contains("harness")) {
                            harness = stack;
                            break;
                        }
                    }
                }

                if (!harness.isEmpty()) {
                    applyBoost(entity, harness, cir, modconfig.INSTANCE.happyGhastSpeedMultiplier);
                }
            }
        }
    }

    //custom mixin
    @Unique
    @SuppressWarnings("resource")
    private void applyBoost(LivingEntity entity, ItemStack stack, CallbackInfoReturnable<Double> cir, double multiplier) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(
                entity.level().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT).getOrThrow(enchantments.MOMENTUM),
                stack
        );
        if (level > 0) {
            double boost = 1.0 + (level * multiplier);
            cir.setReturnValue(cir.getReturnValue() * boost);
        }
    }
}