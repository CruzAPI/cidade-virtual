package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownTile;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.TownTileMap;

import java.io.IOException;

public class TownTileMapReader extends ObjectReader<TownTileMap>
{
	private final Reader<TownTileMap> reader;
	private final ParameterizedReadable<TownTileMap, Town> parameterizedReadable;
	
	public TownTileMapReader(Readers readers) throws InvalidVersionException
	{
		super(readers, TownTileMap.class);
		
		final ObjectType objectType = PluginObjectType.TOWN_TILE_MAP;
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
	
	private Readable<TownTileMap> parameterizedReadableVersion0(Town town)
	{
		return () ->
		{
			TownTileMap townTileMap = new TownTileMap();
			
			int size = in.readInt();
			
			for(int i = 0; i < size; i++)
			{
				TownTile townTile = readers.getReader(TownTileReader.class).readReference(town);
				townTileMap.put(townTile.getBlock(), townTile);
			}
			
			return townTileMap;
		};
	}
	
	public TownTileMap readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected TownTileMap readObject(TownTileMap townTileMap) throws IOException, ClassNotFoundException
	{
		return reader.readObject(townTileMap);
	}
}
