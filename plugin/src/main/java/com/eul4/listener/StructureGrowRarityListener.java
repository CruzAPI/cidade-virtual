package com.eul4.listener;

import com.eul4.Main;
import com.eul4.enums.Rarity;
import com.eul4.event.block.StructureFertilizeEvent;
import com.eul4.service.BlockData;
import com.eul4.util.RarityUtil;
import com.eul4.wrapper.StabilityFormula;
import lombok.RequiredArgsConstructor;
import org.bukkit.Tag;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureAgeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

@RequiredArgsConstructor
public class StructureGrowRarityListener implements Listener
{
	private final Main plugin;
	
	private final Random random = new Random();
	
	@EventHandler
	public void onStructureFertilize(StructureFertilizeEvent event)
	{
		Block block = event.getBlock();
		ItemStack boneMeal = event.getBoneMeal();
		
		Rarity structureRarity = RarityUtil.getRarity(plugin, block);
		Rarity boneMealRarity = RarityUtil.getRarity(boneMeal);
		int rarityDiff = structureRarity.ordinal() - boneMealRarity.ordinal();
		float chance = 0.45F * (float) Math.pow(0.1D, rarityDiff);
		
		event.setChance(chance);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onStructureAge(StructureAgeEvent event)
	{
		if(event.isFromBonemeal())
		{
			return;
		}
		
		Block block = event.getLocation().getBlock();
		
		Rarity structureRarity = RarityUtil.getRarity(plugin, block);
		Rarity areaRarity = Rarity.COMMON; //TODO rarity areas (legendary islands/rare colonials)
		
		final int rarityDiff = structureRarity.ordinal() - areaRarity.ordinal();
		final int bound = (int) Math.pow(3, rarityDiff);
		
		if(bound > 1)
		{
			final int nextInt = random.nextInt(bound);
			
			if(nextInt != 0)
			{
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMushroomGrow(StructureGrowEvent event)
	{
		TreeType species = event.getSpecies();
		
		if(species != TreeType.BROWN_MUSHROOM && species != TreeType.RED_MUSHROOM)
		{
			return;
		}
		
		Block block = event.getLocation().getBlock();
		Rarity structureRarity = RarityUtil.getRarity(plugin, block);
		
		int blockCount = event.getBlocks().size();
		
		float base = blockCount == 0 ? 1.0F : 1.5F / blockCount;
		float enchantBase = blockCount == 0
				? 1.0F
				: 4.2F / blockCount;
		StabilityFormula stabilityFormula = new StabilityFormula(base, 0.5F, enchantBase, 1.0F / 4.0F);
		
		for(BlockState blockState : event.getBlocks())
		{
			plugin.getBlockDataFiler().setBlockData(blockState.getBlock(), BlockData.builder()
					.rarity(structureRarity)
					.origin(BlockData.Origin.POST_GENERATED)
					.stabilityFormula(stabilityFormula)
					
					.build());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onTreeGrow(StructureGrowEvent event)
	{
		TreeType species = event.getSpecies();
		
		if(species == TreeType.BROWN_MUSHROOM || species == TreeType.RED_MUSHROOM)
		{
			return;
		}
		
		Block block = event.getLocation().getBlock();
		Rarity structureRarity = RarityUtil.getRarity(plugin, block);
		
		int leaveCount = 0;
		
		for(BlockState blockState : event.getBlocks())
		{
			if(Tag.LEAVES.isTagged(blockState.getType()))
			{
				leaveCount++;
			}
		}
		
		boolean jungleTree = species == TreeType.JUNGLE
				|| species == TreeType.JUNGLE_BUSH
				|| species == TreeType.SMALL_JUNGLE
				|| species == TreeType.COCOA_TREE;
		
		float base = leaveCount == 0 ? 1.0F : (jungleTree ? 10.0F : 5.0F) / leaveCount;
		float enchantBase = leaveCount == 0
				? 1.0F
				: (jungleTree ? 9.0F / 4.0F : 9.0F / 2.0F) / leaveCount;
		StabilityFormula leaveStabilityFormula = new StabilityFormula(base, 0.5F, enchantBase, 0.5F);
		
		for(BlockState blockState : event.getBlocks())
		{
			StabilityFormula stabilityFormula = Tag.LEAVES.isTagged(blockState.getType())
					? leaveStabilityFormula
					: StabilityFormula.PLACED;
			
			plugin.getBlockDataFiler().setBlockData(blockState.getBlock(), BlockData.builder()
					.rarity(structureRarity)
					.origin(BlockData.Origin.POST_GENERATED)
					.stabilityFormula(stabilityFormula)
					.build());
		}
	}
}
