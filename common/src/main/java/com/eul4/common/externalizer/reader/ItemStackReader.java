package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.Reader;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.ObjectInput;

public final class ItemStackReader extends ObjectReader<ItemStack>
{
	private final Reader<ItemStack> reader;
	
	public ItemStackReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		if(commonVersions.getItemStackVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid ItemStack version: " + commonVersions.getItemStackVersion());
		}
	}
	
	private ItemStack readerVersion0() throws IOException
	{
		int length = in.readInt();
		
		if(length == -1)
		{
			return null;
		}
		else
		{
			final byte[] bytes = new byte[length];
			in.readFully(bytes);
			return ItemStack.deserializeBytes(bytes);
		}
	}
	
	@Override
	protected ItemStack readObject() throws IOException, ClassNotFoundException
	{
		return reader.readObject();
	}
}
