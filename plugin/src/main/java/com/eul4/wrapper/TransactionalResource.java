package com.eul4.wrapper;

import com.eul4.calculator.Calculator;
import com.eul4.common.i18n.TranslatableMessage;
import com.eul4.enums.Currency;
import com.eul4.function.TransactionCreator;
import com.eul4.i18n.PluginMessage;
import com.sk89q.worldedit.math.BlockVector3;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import java.util.function.BooleanSupplier;
import java.util.function.Function;

@Builder
@RequiredArgsConstructor
@Getter
public class TransactionalResource<N extends Number & Comparable<N>>
{
	@RequiredArgsConstructor
	@Getter
	public enum Type
	{
		CROWN(Material.GOLD_BLOCK.createBlockData(), Currency.CROWN, PluginMessage.STEALING_CROWNS_DEPOSIT_FULL),
		;
		
		private final BlockData blockData;
		private final Currency currency;
		private final TranslatableMessage overCapacityMessage;
	}
	
	private final BlockVector3 relativePosition;
	private final Type type;
	private final TransactionCreator<N> transactionCreator;
	private final Calculator<N> calculator;
	private final BooleanSupplier emptyChecker;
	private final Function<ItemStack, N> amountToStealFunction;
	
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
	
	public Calculator<N> getCalculator()
	{
		return calculator;
	}
	
	public N getAmountToSteal(ItemStack tool)
	{
		return amountToStealFunction.apply(tool);
	}
}
