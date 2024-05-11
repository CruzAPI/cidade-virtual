package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

@Getter
public class InventoryReader extends ObjectReader<ItemStack[]>
{
	private final Reader<ItemStack[]> reader;
	private final Readable<ItemStack[]> readable;
	
	public InventoryReader(Readers readers) throws InvalidVersionException
	{
		super(readers);
		
		final ObjectType objectType = CommonObjectType.INVENTORY;
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
	
	private ItemStack[] readableVersion0() throws IOException, ClassNotFoundException
	{
		ItemStack[] contents = new ItemStack[in.readInt()];
		
		for(int i = 0; i < contents.length; i++)
		{
			contents[i] = readers.getReader(ItemStackReader.class).readReference();
		}
		
		return contents;
	}
	
	public ItemStack[] readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
	
	@Override
	protected ItemStack[] readObject(ItemStack[] contents) throws IOException, ClassNotFoundException
	{
		return reader.readObject(contents);
	}
}
