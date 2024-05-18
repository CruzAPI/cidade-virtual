package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public final class ItemStackReader extends ObjectReader<ItemStack>
{
	@Getter
	private final Reader<ItemStack> reader;
	private final Readable<ItemStack> readable;
	
	public ItemStackReader(Readers readers) throws InvalidVersionException
	{
		super(readers, ItemStack.class);
		
		final ObjectType objectType = CommonObjectType.ITEM_STACK;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.readable = this::readableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private ItemStack readableVersion0() throws IOException
	{
		int length = in.readInt();
		
		if(length == -1)
		{
			return ItemStack.empty();
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
}
