package com.eul4.wrapper;

import org.bukkit.enchantments.Enchantment;

public class EnchantmentWrapper
{
	private final Enchantment enchantment;
	private final int maxCommonLevel;
	private final int maxRareLevel;
	private final int maxLegendaryLevel;
	
	public EnchantmentWrapper
	(
		Enchantment enchantment,
		int maxCommonLevel,
		int maxRareLevel,
		int maxLegendaryLevel
	)
	{
		this.enchantment = enchantment;
		this.maxCommonLevel = maxCommonLevel;
		this.maxRareLevel = maxRareLevel;
		this.maxLegendaryLevel = maxLegendaryLevel;
	}
	
}
