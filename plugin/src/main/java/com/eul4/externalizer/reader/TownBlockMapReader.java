package com.eul4.externalizer.reader;

import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import org.bukkit.block.Block;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.HashMap;
import java.util.Map;

public class TownBlockMapReader extends ObjectReader<Map<Block, TownBlock>>
{
	private final TownBlockReader townBlockReader;
	
	private final Reader<Map<Block, TownBlock>> reader;
	private final ParameterizedReadable<Map<Block, TownBlock>, Town> parameterizedReadable;
	
	public TownBlockMapReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		this.townBlockReader = new TownBlockReader(in, versions);
		
		if(versions.getTownBlockMapVersion() == 0)
		{
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid TownBlockMap version: " + versions.getTownBlockMapVersion());
		}
	}
	
	private Readable<Map<Block, TownBlock>> parameterizedReadableVersion0(Town town)
	{
		return () ->
		{
			Map<Block, TownBlock> townBlockMap = new HashMap<>();
			
			int size = in.readInt();
			
			for(int i = 0; i < size; i++)
			{
				TownBlock townBlock = townBlockReader.readReference(town);
				townBlockMap.put(townBlock.getBlock(), townBlock);
			}
			
			return townBlockMap;
		};
	}
	
	public Map<Block, TownBlock> readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected Map<Block, TownBlock> readObject(Map<Block, TownBlock> townBlockMap) throws IOException, ClassNotFoundException
	{
		return reader.readObject(townBlockMap);
	}
}
