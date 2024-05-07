package com.eul4.common.externalizer;

import com.eul4.common.Common;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@RequiredArgsConstructor
public class ItemStackExternalizer
{
	private final Common plugin;

	public static final long VERSION = 0L;

	public ItemStack read(ObjectInput in) throws IOException
	{
		return read(in.readLong(), in);
	}

	public ItemStack read(long version, ObjectInput in) throws IOException
	{
		if(version == 0L)
		{
			int length = in.readInt();
			
			if(length == -1)
			{
				return null;
			}
			else
			{
				final byte[] bytes = new byte[length];
				in.read(bytes);
				return ItemStack.deserializeBytes(bytes);
			}
		}
		
		throw new RuntimeException();
	}

	public void writeVersioned(ItemStack itemStack, ObjectOutput out) throws IOException
	{
		out.writeLong(VERSION);
		write(itemStack, out);
	}
	
	public void write(ItemStack itemStack, ObjectOutput out) throws IOException
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
