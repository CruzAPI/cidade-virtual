package com.eul4.service;

import com.eul4.common.util.ItemStackUtil;
import com.eul4.common.util.MathUtil;
import com.eul4.enums.Rarity;
import com.eul4.util.RarityUtil;
import com.eul4.wrapper.EnchantType;
import com.eul4.wrapper.StabilityFormula;
import com.eul4.wrapper.StackedEnchantment;
import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@ToString
public class BlockData
{
	public enum Enchant
	{
		FORTUNE(EnchantType.FORTUNE),
		SILK_TOUCH(EnchantType.SILK_TOUCH),
		STABILITY(EnchantType.STABILITY),
		;
		
		private final EnchantType enchantType;
		
		Enchant(EnchantType enchantType)
		{
			this.enchantType = enchantType;
		}
		
		public static Enchant getByEnchantType(EnchantType enchantType)
		{
			for(Enchant enchant : values())
			{
				if(enchant.enchantType == enchantType)
				{
					return enchant;
				}
			}
			
			return null;
		}
		
		public static Enchant getByBukkitEnchantment(Enchantment enchantment)
		{
			return getByEnchantType(EnchantType.fromBukkit(enchantment));
		}
	}
	
	@RequiredArgsConstructor
	@Getter
	public enum Origin
	{
		UNKNOWN((byte) 0),
		CHUNK_GENERATED((byte) 1),
		PLACED((byte) 2),
		POST_GENERATED((byte) 3),
		;
		
		public static final Origin DEFAULT_ORIGIN = CHUNK_GENERATED;
		
		private final byte id;
		
		public static @Nullable Origin getById(byte id)
		{
			for(Origin origin : values())
			{
				if(origin.id == id)
				{
					return origin;
				}
			}
			
			return null;
		}
	}
	
	private boolean hasHardness;
	
	private Rarity rarity;
	private float health;
	private boolean willDrop;
	
	public final byte[] enchantments;
	private Origin origin;
	private StabilityFormula stabilityFormula;
	
	private byte scrapeHealth;
	
	public BlockData()
	{
		this(false, null, null, null, null, null, null, null);
	}
	
	@Builder
	public BlockData
	(
		boolean hasHardness,
		@Nullable Rarity rarity,
		@Nullable Float health,
		@Nullable Boolean willDrop,
		byte @Nullable [] enchantments,
		@Nullable Origin origin,
		@Nullable StabilityFormula stabilityFormula,
		@Nullable Byte scrapeHealth
	)
	{
		this.hasHardness = hasHardness;
		this.rarity = rarity == null ? Rarity.DEFAULT_RARITY : rarity;
		this.health = health == null ? this.rarity.getMaxHealth() : health;
		this.willDrop = willDrop == null || willDrop;
		this.enchantments = adjustEnchantmentByteArray(enchantments);
		this.origin = origin == null ? Origin.DEFAULT_ORIGIN : origin;
		this.stabilityFormula = stabilityFormula == null ? StabilityFormula.STABLE : stabilityFormula;
		this.scrapeHealth = scrapeHealth == null
				? MathUtil.clampToByte(this.rarity.getScalarMultiplier(10))
				: scrapeHealth;
	}
	
	public boolean willDrop(ItemStack tool, Block block)
	{
		Rarity toolRarity = RarityUtil.getRarity(tool);
		boolean preferredTool = block.getBlockData().isPreferredTool(tool);
		
		return preferredTool && (ItemStackUtil.isTool(tool) && toolRarity.compareTo(this.rarity) >= 0 || willDrop);
	}
	
	private Map<Enchantment, Integer> getBlockDataEnchantments()
	{
		Map<Enchantment, Integer> enchantments = new LinkedHashMap<>();
		
		for(int i = 0; i < Enchant.values().length; i++)
		{
			Enchant enchant = Enchant.values()[i];
			enchantments.put(enchant.enchantType.getEnchantment(), (int) this.enchantments[i]);
		}
		
		return enchantments;
	}
	
	public float damageExplosion(float damage, @NotNull Block block)
	{
		final boolean willDrop = block.isPreferredTool(ItemStack.empty());
		damage = block.getType().getBlastResistance() == 0.0F ? Float.MAX_VALUE : damage;
		return damage(damage, willDrop, null);
	}
	
