package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.type.player.PluginObjectType;
import org.bukkit.block.Block;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TownBlockMapReader extends ObjectReader<Map<Block, TownBlock>>
{
	private final Reader<Map<Block, TownBlock>> reader;
	private final ParameterizedReadable<Map<Block, TownBlock>, Town> parameterizedReadable;
	
	public TownBlockMapReader(Readers readers) throws InvalidVersionException
	{
		super(readers);
		
		final ObjectType objectType = PluginObjectType.TOWN_BLOCK_MAP;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
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
				TownBlock townBlock = readers.getReader(TownBlockReader.class).readReference(town);
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
