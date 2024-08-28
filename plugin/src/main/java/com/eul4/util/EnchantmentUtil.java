package com.eul4.util;

import com.eul4.enums.PluginNamespacedKey;
import com.eul4.enums.Rarity;
import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

public class EnchantmentUtil
{
	public static final Enchantment STABILITY = getCustomEnchantment(PluginNamespacedKey.ENCHANTMENT_STABILITY);
	
	public static int getMaxLevel(Enchantment enchantment, Rarity rarity)
	{
		switch(rarity)
		{
		case RARE:
			if
			(
				enchantment.equals(Enchantment.PROTECTION)
				|| enchantment.equals(Enchantment.BLAST_PROTECTION)
				|| enchantment.equals(Enchantment.FIRE_PROTECTION)
				|| enchantment.equals(Enchantment.PROJECTILE_PROTECTION)
				|| enchantment.equals(Enchantment.FEATHER_FALLING)
			)
			{
				return 7;
			}
		case LEGENDARY:
		}
		
		return enchantment.getMaxLevel();
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
