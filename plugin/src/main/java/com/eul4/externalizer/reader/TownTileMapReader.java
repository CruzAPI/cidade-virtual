package com.eul4.externalizer.reader;

import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.TownTile;
import org.bukkit.block.Block;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.HashMap;
import java.util.Map;

public class TownTileMapReader extends ObjectReader<Map<Block, TownTile>>
{
	private final TownTileReader townTileReader;
	
	private final Reader<Map<Block, TownTile>> reader;
	private final ParameterizedReadable<Map<Block, TownTile>, Town> parameterizedReadable;
	
	public TownTileMapReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		this.townTileReader = new TownTileReader(in, versions);
		
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
	
	private Readable<Map<Block, TownTile>> parameterizedReadableVersion0(Town town) throws IOException, ClassNotFoundException
	{
		return () ->
		{
			Map<Block, TownTile> townTileMap = new HashMap<>();
			
			int size = in.readInt();
			
			for(int i = 0; i < size; i++)
			{
				TownTile townTile = townTileReader.readReference(town);
				townTileMap.put(townTile.getBlock(), townTile);
			}
			
			return townTileMap;
		};
	}
	
	public Map<Block, TownTile> readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected Map<Block, TownTile> readObject(Map<Block, TownTile> townTileMap) throws IOException, ClassNotFoundException
	{
		return reader.readObject(townTileMap);
	}
}
