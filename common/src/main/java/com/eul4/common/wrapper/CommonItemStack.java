package com.eul4.common.wrapper;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class CommonItemStack
{
	protected final ItemStack itemStack;
	
	public CommonItemStack(ItemStack itemStack)
	{
		this.itemStack = itemStack;
	}
}
