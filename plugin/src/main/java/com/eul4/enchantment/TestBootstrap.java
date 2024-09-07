package com.eul4.enchantment;

import com.eul4.enums.PluginNamespacedKey;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.tags.EnchantmentTagKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public class TestBootstrap implements PluginBootstrap
{
	@Override
	public void bootstrap(@NotNull BootstrapContext context)
	{
		context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> event.registry().register
		(
			TypedKey.create(RegistryKey.ENCHANTMENT, PluginNamespacedKey.ENCHANTMENT_STABILITY),
			b -> b.description(Component.text("Stability"))
					.supportedItems(event.getOrCreateTag(ItemTypeTagKeys.SWORDS))
					.anvilCost(1)
					.maxLevel(10)
					.weight(10)
					.minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
					.maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
					.activeSlots(EquipmentSlotGroup.ANY)
		)));
	}
}
