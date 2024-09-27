package com.eul4.enums;

import com.eul4.common.i18n.Message;
import com.eul4.i18n.PluginMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Material;
import org.bukkit.Tag;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

@RequiredArgsConstructor
@Getter
public enum Rarity
{
	COMMON
	(
		(byte) 0, 1.0F, 1.0F, 1, 50, 0.2D,
		PluginMessage.COMMON,
		PluginMessage.RARITY_COMMON,
		PluginMessage.COMMON_INCOMPATIBILITY_$TRANSLATABLE,
		style(GREEN, BOLD),
		BossBar.Color.GREEN
	),
	
	RARE
	(
		(byte) 1, 10.0F, 10.0F, 3, 100, 0.1D,
		PluginMessage.RARE,
		PluginMessage.RARITY_RARE,
		PluginMessage.RARE_INCOMPATIBILITY_$TRANSLATABLE,
		style(DARK_PURPLE, BOLD),
		BossBar.Color.PURPLE
	),
	
	LEGENDARY
	(
		(byte) 2, 100.0F, 100.0F, 9, 300, 0.0D,
		PluginMessage.LEGENDARY,
		PluginMessage.RARITY_LEGENDARY,
		PluginMessage.LEGENDARY_INCOMPATIBILITY_$TRANSLATABLE,
		style(GOLD, BOLD),
		BossBar.Color.RED
	),
	;
	
	public static final Rarity DEFAULT_RARITY = COMMON;
	public static final Rarity MIN_RARITY = COMMON;
	public static final Rarity MAX_RARITY = LEGENDARY;
	
	private static final int MOB_MAX_HEALTH_BASE = 10;
	private static final int DAMAGE_EXPONENTIATION_BASE = 10;
	private static final int ITEM_DAMAGE_EXPONENTIATION_BASE = 10;
	private static final int ARMOR_DAMAGE_EXPONENTIATION_BASE = 5;
	private static final int SHIELD_DAMAGE_EXPONENTIATION_BASE = 5;
	private static final double FIREWORK_ELYTRA_BOOST_EXPONENTIATION_BASE = 1.2D;
	private static final double FIREWORK_ELYTRA_DURATION_EXPONENTIATION_BASE = 2.0D;
	private static final double ELYTRA_DURABILITY_EXPONENTIATION_BASE = 2.0D;
	private static final double ARMOR_DURABILITY_EXPONENTIATION_BASE = 5.0D;
	private static final double SHIELD_DURABILITY_EXPONENTIATION_BASE = 5.0D;
	private static final double TOOLS_DURABILITY_EXPONENTIATION_BASE = 10.0D;
	private static final double ANVIL_DURABILITY_RANDOM_BOUND_EXPONENTIATION_BASE = 3.0D;
	
	private final byte id;
	private final float maxHealth;
	private final float explosionMultiplierDamage;
	private final int bookshelfBonus;
	private final int enchantmentRandomBound;
	private final double elytraGravityDebuff;
	
	private final Message rawMessage;
	private final Message stylizedMessage;
	private final Message containerIncompatibilityMessage;
	private final Style style;
	private final BossBar.Color bossBarColor;
	
	public static Rarity getRarityById(byte id)
	{
		for(Rarity rarity : values())
		{
			if(rarity.getId() == id)
			{
				return rarity;
			}
		}
		
		return null;
	}
	
	public int subtract(Rarity other)
	{
		return this.ordinal() - other.ordinal();
	}
	
	public int getMaxEnchantmentBonus()
	{
		return bookshelfBonus * 15;
	}
	
	public int getLevel()
	{
		return ordinal() + 1;
	}
	
	public double getMobMaxHealthMultiplier()
	{
		return Math.pow(MOB_MAX_HEALTH_BASE, ordinal()) * getLevel();
	}
	
	public double getDamageMultiplier()
	{
		return Math.pow(DAMAGE_EXPONENTIATION_BASE, ordinal());
	}
	
	public int getItemDamageMultiplier()
	{
		return (int) Math.pow(ITEM_DAMAGE_EXPONENTIATION_BASE, ordinal());
	}
	
