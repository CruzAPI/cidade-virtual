package com.eul4.wrapper;

import com.eul4.enums.Rarity;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class StabilityFormula
{
	public static final StabilityFormula STABLE = new StabilityFormula();
	
	private final boolean stable;
	private final float base;
	private final float enchantBase;
	
	private StabilityFormula()
	{
		this.stable = true;
		this.base = 1.0F;
		this.enchantBase = 1.0F;
	}
	
	public StabilityFormula(float base, float enchantBase)
	{
		this.stable = false;
		this.base = base;
		this.enchantBase = enchantBase;
	}
	
	public float calculateChance(int stabilityLevel, Rarity blockRarity, Rarity areaRarity)
	{
		if(stable)
		{
			return 1.0F;
		}
		
		final int rarityDiff = blockRarity.ordinal() - areaRarity.ordinal();
		final float calculatedBase = (float) Math.pow(this.base, rarityDiff);
		final float calculatedEnchantBase = (float) Math.pow(this.enchantBase, rarityDiff);
		final float enchantIncremention = calculatedEnchantBase * stabilityLevel;
		final float calculatedChance = calculatedBase + enchantIncremention;
		
		return Math.max(0.0F, Math.min(1.0F, calculatedChance));
	}
}
