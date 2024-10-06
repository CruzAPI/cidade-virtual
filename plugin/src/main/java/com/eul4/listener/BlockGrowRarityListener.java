package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.util.ThreadUtil;
import com.eul4.enums.Rarity;
import com.eul4.event.block.BoneMealChanceEvent;
import com.eul4.service.BlockData;
import com.eul4.util.RarityUtil;
import com.eul4.wrapper.StabilityFormula;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BlockGrowRarityListener implements Listener
{
	private final Main plugin;
	
	private final Random random = new Random();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBonemealChance(BoneMealChanceEvent event)
	{
		Block block = event.getBlock();
		Material type = block.getType();
		
		if(Tag.SAPLINGS.isTagged(type) || type == Material.RED_MUSHROOM || type == Material.BROWN_MUSHROOM)
		{
			return;
		}
		
		Rarity blockRarity = RarityUtil.getRarity(plugin, block);
		Rarity boneMealRarity = RarityUtil.getRarity(event.getBoneMeal());
		int rarityDiff = blockRarity.ordinal() - boneMealRarity.ordinal();
		
		if(event.getChance() == 1.0F)
		{
			event.setChance((float) Math.pow(0.1F, rarityDiff));
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBoneMealGrass(BlockFertilizeEvent event)
	{
		if(event.getBlock().getType() != Material.GRASS_BLOCK)
		{
			return;
		}
		
		ItemStack boneMeal = event.getBoneMeal();
		Rarity boneMealRarity = RarityUtil.getRarity(boneMeal);
		
		List<BlockState> blockStates = event.getBlocks();
		Set<Block> blocks = blockStates.stream().map(BlockState::getBlock).collect(Collectors.toSet());
		
		for(BlockState blockState : blockStates)
		{
			Block block = blockState.getBlock();
			Block relativeDown = block.getRelative(BlockFace.DOWN);
			
			while(blocks.contains(relativeDown))
			{
				relativeDown = relativeDown.getRelative(BlockFace.DOWN);
			}
			
			Rarity relativeDownRarity = RarityUtil.getRarity(plugin, relativeDown);
			Rarity rarity = Rarity.getMinRarity(boneMealRarity, relativeDownRarity);
			StabilityFormula stabilityFormula = StabilityFormula.GROWTH_STABILITY_FORMULA_BY_MATERIAL.getOrDefault(blockState.getType(), StabilityFormula.UNSTABLE);
			
			plugin.getBlockDataFiler().setBlockData(block, BlockData.builder()
					.origin(BlockData.Origin.POST_GENERATED)
					.rarity(rarity)
					.stabilityFormula(stabilityFormula)
					.build());
		}
	}
	
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockGrowStemBlock(BlockGrowEvent event)
	{
		Block block = event.getBlock();
		Block source = event.getSource();
		BlockState newState = event.getNewState();
		Material type = newState.getType();
		
		if(type != Material.PUMPKIN && type != Material.MELON)
		{
			return;
		}
		
		Block relativeDown = block.getRelative(BlockFace.DOWN);
		Rarity stemRarity = RarityUtil.getRarity(plugin, source);
		Rarity relativeDownRarity = RarityUtil.getRarity(plugin, relativeDown);
		
		if(relativeDownRarity.compareTo(stemRarity) < 0)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.VERY_HIGH, ignoreCancelled = true)
	public void onBlockGrowNerfTime(BlockGrowEvent event)
	{
		if(event.isFromBonemeal())
		{
			return;
		}
		
		final Rarity sourceRarity = RarityUtil.getRarity(plugin, event.getSource());
		final Rarity areaRarity = Rarity.COMMON; //TODO rarity areas (legendary islands/rare colonials)
		
		final int rarityDiff = sourceRarity.ordinal() - areaRarity.ordinal();
		final int bound = (int) Math.pow(3, rarityDiff);
		
		if(bound > 1 && random.nextInt(bound) != 0)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void delegateBlockSpreadEvent(BlockSpreadEvent event)
	{
		onBlockGrow(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockForm(BlockFormEvent event)
	{
		Material type = event.getNewState().getType();
		StabilityFormula stabilityFormula = StabilityFormula.GROWTH_STABILITY_FORMULA_BY_MATERIAL.get(type);
		
		if(stabilityFormula == null)
		{
			event.setCancelled(true);
			return;
		}
		
		Block block = event.getBlock();
		Block source = event.getSource();
		
		Rarity blockRarity = RarityUtil.getRarity(plugin, block);
		Rarity sourceRarity = RarityUtil.getRarity(plugin, source);
		Rarity minRarity = Rarity.getMinRarity(blockRarity, sourceRarity);
		
		if(!block.isEmpty() && plugin.getBlockDataFiler().hasBlockData(block) && blockRarity != minRarity)
		{
			event.setCancelled(true);
			return;
		}
		
		plugin.getBlockDataFiler().setBlockData(block, BlockData.builder()
				.rarity(minRarity)
				.stabilityFormula(stabilityFormula)
				.origin(BlockData.Origin.POST_GENERATED)
				.build());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockGrow(BlockGrowEvent event)
	{
		Material type = event.getNewState().getType();
		StabilityFormula stabilityFormula = StabilityFormula.GROWTH_STABILITY_FORMULA_BY_MATERIAL.get(type);
		
		if(stabilityFormula == null)
		{
			event.setCancelled(true);
			return;
		}
		
		Block block = event.getBlock();
		Block source = event.getSource();
		
		Rarity sourceRarity = RarityUtil.getRarity(plugin, source);
		
		plugin.getBlockDataFiler().setBlockData(block, BlockData.builder()
				.rarity(sourceRarity)
				.stabilityFormula(stabilityFormula)
				.origin(BlockData.Origin.POST_GENERATED)
				.build());
	}
}
