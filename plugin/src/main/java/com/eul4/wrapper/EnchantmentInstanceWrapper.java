package com.eul4.wrapper;

import com.eul4.enums.Rarity;
import lombok.Getter;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

@Getter
public class EnchantmentInstanceWrapper extends WeightedEntry.IntrusiveBase
{
	private final EnchantmentInstance enchantmentInstance;
	private final EnchantType enchantType;
	private final Rarity rarity;
	
	public EnchantmentInstanceWrapper(EnchantmentInstance enchantmentInstance, Rarity rarity)
	{
		super(getWeight(enchantmentInstance, rarity));
		
		this.enchantmentInstance = enchantmentInstance;
		this.enchantType = EnchantType.asMirror(enchantmentInstance);
		this.rarity = rarity;
	}
	
	public static EnchantmentInstanceWrapper wrap(EnchantmentInstance enchantmentInstance, Rarity rarity)
	{
		return new EnchantmentInstanceWrapper(enchantmentInstance, rarity);
	}
	
	private static int getWeight(EnchantmentInstance enchantmentInstance, Rarity rarity)
	{
		return 10;
//		EnchantType.asMirror(enchantmentInstance).getWeight(rarity);
	}
}
