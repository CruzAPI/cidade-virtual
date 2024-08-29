package com.eul4.service;

import com.eul4.common.util.ItemStackUtil;
import com.eul4.enums.Rarity;
import com.eul4.util.RarityUtil;
import com.eul4.wrapper.StackedEnchantment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
@Setter
@ToString
public class BlockData
{
	@RequiredArgsConstructor
	public enum Enchant
	{
		FORTUNE(Enchantment.FORTUNE),
		SILK_TOUCH(Enchantment.SILK_TOUCH),
//		STABILITY(null),
		;
		
		private final Enchantment enchantment;
		
		public static Enchant getByBukkitEnchantment(Enchantment enchantment)
		{
			for(Enchant enchant : values())
			{
				if(enchantment.equals(enchant.enchantment))
				{
					return enchant;
				}
			}
			
			return null;
		}
	}
	
	@Accessors(fluent = true)
	private boolean hasHardness;
	
	private Rarity rarity;
	private float health;
	@Accessors(fluent = true)
	private boolean willDrop;
	
	private byte[] enchantments;
	
	public BlockData()
	{
		this(false);
	}
	
	public BlockData(boolean hasHardness)
	{
		this(hasHardness, Rarity.DEFAULT_RARITY, Rarity.DEFAULT_RARITY.getMaxHealth(), true);
	}
	
	public BlockData(Rarity rarity)
	{
		this(true, rarity, rarity.getMaxHealth(), true);
	}
	
	public BlockData(boolean hasHardness, Rarity rarity, float health, boolean willDrop)
	{
		this(hasHardness, rarity, health, willDrop, new byte[Enchant.values().length]);
		Arrays.fill(enchantments, Byte.MAX_VALUE);
	}
	
	public BlockData(boolean hasHardness, Rarity rarity, float health, boolean willDrop, byte[] enchantments)
	{
		this.hasHardness = hasHardness;
		this.rarity = rarity;
		this.health = health;
		this.willDrop = willDrop;
		this.enchantments = enchantments;
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
			enchantments.put(enchant.enchantment, (int) this.enchantments[i]);
		}
		
		return enchantments;
	}
	
	public void damageExplosion(float damage, @NotNull Block block)
	{
		final boolean willDrop = block.isPreferredTool(ItemStack.empty());
		damage = block.getType().getBlastResistance() == 0.0F ? Float.MAX_VALUE : damage;
		damage(damage, willDrop, null);
	}
	
	public void damage(@NotNull ItemStack tool, @NotNull Block block)
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
		
		damage(damage, validTool, enchantments);
	}
	
	private void damage(float damage, boolean willDrop, @Nullable Map<Enchantment, Integer> enchantments)
	{
		health = Math.max(0.0F, health - damage);
		
		updateWillDrop(willDrop);
		updateStackedEnchantments(enchantments);
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
			updateEnchantment(enchant.enchantment, enchantments.getOrDefault(enchant.enchantment, 0));
		}
	}
	
	private void updateEnchantment(Enchantment enchantment, int level)
	{
		Enchant enchant = Enchant.getByBukkitEnchantment(enchantment);
		
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
		
		for(Map.Entry<Enchantment, Integer> enchantments : getBlockDataEnchantments().entrySet())
		{
			meta.addEnchant(enchantments.getKey(), enchantments.getValue(), true);
		}
		
		fakeTool.setItemMeta(meta);
		
		return fakeTool;
	}
	
	public Set<StackedEnchantment> getStackedEnchantments(ItemStack tool)
	{
		Rarity toolRarity = RarityUtil.getRarity(tool);
		
		if(toolRarity.compareTo(rarity) >= 0)
		{
			return getStaticStackedEnchantments(tool);
		}
		
		ItemMeta meta = tool.getItemMeta();
		
		if(meta == null)
		{
			return Collections.emptySet();
		}
		
		Set<StackedEnchantment> stackedEnchantments = new LinkedHashSet<>();
		
		for(Map.Entry<Enchantment, Integer> enchantments : meta.getEnchants().entrySet())
		{
			Enchant enchant = Enchant.getByBukkitEnchantment(enchantments.getKey());
			
			if(enchant == null)
			{
				continue;
			}
			
			byte toolEnchantLevel = enchantments.getValue().byteValue();
			byte level = (byte) Math.min(toolEnchantLevel, this.enchantments[enchant.ordinal()]);
			boolean downgraded = level < toolEnchantLevel;
			
			stackedEnchantments.add(new StackedEnchantment(enchant.enchantment, level, downgraded));
		}
		
		return stackedEnchantments;
	}
	
	private void removeEnchantments()
	{
		Arrays.fill(enchantments, (byte) 0);
	}
	
	public static Set<StackedEnchantment> getStaticStackedEnchantments(ItemStack tool)
	{
		ItemMeta meta = tool.getItemMeta();
		
		if(meta == null)
		{
			return Collections.emptySet();
		}
		
		Set<StackedEnchantment> stackedEnchantments = new LinkedHashSet<>();
		
		for(Map.Entry<Enchantment, Integer> enchantments : meta.getEnchants().entrySet())
		{
			BlockData.Enchant enchant = BlockData.Enchant.getByBukkitEnchantment(enchantments.getKey());
			
			if(enchant == null)
			{
				continue;
			}
			
			byte level = enchantments.getValue().byteValue();
			
			stackedEnchantments.add(new StackedEnchantment(enchant.enchantment, level, false));
		}
		
		return stackedEnchantments;
	}
}
