package com.eul4.util;

import com.eul4.wrapper.EnchantType;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class EnchantmentUtil
{
	public static int getEnchantmentLevel(ItemStack item, EnchantType enchantType)
	{
		if(item == null)
		{
			return 0;
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if(meta == null)
		{
			return 0;
		}
		
		return meta.getEnchantLevel(enchantType.getEnchantment());
	}
	
	@NotNull
	private static Enchantment getCustomEnchantment(NamespacedKey namespacedKey)
	{
		final Registry<Enchantment> enchantmentRegistry = RegistryAccess
				.registryAccess()
				.getRegistry(RegistryKey.ENCHANTMENT);
		
		return enchantmentRegistry.getOrThrow(TypedKey.create
		(
			RegistryKey.ENCHANTMENT, namespacedKey)
		);
	}
}
