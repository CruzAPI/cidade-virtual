package com.eul4.wrapper;

import com.eul4.enums.Rarity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
public class StabilityFormula
{
	public static final StabilityFormula STABLE = new StabilityFormula();
	public static final StabilityFormula UNSTABLE = new StabilityFormula(0.0F, 0.0F, 0.0F, 0.0F);
	
	public static final StabilityFormula PLACED = new StabilityFormula(1.0F, 0.5F, 0.1F, 0.5F);
	public static final StabilityFormula ORE_VEIN = PLACED;
	
	public static final StabilityFormula NETHER_WART = new StabilityFormula(1.0F / 6.0F, 0.5F, 0.056060F, 0.5F);
	public static final StabilityFormula MELON = new StabilityFormula(0.0F, 0.0F, 0.1F, 0.1F);
	public static final StabilityFormula PUMPKIN = new StabilityFormula(0.0F, 0.0F, 0.1F, 0.1F);
	public static final StabilityFormula SUGAR_CANE = new StabilityFormula(0.0F, 0.0F, 0.1F, 0.1F);
	public static final StabilityFormula CACTUS = new StabilityFormula(0.0F, 0.0F, 0.1F, 0.1F);
	public static final StabilityFormula AMETHYST = new StabilityFormula(0.0F, 0.0F, 0.1F, 0.1F);
	public static final StabilityFormula CHORUS_PLANT = new StabilityFormula(0.0F, 0.0F, 0.1F, 0.1F);
	public static final StabilityFormula MUSHROOM = new StabilityFormula(0.0F, 0.0F, 0.1F, 0.1F);
	public static final StabilityFormula VINE = new StabilityFormula(0.0F, 0.0F, 0.1F, 0.1F);
	public static final StabilityFormula BAMBOO = new StabilityFormula(0.0F, 0.0F, 0.1F, 0.1F);
	public static final StabilityFormula DRIPSTONE = new StabilityFormula(0.0F, 0.0F, 0.1F, 0.1F);
	public static final StabilityFormula COCOA = new StabilityFormula(1.0F / 3.0F, 1.0F / 3.0F, 2.0F / 30.0F, 0.7F);
	public static final StabilityFormula CARROT = new StabilityFormula(1.0F / 2.0F / 3.71F, 1.0F / 3.0F, (1.0F - (1.0F / 2.0F / 3.71F)) / 10.0F, 0.45F);
	public static final StabilityFormula WHEAT_SEEDS = new StabilityFormula(1.0F / 2.0F / 2.71F, 1.0F / 3.0F, (1.0F - (1.0F / 2.0F / 2.71F)) / 10.0F, 0.5F);
	public static final StabilityFormula BERRIES = new StabilityFormula(1.0F / 2.0F / 2.5F, 1.0F / 2.0F, (1.0F - (1.0F / 2.0F / 2.5F)) / 10.0F, 0.55F);
	public static final StabilityFormula GLOW_BERRIES = new StabilityFormula(0.0F, 0.0F, 0.1F, 0.1F);
	public static final StabilityFormula BEETROOT_SEEDS = WHEAT_SEEDS;
	public static final StabilityFormula POTATO = CARROT;
	
	public static final StabilityFormula GRASS_BLOCK = PLACED;
	public static final StabilityFormula MYCELIUM = PLACED;
	public static final StabilityFormula COBBLESTONE = new StabilityFormula(0.0F, 0.0F, 0.01F, 0.1F);
	public static final StabilityFormula STONE = new StabilityFormula(0.0F, 0.0F, 0.01F, 0.1F);
	public static final StabilityFormula BASALT = new StabilityFormula(0.0F, 0.0F, 0.01F, 0.1F);
	public static final StabilityFormula CONCRETE = new StabilityFormula(0.0F, 0.0F, 0.01F, 0.1F);
	public static final StabilityFormula ICE = new StabilityFormula(0.0F, 0.0F, 0.01F, 0.1F);
	public static final StabilityFormula SNOW = new StabilityFormula(0.0F, 0.0F, 0.01F, 0.1F);
	public static final StabilityFormula OBSIDIAN = STABLE;
	
