package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.Reader;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.ObjectInput;

@Getter
public class InventoryReader extends ObjectReader<ItemStack[]>
{
	private final Reader<ItemStack[]> reader;
	
	private final ItemStackReader itemStackReader;
	
	public InventoryReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		this.itemStackReader = new ItemStackReader(in, commonVersions);
		
		if(commonVersions.getInventoryVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid Inventory version: " + commonVersions.getInventoryVersion());
		}
	}
	
	private ItemStack[] readerVersion0() throws IOException, ClassNotFoundException
	{
		ItemStack[] contents = new ItemStack[in.readInt()];
		
		for(int i = 0; i < contents.length; i++)
		{
			contents[i] = itemStackReader.readReference();
		}
		
		return contents;
	}
	
	@Override
	protected ItemStack[] readObject() throws IOException, ClassNotFoundException
	{
		return reader.readObject();
	}
}
