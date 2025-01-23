package com.eul4.wrapper;

import com.eul4.enums.Currency;
import com.eul4.model.town.Town;
import com.sk89q.worldedit.math.BlockVector3;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.IntUnaryOperator;

@Builder
@RequiredArgsConstructor
@Getter
public class Resource
{
	@RequiredArgsConstructor
	@Getter
	public enum Type
	{
		LIKE(Material.LIME_CONCRETE.createBlockData(), Town::addLikes, Currency.LIKE),
		DISLIKE(Material.RED_CONCRETE.createBlockData(), Town::addDislikes, Currency.DISLIKE),
		CROWN(Material.GOLD_BLOCK.createBlockData(), Town::addDislikes, Currency.CROWN), //FIXME fix addOperation
		;
		
		private final BlockData blockData;
		private final BiConsumer<Town, Integer> addOperation;
		private final Currency currency;
	}
	
	private final BlockVector3 relativePosition;
	private final Type type;
	private final IntUnaryOperator subtractOperation;
	private final BooleanSupplier emptyChecker;
	
	public BlockData getBlockData()
	{
		return type.blockData;
	}
	
	public Block getRelative(Block centerBlock)
	{
		return centerBlock.getRelative(relativePosition.x(), relativePosition.y(), relativePosition.z());
	}
	
	public boolean isEmpty()
	{
		return emptyChecker.getAsBoolean();
	}
	
	public void placeRelative(Block centerBlock)
	{
		Block relative = getRelative(centerBlock);
		
		if(isEmpty())
		{
			relative.setType(Material.AIR);
		}
		else
		{
			relative.setBlockData(type.getBlockData());
		}
	}
}