	public int getArmorDamageMultiplier()
	{
		return (int) Math.pow(ARMOR_DAMAGE_EXPONENTIATION_BASE, ordinal());
	}
	
	public int getShieldDamageMultiplier()
	{
		return (int) Math.pow(SHIELD_DAMAGE_EXPONENTIATION_BASE, 1);
	}
	
	public double getFireworkElytraBoostMultiplier()
	{
		return Math.pow(FIREWORK_ELYTRA_BOOST_EXPONENTIATION_BASE, ordinal() - LEGENDARY.ordinal());
	}
	
	public double getFireworkElytraDurationMultiplier()
	{
		return Math.pow(FIREWORK_ELYTRA_DURATION_EXPONENTIATION_BASE, ordinal() - LEGENDARY.ordinal());
	}
	
	public double getDurabilityMultiplier(Material material)
	{
		return getRelativeDurabilityMultiplier(material, this.ordinal());
	}
	
	public double getRelativeDurabilityMultiplier(Material material, Rarity relative)
	{
		return Math.pow(getDurabilityBaseExponentiationBase(material), this.subtract(relative));
	}
	
	public int getAnvilDurabilityRandomBound()
	{
		return Math.max(1, (int) Math.pow(ANVIL_DURABILITY_RANDOM_BOUND_EXPONENTIATION_BASE, ordinal()));
	}
	
	public int getAnvilMaxRepairCost()
	{
		return (int) (40.0 * Math.pow(4.0D, ordinal()));
	}
	
	public int getEnchantmentMaxLevel()
	{
		return 30 * (int) Math.pow(3, ordinal());
	}
	
	public double getScalarMultiplier(double base)
	{
		return Math.pow(base, ordinal());
	}
	
	public int getScalarMultiplier(int base)
	{
		return (int) Math.pow(base, ordinal());
	}
	
	public float getScalarMultiplier(float base)
	{
		return (float) Math.pow(base, ordinal());
	}
	
	public double getIntMultiplier()
	{
		return (int) getMultiplier();
	}
	
	public float getFloatMultiplier()
	{
		return (float) getMultiplier();
	}
	
	public double getMultiplier()
	{
		return Math.pow(10.0D, ordinal());
	}
	
	private double getRelativeDurabilityMultiplier(Material material, double exponent)
	{
		return Math.pow(getDurabilityBaseExponentiationBase(material),
				exponent + getDurabilityExponentConstant(material));
	}
	
	public static double getDurabilityExponentConstant(Material material)
	{
		if(material == Material.ELYTRA)
		{
			return -LEGENDARY.ordinal();
		}
		
		return -COMMON.ordinal();
	}
	
	public static double getDurabilityBaseExponentiationBase(Material material)
	{
		if(material == Material.ELYTRA)
		{
			return ELYTRA_DURABILITY_EXPONENTIATION_BASE;
		}
		
		if(isArmor(material))
		{
			return ARMOR_DURABILITY_EXPONENTIATION_BASE;
		}
		
		if(material == Material.SHIELD)
		{
			return SHIELD_DURABILITY_EXPONENTIATION_BASE;
		}
		
		return TOOLS_DURABILITY_EXPONENTIATION_BASE;
	}
	
	private static boolean isArmor(Material type)
	{
		return Tag.ITEMS_HEAD_ARMOR.isTagged(type)
				|| Tag.ITEMS_CHEST_ARMOR.isTagged(type)
				|| Tag.ITEMS_LEG_ARMOR.isTagged(type)
				|| Tag.ITEMS_FOOT_ARMOR.isTagged(type);
	}
	
	public static Rarity getMinRarity(Rarity... rarities)
	{
		Rarity minRarity = Rarity.MAX_RARITY;
		
		for(Rarity rarity : rarities)
		{
			if(rarity == null || rarity == MIN_RARITY)
			{
				return MIN_RARITY;
			}
			
			if(rarity.compareTo(minRarity) < 0)
			{
				minRarity = rarity;
			}
		}
		
		return minRarity;
	}
	
	public Rarity lower()
	{
		return values()[Math.max(0, ordinal() - 1)];
	}
}
