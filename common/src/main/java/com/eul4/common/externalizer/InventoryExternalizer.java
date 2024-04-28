package com.eul4.common.externalizer;

import com.eul4.common.Common;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@RequiredArgsConstructor
public class InventoryExternalizer
{
	private final Common plugin;

	private static final long VERSION = 0L;

	public ItemStack[] read(ObjectInput in) throws IOException
	{
		return read(in.readLong(), in);
	}

	public ItemStack[] read(long version, ObjectInput in) throws IOException
	{
		if(version == 0L)
		{
			ItemStack[] contents = new ItemStack[in.readInt()];

			for(int i = 0; i < contents.length; i++)
			{
				int length = in.readInt();
				
				if(length == -1)
				{
					contents[i] = null;
				}
				else
				{
					final byte[] bytes = new byte[length];
					in.read(bytes);
					contents[i] = ItemStack.deserializeBytes(bytes);
				}
			}

			return contents;
		}
		else
		{
			throw new RuntimeException();
		}
	}

	public void write(ItemStack[] contents, ObjectOutput out) throws IOException
	{
		out.writeLong(VERSION);

		out.writeInt(contents.length);

		for(ItemStack content : contents)
		{
			if(content == null)
			{
				out.writeInt(-1);
			}
			else
			{
				final byte[] bytes = content.serializeAsBytes();
	
				out.writeInt(bytes.length);
				out.write(bytes);
			}
		}
	}
}
