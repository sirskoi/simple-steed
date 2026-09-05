package sirskoi.simplesteeds.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sirskoi.simplesteeds.modconfig;
import sirskoi.simplesteeds.enchantments;

@Mixin(LivingEntity.class)
public class mount_jumpboost {

    // Suppress the false-positive IDE warning about closing the 'Level' resource
    @SuppressWarnings("resource")
    @Inject(method = "getAttributeValue", at = @At("RETURN"), cancellable = true)
    private void applyLeapingAttributeJump(Holder<Attribute> attribute, CallbackInfoReturnable<Double> cir) {

        // Replaced the deprecated .is(Holder) with .equals()
        if (attribute.equals(Attributes.JUMP_STRENGTH)) {
            LivingEntity entity = (LivingEntity) (Object) this;
            String className = entity.getClass().getName();

            boolean isHorseLike = className.contains("Horse") || className.contains("Donkey") || className.contains("Mule");

            if (isHorseLike) {
                //saddle and harness check
                ItemStack equipment = entity.getItemBySlot(EquipmentSlot.SADDLE);
                if (equipment.isEmpty()) {
                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        ItemStack stack = entity.getItemBySlot(slot);
                        if (!stack.isEmpty()) {
                            String path = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
                            if (path.contains("harness")) {
                                equipment = stack;
                                break;
                            }
                        }
                    }
                }

                if (!equipment.isEmpty()) {
                    int level = EnchantmentHelper.getItemEnchantmentLevel(
                            entity.level().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT).getOrThrow(enchantments.LEAPING),
                            equipment
                    );
                    if (level > 0) {
                        double boost = 1.0 + (level * modconfig.INSTANCE.leapingJumpMultiplier);
                        cir.setReturnValue(cir.getReturnValue() * boost);
                    }
                }
            }
        }
    }
}