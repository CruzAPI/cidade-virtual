package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.ObjectInput;

public final class ItemStackReader extends ObjectReader<ItemStack>
{
	private final Reader<ItemStack> reader;
	private final Readable<ItemStack> readable;
	
	public ItemStackReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		if(commonVersions.getItemStackVersion() == 0)
		{
			this.reader = Reader.identity();
			this.readable = this::readableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid ItemStack version: " + commonVersions.getItemStackVersion());
		}
	}
	
	private ItemStack readableVersion0() throws IOException
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
	
	public ItemStack readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
	
	@Override
	protected ItemStack readObject(ItemStack itemStack) throws IOException, ClassNotFoundException
	{
		return reader.readObject(itemStack);
	}
}
