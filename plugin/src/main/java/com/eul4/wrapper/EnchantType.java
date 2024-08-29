package com.eul4.wrapper;

import com.eul4.common.util.MathUtil;
import com.eul4.enums.PluginNamespacedKey;
import com.eul4.enums.Rarity;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import lombok.Getter;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Tag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public enum EnchantType
{
	PROTECTION
	(
		"protection",
		Definition.def(4, Cost.cost(1, 11)),
		Definition.def(7, Cost.cost(1, 16)),
		Definition.def(10, Cost.cost(1, 32))
	),
	FIRE_PROTECTION
	(
		"fire_protection",
		Definition.def(4, Cost.cost(10, 8)),
		Definition.def(7, Cost.cost(10, 13)),
		Definition.def(10, Cost.cost(10, 29))
	),
	FEATHER_FALLING
	(
		"feather_falling",
		Definition.def(4, Cost.cost(5, 6)),
		Definition.def(7, Cost.cost(5, 11)),
		Definition.def(10, Cost.cost(5, 27))
	),
	BLAST_PROTECTION
	(
		"blast_protection",
		Definition.def(4, Cost.cost(5, 8)),
		Definition.def(7, Cost.cost(5, 13)),
		Definition.def(10, Cost.cost(5, 29))
	),
	PROJECTILE_PROTECTION
	(
		"projectile_protection",
		Definition.def(4, Cost.cost(3, 6)),
		Definition.def(7, Cost.cost(3, 11)),
		Definition.def(10, Cost.cost(3, 27))
	),
	RESPIRATION
	(
		"respiration",
		Definition.def(3, Cost.costInfinityRange(10, 10)),
		Definition.def(6, Cost.costInfinityRange(10, 15)),
		Definition.def(10, Cost.costInfinityRange(10, 27))
	),
	AQUA_AFFINITY
	(
		"aqua_affinity",
		Definition.def(1, Cost.cost(1, 40)),
		Definition.def(1, Cost.cost(1, 105)),
		Definition.def(1, Cost.cost(1, 300))
	),
	THORNS
	(
		"thorns",
		Definition.def(3, Cost.costInfinityRange(10, 20)),
		Definition.def(6, Cost.costInfinityRange(10, 22)),
		Definition.def(10, Cost.costInfinityRange(11, 34)) //TODO testar se o Peitoral de ouro encanta com thorns 10 e o de diamante não!
	),
	DEPTH_STRIDER
	(
		"depth_strider",
		Definition.def(1, Cost.cost(30, 15)),
		Definition.def(2, Cost.cost(30, 41)),
		Definition.def(3, Cost.cost(30, 96))
	),
	FROST_WALKER
	(
		"frost_walker",
		Definition.def(1, Cost.cost(20, 15)),
		Definition.def(2, Cost.cost(20, 40)),
		Definition.def(3, Cost.cost(20, 90))
	),
	BINDING_CURSE
	(
		"binding_curse",
		Definition.def(1, Cost.costInfinityRange(25, 25)),
		Definition.def(1, Cost.costInfinityRange(25, 25)),
		Definition.def(1, Cost.costInfinityRange(25, 25))
	),
	SOUL_SPEED
	(
		"soul_speed",
		Definition.def(3, Cost.cost(10, 10, 15)),
		Definition.def(4, Cost.cost(10, 25, 25)),
		Definition.def(5, Cost.cost(10, 60, 50))
	),
	SWIFT_SNEAK
	(
		"swift_sneak",
		Definition.def(3, Cost.costInfinityRange(25, 50)),
		Definition.def(4, Cost.costInfinityRange(25, 42)),
		Definition.def(5, Cost.costInfinityRange(25, 70))
	),
	SHARPNESS
	(
		"sharpness",
		Definition.def(5, Cost.costInfinityRange(1, 11)),
		Definition.def(7, Cost.costInfinityRange(1, 18)),
		Definition.def(10, Cost.costInfinityRange(1, 34))
	),
	SMITE
	(
		"smite",
		Definition.def(5, Cost.costInfinityRange(5, 8)),
		Definition.def(7, Cost.costInfinityRange(5, 15)),
		Definition.def(10, Cost.costInfinityRange(5, 31))
	),
	BANE_OF_ARTHROPODS
	(
		"bane_of_arthropods",
		Definition.def(5, Cost.costInfinityRange(5, 8)),
		Definition.def(7, Cost.costInfinityRange(5, 15)),
		Definition.def(10, Cost.costInfinityRange(5, 31))
	),
	KNOCKBACK
	(
		"knockback",
		Definition.def(1, Cost.costInfinityRange(5, 20)),
		Definition.def(2, Cost.costInfinityRange(15, 60)),
		Definition.def(3, Cost.costInfinityRange(25, 100))
	),
	FIRE_ASPECT
	(
		"fire_aspect",
		Definition.def(1, Cost.costInfinityRange(10, 20)),
		Definition.def(2, Cost.costInfinityRange(30, 60)),
		Definition.def(3, Cost.costInfinityRange(50, 110))
	),
	LOOTING
	(
		"looting",
		Definition.def(3, Cost.costInfinityRange(15, 9)),
		Definition.def(4, Cost.costInfinityRange(15, 26)),
		Definition.def(5, Cost.costInfinityRange(15, 66))
	),
	SWEEPING_EDGE
	(
		"sweeping_edge",
		Definition.def(3, Cost.cost(5, 9, 15)),
		Definition.def(4, Cost.cost(15, 20, 25)),
		Definition.def(5, Cost.cost(30, 50, 50))
	),
	EFFICIENCY
	(
		"efficiency",
		Definition.def(5, Cost.costInfinityRange(1, 10)),
		Definition.def(7, Cost.costInfinityRange(1, 18)), //TODO testar se picareta de diamante/ouro/ferro pega EFF 7
		Definition.def(10, Cost.costInfinityRange(1, 35)) //TODO testar se a picareta de diamante/ouro/ferro pega EFF 10
	),
	SILK_TOUCH
	(
		"silk_touch",
		Definition.def(1, Cost.costInfinityRange(15, 15)),
		Definition.def(1, Cost.costInfinityRange(45, 45)),
		Definition.def(1, Cost.costInfinityRange(135, 135))
	),
	UNBREAKING
	(
		"unbreaking",
		Definition.def(3, Cost.costInfinityRange(5, 15)),
		Definition.def(4, Cost.costInfinityRange(15, 25)),
		Definition.def(5, Cost.costInfinityRange(45, 55))
	),
	FORTUNE
	(
		"fortune",
		Definition.def(3, Cost.costInfinityRange(15, 9)),
		Definition.def(4, Cost.costInfinityRange(15, 25)),
		Definition.def(5, Cost.costInfinityRange(15, 60))
	),
	POWER
	(
		"power",
		Definition.def(5, Cost.costInfinityRange(1, 10)),
		Definition.def(7, Cost.costInfinityRange(1, 18)),
		Definition.def(10, Cost.costInfinityRange(1, 36))
	),
	PUNCH
	(
		"punch",
		Definition.def(1, Cost.costInfinityRange(12, 12)),
		Definition.def(2, Cost.costInfinityRange(12, 80)),
		Definition.def(3, Cost.costInfinityRange(12, 135))
	),
	FLAME
	(
		"flame",
		Definition.def(1, Cost.costInfinityRange(20, 20)),
		Definition.def(1, Cost.costInfinityRange(60, 60)),
		Definition.def(1, Cost.costInfinityRange(180, 180))
	),
	INFINITY
	(
		"infinity",
		Definition.def(1, Cost.costInfinityRange(20, 20)),
		Definition.def(1, Cost.costInfinityRange(60, 60)),
		Definition.def(1, Cost.costInfinityRange(180, 180))
	),
	LUCK_OF_THE_SEA
	(
		"luck_of_the_sea",
		Definition.def(3, Cost.costInfinityRange(15, 9)),
		Definition.def(4, Cost.costInfinityRange(15, 27)),
		Definition.def(5, Cost.costInfinityRange(15, 65))
	),
	LURE
	(
		"lure",
		Definition.def(3, Cost.costInfinityRange(15, 9)),
		Definition.def(4, Cost.costInfinityRange(15, 27)),
		Definition.def(5, Cost.costInfinityRange(15, 65))
	),
	LOYALTY
	(
		"loyalty",
		Definition.def(1, Cost.costInfinityRange(12, 12)),
		Definition.def(2, Cost.costInfinityRange(12, 65)),
		Definition.def(3, Cost.costInfinityRange(12, 109))
	),
	IMPALING
	(
		"impaling",
		Definition.def(5, Cost.costInfinityRange(1, 8)),
		Definition.def(7, Cost.costInfinityRange(1, 16)),
		Definition.def(10, Cost.costInfinityRange(1, 33))
	),
	RIPTIDE
	(
		"riptide",
		Definition.def(3, Cost.costInfinityRange(12, 20)),
		Definition.def(4, Cost.costInfinityRange(12, 27)),
		Definition.def(5, Cost.costInfinityRange(12, 67))
	),
	CHANNELING
	(
		"channeling",
		Definition.def(1, Cost.costInfinityRange(25, 25)),
		Definition.def(1, Cost.costInfinityRange(75, 75)),
		Definition.def(1, Cost.costInfinityRange(225, 225))
	),
	MULTISHOT
	(
		"multishot",
		Definition.def(1, Cost.costInfinityRange(20, 20)),
		Definition.def(1, Cost.costInfinityRange(60, 60)),
		Definition.def(1, Cost.costInfinityRange(180, 180))
	),
	PIERCING
	(
		"piercing",
		Definition.def(4, Cost.costInfinityRange(1, 10)),
		Definition.def(7, Cost.costInfinityRange(1, 18)),
		Definition.def(10, Cost.costInfinityRange(1, 35))
	),
	QUICK_CHARGE
	(
		"quick_charge",
		Definition.def(3, Cost.costInfinityRange(12, 20)),
		Definition.def(4, Cost.costInfinityRange(12, 42)),
		Definition.def(5, Cost.costInfinityRange(12, 91))
	),
	DENSITY
	(
		"density",
		Definition.def(5, Cost.costInfinityRange(5, 8)),
		Definition.def(7, Cost.costInfinityRange(5, 15)),
		Definition.def(10, Cost.costInfinityRange(5, 31))
	),
	BREACH
	(
		"breach",
		Definition.def(4, Cost.costInfinityRange(15, 9)),
		Definition.def(7, Cost.costInfinityRange(15, 15)),
		Definition.def(10, Cost.costInfinityRange(15, 33))
	),
	WIND_BURST
	(
		"wind_burst",
		Definition.def(3, Cost.costInfinityRange(15, 9)),
		Definition.def(4, Cost.costInfinityRange(15, 27)),
		Definition.def(5, Cost.costInfinityRange(15, 62))
	),
	MENDING
	(
		"mending",
		Definition.def(1, Cost.costInfinityRange(25, 25)),
		Definition.def(1, Cost.costInfinityRange(75, 75)),
		Definition.def(1, Cost.costInfinityRange(225, 225))
	),
	VANISHING_CURSE
	(
		"vanishing_curse",
		Definition.def(1, Cost.costInfinityRange(25, 25)),
		Definition.def(1, Cost.costInfinityRange(75, 75)),
		Definition.def(1, Cost.costInfinityRange(225, 225))
	),
	
	STABILITY
	(
		PluginNamespacedKey.ENCHANTMENT_STABILITY,
		Definition.def(4, Cost.costInfinityRange(1, 10 + 3)),
		Definition.def(7, Cost.costInfinityRange(1, 18 + 1)),
		Definition.def(10, Cost.costInfinityRange(1, 35 + 1))
	)
	{
		@Override
		public boolean conflictsWith(EnchantType other)
		{
			return other == MENDING;
		}
		
		@Override
		public boolean canEnchantItem(ItemStack itemStack)
		{
			return Tag.ITEMS_SWORDS.isTagged(itemStack.getType())
					|| Tag.ITEMS_AXES.isTagged(itemStack.getType())
					|| Tag.ITEMS_PICKAXES.isTagged(itemStack.getType())
					|| Tag.ITEMS_SHOVELS.isTagged(itemStack.getType())
					|| Tag.ITEMS_HOES.isTagged(itemStack.getType())
					|| itemStack.getType() == Material.BOW
					|| itemStack.getType() == Material.MACE
					|| itemStack.getType() == Material.TRIDENT
					|| itemStack.getType() == Material.CROSSBOW
					;
		}
	},
	;
	
	private final NamespacedKey key;
	private final Enchantment enchantment;
	private final Definition commonDefinition;
	private final Definition rareDefinition;
	private final Definition legendaryDefinition;
	
	EnchantType
	(
		String key,
		Definition commonDefinition,
		Definition rareDefinition,
		Definition legendaryDefinition
	)
	{
		this
		(
			NamespacedKey.minecraft(key),
			commonDefinition,
			rareDefinition,
			legendaryDefinition
		);
	}
	
	EnchantType
	(
		NamespacedKey namespacedKey,
		Definition commonDefinition,
		Definition rareDefinition,
		Definition legendaryDefinition
	)
	{
		this.key = namespacedKey;
		this.enchantment = getCustomEnchantment(namespacedKey);
		this.commonDefinition = commonDefinition;
		this.rareDefinition = rareDefinition;
		this.legendaryDefinition = legendaryDefinition;
	}
	
	public static EnchantType asMirror(EnchantmentInstance enchantmentInstance)
	{
		return asMirror(enchantmentInstance.enchantment);
	}
	
	public static EnchantType asMirror(Holder<net.minecraft.world.item.enchantment.Enchantment> holder)
	{
		return EnchantType.valueOf(holder.getRegisteredName().replaceAll(".*:", "").toUpperCase());
	}
	
	public boolean conflictsWith(EnchantType other)
	{
		return enchantment.conflictsWith(other.enchantment);
	}
	
	public boolean canEnchantItem(ItemStack itemStack)
	{
		return enchantment.canEnchantItem(itemStack);
	}
	
	public int getMaxLevel(Rarity rarity)
	{
		return getDefinition(rarity).maxLevel;
	}
	
	public int getMinCost(int level, Rarity rarity)
	{
		return getDefinition(rarity).cost.calculateMin(level);
	}
	
	public int getMaxCost(int level, Rarity rarity)
	{
		return getDefinition(rarity).cost.calculateMax(level);
	}
	
	public Definition getDefinition(Rarity rarity)
	{
		return switch(rarity)
		{
			case COMMON -> commonDefinition;
			case RARE -> rareDefinition;
			case LEGENDARY -> legendaryDefinition;
		};
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
	
	private static class Definition
	{
		private final int maxLevel;
		private final Cost cost;
		
		private Definition(int maxLevel, Cost cost)
		{
			this.maxLevel = maxLevel;
			this.cost = cost;
		}
		
		private static Definition def(int maxLevel, Cost cost)
		{
			return new Definition(maxLevel, cost);
		}
	}
	
	private static class Cost
	{
		private final int base;
		private final int perLevelAboveFirst;
		private final int range;
		
		private Cost(int base, int perLevelAboveFirst)
		{
			this(base, perLevelAboveFirst, perLevelAboveFirst);
		}
		
		private Cost(int base, int perLevelAboveFirst, int range)
		{
			this.base = base;
			this.perLevelAboveFirst = perLevelAboveFirst;
			this.range = range;
		}
		
		private static Cost cost(int base, int perLevelAboveFirst)
		{
			return new Cost(base, perLevelAboveFirst);
		}
		
		private static Cost costInfinityRange(int base, int perLevelAboveFirst)
		{
			return new Cost(base, perLevelAboveFirst, Integer.MAX_VALUE);
		}
		
		private static Cost cost(int base, int perLevelAboveFirst, int range)
		{
			return new Cost(base, perLevelAboveFirst, range);
		}
		
		private int calculateMin(int level)
		{
			return this.base + this.perLevelAboveFirst * (level - 1);
		}
		
		private int calculateMax(int level)
		{
			return MathUtil.addSaturated(calculateMin(level), range);
		}
	}
}