	public float damage(@NotNull ItemStack tool, @NotNull Block block)
	{
		final Rarity toolRarity = RarityUtil.getRarity(tool);
		final float damage;
		
		if(block.getType().getHardness() == 0.0F)
		{
			damage = Float.MAX_VALUE;
		}
		else
		{
			damage = Tag.ITEMS_BREAKS_DECORATED_POTS.isTagged(tool.getType())
					? toolRarity.getMaxHealth()
					: Rarity.DEFAULT_RARITY.getMaxHealth();
		}
		
		final boolean validTool = block.getBlockData().isPreferredTool(tool);
		final Map<Enchantment, Integer> enchantments = Optional.ofNullable(tool.getItemMeta())
				.map(ItemMeta::getEnchants)
				.orElse(Collections.emptyMap());
		
		return damage(damage, validTool, enchantments);
	}
	
	private float damage(float damage, boolean willDrop, @Nullable Map<Enchantment, Integer> enchantments)
	{
		Preconditions.checkArgument(damage >= 0.0F, "Damage must not be negative");
		
		float health = this.health;
		
		this.health = Math.max(0.0F, this.health - damage);
		
		updateWillDrop(willDrop);
		updateStackedEnchantments(enchantments);
		
		return Math.min(health, damage);
	}
	
	private void updateWillDrop(boolean willDrop)
	{
		this.willDrop = this.willDrop && willDrop;
	}
	
	private void updateStackedEnchantments(@Nullable Map<Enchantment, Integer> enchantments)
	{
		if(enchantments == null)
		{
			removeEnchantments();
			return;
		}
		
		for(Enchant enchant : Enchant.values())
		{
			updateEnchantment(enchant.enchantType, enchantments.getOrDefault(enchant.enchantType.getEnchantment(), 0));
		}
	}
	
	private void updateEnchantment(EnchantType enchantType, int level)
	{
		Enchant enchant = Enchant.getByEnchantType(enchantType);
		
		if(enchant == null)
		{
			return;
		}
		
		final int index = enchant.ordinal();
		enchantments[index] = (byte) Math.min(enchantments[index], level);
	}
	
	public ItemStack getFakeTool(ItemStack realTool)
	{
		Rarity toolRarity = RarityUtil.getRarity(realTool);
		
		if(toolRarity.compareTo(rarity) >= 0)
		{
			return realTool;
		}
		
		if(!willDrop)
		{
			return null;
		}
		
		ItemStack fakeTool = ItemStack.of(realTool.getType());
		ItemMeta meta = fakeTool.getItemMeta();
		
		if(meta == null)
		{
			return fakeTool;
		}
		
		for(Map.Entry<Enchantment, Integer> entry : getBlockDataEnchantments().entrySet())
		{
			meta.addEnchant(entry.getKey(), entry.getValue(), true);
		}
		
		fakeTool.setItemMeta(meta);
		
		return fakeTool;
	}
	
	public Map<EnchantType, StackedEnchantment> getStackedEnchantments(ItemStack tool)
	{
		return getStaticStackedEnchantments(tool, Collections.emptySet());
	}
	
	public Map<EnchantType, StackedEnchantment> getStackedEnchantments(ItemStack tool, Set<EnchantType> excludedEnchantTypes)
	{
		Rarity toolRarity = RarityUtil.getRarity(tool);
		
		if(toolRarity.compareTo(rarity) >= 0)
		{
			return getStaticStackedEnchantments(tool, excludedEnchantTypes);
		}
		
		ItemMeta meta = tool.getItemMeta();
		
		if(meta == null)
		{
			return Collections.emptyMap();
		}
		
		Map<EnchantType, StackedEnchantment> stackedEnchantments = new LinkedHashMap<>();
		
		for(Map.Entry<Enchantment, Integer> enchantments : meta.getEnchants().entrySet())
		{
			Enchant enchant = Enchant.getByBukkitEnchantment(enchantments.getKey());
			
			if(enchant == null || excludedEnchantTypes.contains(enchant.enchantType))
			{
				continue;
			}
			
			byte toolEnchantLevel = enchantments.getValue().byteValue();
			byte level = (byte) Math.min(toolEnchantLevel, this.enchantments[enchant.ordinal()]);
			boolean downgraded = level < toolEnchantLevel;
			
			stackedEnchantments.put(enchant.enchantType, new StackedEnchantment(enchant.enchantType, level, downgraded));
		}
		
		return stackedEnchantments;
	}
	
