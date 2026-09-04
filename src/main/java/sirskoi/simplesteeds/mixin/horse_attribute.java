package sirskoi.simplesteeds.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sirskoi.simplesteeds.modconfig;
import sirskoi.simplesteeds.enchantments;

@Mixin(LivingEntity.class)
public class horse_attribute {

    @Inject(method = "getAttributeValue", at = @At("RETURN"), cancellable = true)
    private void modifyHorseAttributes(Holder<Attribute> attribute, CallbackInfoReturnable<Double> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof AbstractHorse horse) {
            //bodyslotjump
            if (attribute.is(Attributes.JUMP_STRENGTH)) {
                ItemStack armor = horse.getItemBySlot(EquipmentSlot.BODY);
                if (!armor.isEmpty()) {
                    int level = EnchantmentHelper.getItemEnchantmentLevel(
                            horse.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantments.LEAPING),
                            armor
                    );
                    if (level > 0) {
                        double original = cir.getReturnValue();
                        double boost = original * (modconfig.INSTANCE.leapingJumpMultiplier * level);
                        cir.setReturnValue(original + boost);
                    }
                }
            }

            //saddleslotjump
            if (attribute.is(Attributes.MOVEMENT_SPEED)) {
                ItemStack saddle = horse.getItemBySlot(EquipmentSlot.SADDLE);
                if (!saddle.isEmpty()) {
                    int level = EnchantmentHelper.getItemEnchantmentLevel(
                            horse.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantments.MOMENTUM),
                            saddle
                    );
                    if (level > 0) {
                        double original = cir.getReturnValue();
                        double boost = original * (modconfig.INSTANCE.momentumSpeedMultiplier * level);
                        cir.setReturnValue(original + boost);
                    }
                }
            }
        }
    }
}