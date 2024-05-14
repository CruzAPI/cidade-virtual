package com.eul4.common.externalizer.writer;

import com.eul4.common.type.player.Writers;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

@Getter
public class InventoryWriter extends ObjectWriter<ItemStack[]>
{
	public InventoryWriter(Writers writers)
	{
		super(writers, ItemStack[].class);
	}
	
	@Override
	protected void writeObject(ItemStack[] contents) throws IOException
	{
		out.writeInt(contents.length);
		
		for(ItemStack content : contents)
		{
			writers.getWriter(ItemStackWriter.class).writeReference(content);
		}
	}
}