	public static final Map<Material, StabilityFormula> GROWTH_STABILITY_FORMULA_BY_MATERIAL = Map.ofEntries
	(
		Map.entry(Material.MYCELIUM, MYCELIUM),
		Map.entry(Material.GRASS_BLOCK, GRASS_BLOCK),
		Map.entry(Material.COBBLESTONE, COBBLESTONE),
		Map.entry(Material.STONE, STONE),
		Map.entry(Material.BASALT, BASALT),
		Map.entry(Material.OBSIDIAN, OBSIDIAN),
		Map.entry(Material.ICE, ICE),
		Map.entry(Material.SNOW, SNOW),
		Map.entry(Material.SNOW_BLOCK, SNOW),
		
		Map.entry(Material.CAVE_VINES, GLOW_BERRIES),
		Map.entry(Material.SWEET_BERRIES, BERRIES),
		Map.entry(Material.SWEET_BERRY_BUSH, BERRIES),
		Map.entry(Material.GLOW_BERRIES, BERRIES),
		Map.entry(Material.BAMBOO, BAMBOO),
		Map.entry(Material.POINTED_DRIPSTONE, DRIPSTONE),
		Map.entry(Material.BAMBOO_SAPLING, BAMBOO),
		Map.entry(Material.RED_MUSHROOM, MUSHROOM),
		Map.entry(Material.BROWN_MUSHROOM, MUSHROOM),
		Map.entry(Material.WHEAT, WHEAT_SEEDS),
		Map.entry(Material.WHEAT_SEEDS, WHEAT_SEEDS),
		Map.entry(Material.BEETROOT, BEETROOT_SEEDS),
		Map.entry(Material.BEETROOTS, BEETROOT_SEEDS),
		Map.entry(Material.BEETROOT_SEEDS, BEETROOT_SEEDS),
		Map.entry(Material.CARROT, CARROT),
		Map.entry(Material.CARROTS, CARROT),
		Map.entry(Material.POTATO, POTATO),
		Map.entry(Material.POTATOES, POTATO),
		Map.entry(Material.VINE, VINE),
		Map.entry(Material.CHORUS_PLANT, CHORUS_PLANT),
		Map.entry(Material.CHORUS_FLOWER, CHORUS_PLANT),
		Map.entry(Material.SMALL_AMETHYST_BUD, AMETHYST),
		Map.entry(Material.MEDIUM_AMETHYST_BUD, AMETHYST),
		Map.entry(Material.LARGE_AMETHYST_BUD, AMETHYST),
		Map.entry(Material.AMETHYST_CLUSTER, AMETHYST),
		Map.entry(Material.MELON, MELON),
		Map.entry(Material.PUMPKIN, PUMPKIN),
		Map.entry(Material.SUGAR_CANE, SUGAR_CANE),
		Map.entry(Material.CACTUS, CACTUS),
		Map.entry(Material.PUMPKIN_STEM, UNSTABLE),
		Map.entry(Material.MELON_STEM, UNSTABLE),
		Map.entry(Material.NETHER_WART, NETHER_WART)
	);
	
	public static final Map<Material, StabilityFormula> PLACEMENT_STABILITY_FORMULA_BY_MATERIAL;
	
	static
	{
		Map<Material, StabilityFormula> placementStabilityFormulaByMaterial = new HashMap<>(GROWTH_STABILITY_FORMULA_BY_MATERIAL);
		
		placementStabilityFormulaByMaterial.put(Material.SPAWNER, STABLE);
		placementStabilityFormulaByMaterial.put(Material.OBSIDIAN, STABLE);
		placementStabilityFormulaByMaterial.remove(Material.STONE);
		placementStabilityFormulaByMaterial.remove(Material.COBBLESTONE);
		placementStabilityFormulaByMaterial.remove(Material.BASALT);
		placementStabilityFormulaByMaterial.remove(Material.ICE);
		placementStabilityFormulaByMaterial.remove(Material.SNOW);
		placementStabilityFormulaByMaterial.remove(Material.SNOW_BLOCK);
		placementStabilityFormulaByMaterial.remove(Material.MYCELIUM);
		placementStabilityFormulaByMaterial.remove(Material.GRASS_BLOCK);
		
		PLACEMENT_STABILITY_FORMULA_BY_MATERIAL = Collections.unmodifiableMap(placementStabilityFormulaByMaterial);
	}
	
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
//		if(stable || blockRarity == Rarity.COMMON)
		if(stable)
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
