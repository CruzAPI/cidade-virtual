package com.eul4;

import com.eul4.exception.StructureNotForSaleException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StructurePriceChart implements Externalizable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	private final Map<StructureTypeLevel, Price> priceChart = new HashMap<>();
	
	public boolean hasPrice(StructureTypeLevel structureTypeLevel)
	{
		return priceChart.containsKey(structureTypeLevel);
	}
	
	public Price getPrice(StructureType structureType, int level) throws StructureNotForSaleException
	{
		return getPrice(new StructureTypeLevel(structureType, level));
	}
	
	public Price getPrice(StructureTypeLevel structureTypeLevel) throws StructureNotForSaleException
	{
		if(!hasPrice(structureTypeLevel))
		{
			throw new StructureNotForSaleException();
		}
		
		return priceChart.get(structureTypeLevel);
	}
	
	public Price createPriceIfNotExists(StructureType structureType, int level)
	{
		return createPriceIfNotExists(new StructureTypeLevel(structureType, level));
	}
	
	public Price createPriceIfNotExists(StructureTypeLevel structureTypeLevel)
	{
		return priceChart.computeIfAbsent(structureTypeLevel, key -> new Price());
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		long version = in.readLong();
		
		if(version == 1L)
		{
			priceChart.clear();
			
			int size = in.readInt();
			
			for(int i = 0; i < size; i++)
			{
				final StructureTypeLevel structureTypeLevel = new StructureTypeLevel();
				final Price price = new Price();
				
				structureTypeLevel.readExternal(in);
				price.readExternal(in);
				
				priceChart.put(structureTypeLevel, price);
			}
		}
		else
		{
			throw new RuntimeException();
		}
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeLong(serialVersionUID);
		
		out.writeInt(priceChart.size());
		
		for(Map.Entry<StructureTypeLevel, Price> entry : priceChart.entrySet())
		{
			entry.getKey().writeExternal(out);
			entry.getValue().writeExternal(out);
		}
	}
}
