package com.eul4.wrapper;

import com.eul4.enums.Rarity;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class StabilityFormula
{
	public static final StabilityFormula STABLE = new StabilityFormula();
	public static final StabilityFormula PLACED = new StabilityFormula(1.0F, 0.5F, 0.1F, 0.5F);
	public static final StabilityFormula ORE_VEIN = PLACED;
	
	private final boolean stable;
	private final float base;
	private final float baseMultiplier;
	private final float enchantBase;
	private final float enchantBaseMultiplier;
	
	private StabilityFormula()
	{
		this.stable = true;
		this.base = 0.0F;
		this.baseMultiplier = 0.0F;
		this.enchantBase = 0.0F;
		this.enchantBaseMultiplier = 0.0F;
	}
	
	public StabilityFormula(float base, float enchantBase)
	{
		this(base, 1.0F, enchantBase, 1.0F);
	}
	
	public StabilityFormula
	(
		float base,
		float baseMultiplier,
		float enchantBase,
		float enchantBaseMultiplier
	)
	{
		this.stable = false;
		this.base = base;
		this.baseMultiplier = baseMultiplier;
		this.enchantBase = enchantBase;
		this.enchantBaseMultiplier = enchantBaseMultiplier;
	}
	
	public float calculateChance(int stabilityLevel, Rarity blockRarity, Rarity areaRarity)
	{
		if(stable || blockRarity == Rarity.COMMON)
		{
			return 1.0F;
		}
		
		final int rarityDiff = blockRarity.ordinal() - areaRarity.ordinal();
		final float calculatedBase = this.base * (float) Math.pow(this.baseMultiplier, rarityDiff);
		final float calculatedEnchantBase = this.enchantBase * (float) Math.pow(this.enchantBaseMultiplier, rarityDiff);
		final float enchantIncremention = calculatedEnchantBase * stabilityLevel;
		final float calculatedChance = calculatedBase + enchantIncremention;
		
		return Math.max(0.0F, Math.min(1.0F, calculatedChance));
	}
}
