package com.eul4.util;

import com.eul4.Main;
import com.eul4.enums.Rarity;
import com.eul4.event.BlockDataLoadEvent;
import com.eul4.service.BlockData;
import com.eul4.world.RaidLevel;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OreVeinUtil
{
	private static final Random RANDOM = new Random();
	
	public static void rarifyVein(Main plugin, Block block)
	{
		if(!(plugin.getWorldManager().get(block.getWorld()) instanceof RaidLevel))
		{
			return;
		}
		
		final Set<Block> vein;
		
		if(plugin.getBlockDataFiler().hasBlockData(block) || (vein = getVein(block)).isEmpty())
		{
			return;
		}
		
		Rarity veinRarity = getRandomRarity(block.getType());
		
		for(Block blockVein : vein)
		{
			BlockData blockData = plugin.getBlockDataFiler().loadBlockData(blockVein);
			
			if(blockData == null)
			{
				blockData = BlockData.builder()
						.rarity(veinRarity)
						.build();
				plugin.getBlockDataFiler().setBlockData(blockVein, blockData, BlockDataLoadEvent.Cause.ORE_VEIN);
			}
		}
	}
	
	public static Set<Block> getVein(Block block)
	{
		Set<Material> materials = getMaterials(block.getType());
		
		if(materials.isEmpty())
		{
			return Collections.emptySet();
		}
		
		return getVein(block, materials, new HashSet<>());
	}
	
	private static Set<Block> getVein(Block center, Set<Material> materials, Set<Block> blocks)
	{
		if(!materials.contains(center.getType()) || blocks.contains(center) || blocks.size() > 100)
		{
			return blocks;
		}
		
		blocks.add(center);
		
		int r = 1;
		
		for(int x = -r; x <= r; x++)
		{
			for(int y = -r; y <= r; y++)
			{
				for(int z = -r; z <= r; z++)
				{
					Block relative = center.getRelative(x, y, z);
					getVein(relative, materials, blocks);
				}
			}
		}
		
		return blocks;
	}
	
	private static Set<Material> getMaterials(Material material)
	{
		if(Tag.COAL_ORES.isTagged(material))
		{
			return Tag.COAL_ORES.getValues();
		}
		else if(Tag.COPPER_ORES.isTagged(material))
		{
			return Tag.COPPER_ORES.getValues();
		}
		else if(Tag.IRON_ORES.isTagged(material))
		{
			return Tag.IRON_ORES.getValues();
		}
		else if(Tag.REDSTONE_ORES.isTagged(material))
		{
			return Tag.REDSTONE_ORES.getValues();
		}
		else if(Tag.LAPIS_ORES.isTagged(material))
		{
			return Tag.LAPIS_ORES.getValues();
		}
		else if(Tag.GOLD_ORES.isTagged(material))
		{
			return Tag.GOLD_ORES.getValues();
		}
		else if(Tag.DIAMOND_ORES.isTagged(material))
		{
			return Tag.DIAMOND_ORES.getValues();
		}
		else if(Tag.EMERALD_ORES.isTagged(material))
		{
			return Tag.EMERALD_ORES.getValues();
		}
		else if(hasVein(material))
		{
			return Collections.singleton(material);
		}
		else
		{
			return Collections.emptySet();
		}
	}
	
	private static float getDivisor(Material material)
	{
		if(Tag.COAL_ORES.isTagged(material))
		{
			return 20.0F;
		}
		else if(Tag.COPPER_ORES.isTagged(material))
		{
			return 17.0F;
		}
		else if(Tag.IRON_ORES.isTagged(material))
		{
			return 18.0F;
		}
		else if(Tag.REDSTONE_ORES.isTagged(material))
		{
			return 16.0F;
		}
		else if(Tag.LAPIS_ORES.isTagged(material))
		{
			return 14.0F;
		}
		else if(Tag.GOLD_ORES.isTagged(material))
		{
			return 13.0F;
		}
		else if(Tag.DIAMOND_ORES.isTagged(material))
		{
			return 10.0F;
		}
		else if(Tag.EMERALD_ORES.isTagged(material))
		{
			return 5.0F;
		}
		else if(material == Material.ANCIENT_DEBRIS)
		{
			return 7.0F;
		}
		else if(material == Material.NETHER_QUARTZ_ORE)
		{
			return 14.0F;
		}
		else if(material == Material.NETHER_GOLD_ORE)
		{
			return 12.0F;
		}
		else if(material == Material.GRANITE || material == Material.ANDESITE || material == Material.DIORITE)
		{
			return 40.0F;
		}
		else
		{
			return 100.0F;
		}
	}
	
	private static Rarity getRandomRarity(Material material)
	{
		final float nextFloat = RANDOM.nextFloat();
		final float materialDivisor = getDivisor(material);
		
		Rarity[] rarities = Rarity.values();
		
		for(int i = rarities.length - 1; i >= 0; i--)
		{
			final float base = 1.0F / materialDivisor;
			float chance = (float) Math.pow(base, i);
			
			if(nextFloat < chance)
			{
				return rarities[i];
			}
		}
		
		return Rarity.DEFAULT_RARITY;
	}
	
	private static boolean hasVein(Material material)
	{
		return isOre(material)
				|| material == Material.GRANITE
				|| material == Material.DIORITE
				|| material == Material.ANDESITE
				;
	}
	
	private static boolean isOre(Material material)
	{
		return Tag.COAL_ORES.isTagged(material)
				|| Tag.COPPER_ORES.isTagged(material)
				|| Tag.IRON_ORES.isTagged(material)
				|| Tag.REDSTONE_ORES.isTagged(material)
				|| Tag.LAPIS_ORES.isTagged(material)
				|| Tag.GOLD_ORES.isTagged(material)
				|| Tag.DIAMOND_ORES.isTagged(material)
				|| Tag.EMERALD_ORES.isTagged(material)
				|| material == Material.NETHER_QUARTZ_ORE
				|| material == Material.NETHER_GOLD_ORE
				|| material == Material.ANCIENT_DEBRIS
				;
	}
}
