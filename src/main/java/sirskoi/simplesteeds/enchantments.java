package sirskoi.simplesteeds;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class enchantments {

    public static final ResourceKey<Enchantment> MOMENTUM = ResourceKey.create(
            Registries.ENCHANTMENT,
            Identifier.fromNamespaceAndPath("simplesteeds", "momentum")
    );

    public static final ResourceKey<Enchantment> LEAPING = ResourceKey.create(
            Registries.ENCHANTMENT,
            Identifier.fromNamespaceAndPath("simplesteeds", "leaping")
    );

    public static void registerenchantments() {
        System.out.println("Registering simplesteeds Enchantments...");
    }
}