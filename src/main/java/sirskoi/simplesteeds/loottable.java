package sirskoi.simplesteeds;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class loottable {

    public static void register() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!modconfig.INSTANCE.enableLootTableChanges) return;
            if (!source.isBuiltin()) return;

            if (key.equals(BuiltInLootTables.BASTION_TREASURE) ||
                    key.equals(BuiltInLootTables.NETHER_BRIDGE) ||
                    key.equals(BuiltInLootTables.TRIAL_CHAMBERS_REWARD)) {

                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1));

                var momentum1 = LootItem.lootTableItem(Items.ENCHANTED_BOOK)
                        .apply(new SetEnchantmentsFunction.Builder()
                                .withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantments.MOMENTUM), ContextIntProviders.exactly(1))
                        );
                var momentum2 = LootItem.lootTableItem(Items.ENCHANTED_BOOK)
                        .apply(new SetEnchantmentsFunction.Builder()
                                .withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantments.MOMENTUM), ContextIntProviders.exactly(2))
                        );
                var momentum3 = LootItem.lootTableItem(Items.ENCHANTED_BOOK)
                        .apply(new SetEnchantmentsFunction.Builder()
                                .withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantments.MOMENTUM), ContextIntProviders.exactly(3))
                        );

                var leaping1 = LootItem.lootTableItem(Items.ENCHANTED_BOOK)
                        .apply(new SetEnchantmentsFunction.Builder()
                                .withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantments.LEAPING), ContextIntProviders.exactly(1))
                        );
                var leaping2 = LootItem.lootTableItem(Items.ENCHANTED_BOOK)
                        .apply(new SetEnchantmentsFunction.Builder()
                                .withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantments.LEAPING), ContextIntProviders.exactly(2))
                        );
                var leaping3 = LootItem.lootTableItem(Items.ENCHANTED_BOOK)
                        .apply(new SetEnchantmentsFunction.Builder()
                                .withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantments.LEAPING), ContextIntProviders.exactly(3))
                        );

                //momentum 10x Level 1 [50%], 7x Level 2 [35%], 3x Level 3 [15%]
                for (int i = 0; i < 10; i++) poolBuilder.add(momentum1);
                for (int i = 0; i < 7; i++) poolBuilder.add(momentum2);
                for (int i = 0; i < 3; i++) poolBuilder.add(momentum3);

                //leaping 10x Level 1 [50%], 7x Level 2 [35%], 3x Level 3 [15%]
                for (int i = 0; i < 10; i++) poolBuilder.add(leaping1);
                for (int i = 0; i < 7; i++) poolBuilder.add(leaping2);
                for (int i = 0; i < 3; i++) poolBuilder.add(leaping3);

                tableBuilder.withPool(poolBuilder);
            }
        });
    }
}