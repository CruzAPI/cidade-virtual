package com.eul4.common.externalizer.writer;

import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.ObjectOutput;

public class ItemStackWriter extends ObjectWriter<ItemStack>
{
	public ItemStackWriter(ObjectOutput out)
	{
		super(out);
	}
	
	@Override
	protected void writeObject(ItemStack itemStack) throws IOException
	{
		if(itemStack == null || itemStack.isEmpty())
		{
			out.writeInt(-1);
		}
		else
		{
			final byte[] bytes = itemStack.serializeAsBytes();
			
			out.writeInt(bytes.length);
			out.write(bytes);
		}
	}
}
