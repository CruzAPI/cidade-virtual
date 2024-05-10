package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.ObjectInput;

@Getter
public class InventoryReader extends ObjectReader<ItemStack[]>
{
	private final Reader<ItemStack[]> reader;
	private final Readable<ItemStack[]> readable;
	
	private final ItemStackReader itemStackReader;
	
	public InventoryReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		this.itemStackReader = new ItemStackReader(in, commonVersions);
		
		switch(commonVersions.getInventoryVersion())
		{
		case 0:
			this.reader = Reader.identity();
			this.readable = this::readableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid Inventory version: " + commonVersions.getInventoryVersion());
		}
	}
	
	private ItemStack[] readableVersion0() throws IOException, ClassNotFoundException
	{
		ItemStack[] contents = new ItemStack[in.readInt()];
		
		for(int i = 0; i < contents.length; i++)
		{
			contents[i] = itemStackReader.readReference();
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
