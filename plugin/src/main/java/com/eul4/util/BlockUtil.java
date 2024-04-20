package com.eul4.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;

public class BlockUtil
{
	public static BlockState newState(Block block, Material newMaterial)
	{
		return newState(block.getState(), newMaterial);
	}
	
	public static BlockState newState(BlockState blockState, Material newMaterial)
	{
		blockState.setType(newMaterial);
		blockState.setBlockData(newMaterial.createBlockData());
		
		return blockState;
	}
	
	public static BlockData getBlockDataUnfilled(BlockData blockData)
	{
		if(blockData instanceof Waterlogged waterlogged)
		{
			waterlogged.setWaterlogged(false);
			return waterlogged;
		}
		else
		{
			return Material.AIR.createBlockData();
		}
	}
	
	public static boolean isWaterBucket(Material material)
	{
		return switch(material)
		{
			case WATER_BUCKET, COD_BUCKET, AXOLOTL_BUCKET, PUFFERFISH_BUCKET, TROPICAL_FISH_BUCKET, SALMON_BUCKET, TADPOLE_BUCKET ->
					true;
			default -> false;
		};
	}
	
	public static BlockData getDataFilledWithWater(BlockData blockData)
	{
		if(blockData instanceof Waterlogged waterlogged)
		{
			waterlogged.setWaterlogged(true);
			return waterlogged;
		}
		else
		{
			return Material.WATER.createBlockData();
		}
	}
	
	public static boolean canBeFilledByWater(Block block)
	{
		return canBeFilledByWater(block.getBlockData());
	}
	
	public static boolean canBeFilledByWater(BlockData blockData)
	{
		return !blockData.getMaterial().isSolid() || blockData instanceof Waterlogged;
	}
	
	public static boolean isFilled(Block block)
	{
		return isFilled(block.getBlockData());
	}
	
	public static boolean isFilled(BlockData blockData)
	{
		return blockData.getMaterial() == Material.LAVA || blockData.getMaterial() == Material.WATER || isWaterlogged(blockData);
	}
	
	public static double getHardnessPlusWaterlogged(BlockState blockState)
	{
		return getHardnessPlusWaterlogged(blockState.getBlockData());
	}
	
	public static double getHardnessPlusWaterlogged(Block block)
	{
		return getHardnessPlusWaterlogged(block.getBlockData());
	}
	
	public static double getHardnessPlusWaterlogged(BlockData blockData)
	{
		double hardness = blockData.getMaterial().getHardness();
		
		if(blockData instanceof Waterlogged waterlogged && waterlogged.isWaterlogged())
		{
			hardness += Material.WATER.getHardness();
		}
		
		return hardness;
	}
	
	public static boolean isWaterlogged(Block block)
	{
		return isWaterlogged(block.getBlockData());
	}
	
	public static boolean isWaterlogged(BlockData blockData)
	{
		return blockData instanceof Waterlogged waterlogged && waterlogged.isWaterlogged();
	}
	
	public static boolean canFillBucket(Block block)
	{
		return canFillBucket(block.getBlockData());
	}
	
	public static boolean canFillBucket(org.bukkit.block.data.BlockData blockData)
	{
		return blockData.getMaterial() == Material.LAVA
				|| blockData.getMaterial() == Material.WATER
				|| blockData.getMaterial() == Material.POWDER_SNOW
				|| blockData instanceof Waterlogged waterlogged && waterlogged.isWaterlogged();
	}
}