	private void removeEnchantments()
	{
		Arrays.fill(enchantments, (byte) 0);
	}
	
	public static Map<EnchantType, StackedEnchantment> getStaticStackedEnchantments(ItemStack tool)
	{
		return getStaticStackedEnchantments(tool, Collections.emptySet());
	}
	
	public static Map<EnchantType, StackedEnchantment> getStaticStackedEnchantments(ItemStack tool, Set<EnchantType> excludedEnchantTypes)
	{
		ItemMeta meta = tool.getItemMeta();
		
		if(meta == null)
		{
			return Collections.emptyMap();
		}
		
		Map<EnchantType, StackedEnchantment> stackedEnchantments = new LinkedHashMap<>();
		
		for(Map.Entry<Enchantment, Integer> enchantments : meta.getEnchants().entrySet())
		{
			BlockData.Enchant enchant = BlockData.Enchant.getByBukkitEnchantment(enchantments.getKey());
			
			if(enchant == null || excludedEnchantTypes.contains(enchant.enchantType))
			{
				continue;
			}
			
			byte level = enchantments.getValue().byteValue();
			
			stackedEnchantments.put(enchant.enchantType, new StackedEnchantment(enchant.enchantType, level, false));
		}
		
		return stackedEnchantments;
	}
	
	public boolean isDead()
	{
		return health <= 0.0F;
	}
	
	public void setRarity(@NotNull Rarity rarity)
	{
		this.rarity = Objects.requireNonNull(rarity);
	}
	
	public void setRarityAndResetHealth(@NotNull Rarity rarity)
	{
		setRarity(rarity);
		resetHealth();
	}
	
	public void resetHealth()
	{
		this.health = rarity.getMaxHealth();
	}
	
	private static byte[] createDefaultEnchantmentsByteArray()
	{
		byte[] enchantments = new byte[Enchant.values().length];
		Arrays.fill(enchantments, Byte.MAX_VALUE);
		return enchantments;
	}
	
	public boolean willDrop()
	{
		return willDrop;
	}
	
	public boolean hasHardness()
	{
		return hasHardness;
	}
	
	public void hasHardness(boolean hasHardness)
	{
		this.hasHardness = hasHardness;
	}
	
	public Rarity getRarity()
	{
		return rarity;
	}
	
	public float getHealth()
	{
		return health;
	}
	
	public Origin getOrigin()
	{
		return origin;
	}
	
	public StabilityFormula getStabilityFormula()
	{
		return stabilityFormula;
	}
	
	private static byte[] adjustEnchantmentByteArray(byte @Nullable [] actual)
	{
		if(actual == null)
		{
			return createDefaultEnchantmentsByteArray();
		}
		
		if(actual.length == Enchant.values().length)
		{
			return actual;
		}
		
		byte[] fixed = new byte[Enchant.values().length];
		System.arraycopy(actual, 0, fixed, 0, actual.length);
		
		for(int i = actual.length; i < fixed.length; i++)
		{
			fixed[i] = Byte.MAX_VALUE;
		}
		
		return fixed;
	}
	
	public byte getScrapeHealth()
	{
		return scrapeHealth;
	}
	
	public void scrape(byte points)
	{
		this.scrapeHealth = MathUtil.addSaturated(this.scrapeHealth, (byte) -points);
	}
	
	public boolean isScraped()
	{
		return scrapeHealth <= 0;
	}
	
	public void resetScrapeHealth()
	{
		this.scrapeHealth = MathUtil.clampToByte(this.rarity.getScalarMultiplier(10));
	}
	
	public void setOrigin(Origin origin)
	{
		this.origin = origin;
	}
	
	public void setStabilityFormula(StabilityFormula stabilityFormula)
	{
		this.stabilityFormula = stabilityFormula;
	}
}
