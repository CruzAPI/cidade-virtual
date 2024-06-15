package com.eul4.wrapper;

import com.sk89q.worldedit.math.BlockVector3;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.function.IntUnaryOperator;

@Builder
@RequiredArgsConstructor
@Getter
public class Resource
{
	@RequiredArgsConstructor
	public enum Type
	{
		LIKE(Material.LIME_CONCRETE.createBlockData()),
		DISLIKE(Material.RED_CONCRETE.createBlockData());
		
		private final BlockData blockData;
	}
	
	private final BlockVector3 relativePosition;
	private final Type type;
	private final IntUnaryOperator subtractOperation;
	
	public BlockData getBlockData()
	{
		return type.blockData;
	}
	
	public Block getRelative(Block centerBlock)
	{
		return centerBlock.getRelative(relativePosition.getX(), relativePosition.getY(), relativePosition.getZ());
	}
}
