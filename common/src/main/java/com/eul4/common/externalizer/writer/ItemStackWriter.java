package com.eul4.common.externalizer.writer;

import com.eul4.common.type.player.Writers;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class ItemStackWriter extends ObjectWriter<ItemStack>
{
	public ItemStackWriter(Writers writers)
	{
		super(writers, ItemStack.class);
	}
	
	@Override
	protected void writeObject(ItemStack itemStack) throws IOException
	{
		if(itemStack.isEmpty())
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
