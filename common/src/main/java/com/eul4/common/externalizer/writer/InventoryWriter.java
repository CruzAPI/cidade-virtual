package com.eul4.common.externalizer.writer;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.ObjectOutput;

@Getter
public class InventoryWriter extends ObjectWriter<ItemStack[]>
{
	private final ItemStackWriter itemStackWriter;
	
	public InventoryWriter(ObjectOutput out)
	{
		super(out);
		
		this.itemStackWriter = new ItemStackWriter(out);
	}
	
	@Override
	protected void writeObject(ItemStack[] contents) throws IOException
	{
		out.writeInt(contents.length);
		
		for(ItemStack content : contents)
		{
			itemStackWriter.writeReference(content);
		}
	}
}
