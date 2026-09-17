package sirskoi.simplesteeds.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
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
            //bodysuit jump
            if (attribute.equals(Attributes.JUMP_STRENGTH)) { // Changed from .is() to .equals()
                ItemStack armor = horse.getItemBySlot(EquipmentSlot.BODY);
                if (!armor.isEmpty()) {
                    int level = armor.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
                            .getLevel(horse.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantments.LEAPING));

                    if (level > 0) {
                        double original = cir.getReturnValue();
                        double boost = original * (modconfig.INSTANCE.leapingJumpMultiplier * level);
                        cir.setReturnValue(original + boost);
                    }
                }
            }

            //sidesaddle
            if (attribute.equals(Attributes.MOVEMENT_SPEED)) { // Changed from .is() to .equals()
                ItemStack saddle = horse.getItemBySlot(EquipmentSlot.SADDLE);
                if (!saddle.isEmpty()) {
                    int level = saddle.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
                            .getLevel(horse.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantments.MOMENTUM));

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