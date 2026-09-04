package sirskoi.simplesteeds.mixin;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sirskoi.simplesteeds.modconfig;

@Mixin(LivingEntity.class)
public class mount_prot {

    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true)
    private float modifyMountDamage(float amount, ServerLevel level, DamageSource source) {
        if (!modconfig.INSTANCE.enableVanillaMountEnchantments) return amount;

        LivingEntity entity = (LivingEntity) (Object) this;
        String className = entity.getClass().getName();

        boolean isMount = className.contains("Horse") || className.contains("Donkey") || className.contains("Mule")
                || entity instanceof net.minecraft.world.entity.animal.camel.Camel
                || className.contains("Strider") || className.contains("Pig")
                || className.contains("HappyGhast");

        if (!isMount) return amount;

        modconfig modconfig = sirskoi.simplesteeds.modconfig.INSTANCE;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (stack.isEmpty()) continue;

            var registryAccess = entity.level().registryAccess();
            String itemPath = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
            boolean isHarness = itemPath.contains("harness");

            //frost walker hot floor immunity
            if (source.is(DamageTypes.HOT_FLOOR) && !isHarness) {
                int frostWalkerLevel = EnchantmentHelper.getItemEnchantmentLevel(
                        registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FROST_WALKER),
                        stack
                );
                if (frostWalkerLevel > 0) {
                    return 0.0f;
                }
            }

            //feather falling check
            if (source.is(DamageTypes.FALL) && !isHarness) {
                int featherFallingLevel = EnchantmentHelper.getItemEnchantmentLevel(
                        registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FEATHER_FALLING),
                        stack
                );
                if (featherFallingLevel > 0) {
                    amount = Math.max(0.0f, amount - (featherFallingLevel * modconfig.featherFallingReductionPerLevel));
                }
            }

            //prot check
            int protectionLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.PROTECTION),
                    stack
            );
            if (protectionLevel > 0 && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                amount *= Math.max(0.0f, 1.0f - (protectionLevel * modconfig.protectionReductionPerLevel));
            }

            //fire prot check
            if (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.LAVA)) {
                int fireProtLevel = EnchantmentHelper.getItemEnchantmentLevel(
                        registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FIRE_PROTECTION),
                        stack
                );
                if (fireProtLevel > 0) {
                    amount *= Math.max(0.0f, 1.0f - (fireProtLevel * modconfig.fireProtectionReductionPerLevel));
                }
            }

            //blast prot check
            if (source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)) {
                int blastProtLevel = EnchantmentHelper.getItemEnchantmentLevel(
                        registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.BLAST_PROTECTION),
                        stack
                );
                if (blastProtLevel > 0) {
                    amount *= Math.max(0.0f, 1.0f - (blastProtLevel * modconfig.blastProtectionReductionPerLevel));
                }
            }

            //proj prot check
            if (source.is(DamageTypes.ARROW) || source.is(DamageTypes.TRIDENT) || source.is(DamageTypes.FIREBALL)) {
                int projProtLevel = EnchantmentHelper.getItemEnchantmentLevel(
                        registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.PROJECTILE_PROTECTION),
                        stack
                );
                if (projProtLevel > 0) {
                    amount *= Math.max(0.0f, 1.0f - (projProtLevel * modconfig.projectileProtectionReductionPerLevel));
                }
            }
        }

        return Math.max(0.0f, amount);
    }
}