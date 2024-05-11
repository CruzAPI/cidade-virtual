package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.BlockReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.CraftTownTile;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownTile;
import com.eul4.type.player.PluginObjectType;

import java.io.IOException;

public class TownTileReader extends ObjectReader<TownTile>
{
	private final Reader<TownTile> reader;
	private final ParameterizedReadable<TownTile, Town> parameterizedReadable;
	
	public TownTileReader(Readers readers) throws InvalidVersionException
	{
		super(readers);
		
		final ObjectType objectType = PluginObjectType.TOWN_TILE;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private Readable<TownTile> parameterizedReadableVersion0(Town town)
	{
		return () -> new CraftTownTile(town,
				readers.getReader(BlockReader.class).readReference(town.getPlugin()),
				in.readBoolean());
	}
	
	private TownTile readerVersion0(TownTile townTile)
	{
		//TODO: read TownTile fields...
		
		return townTile;
	}
	
	public TownTile readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected TownTile readObject(TownTile townTile) throws IOException, ClassNotFoundException
	{
		return reader.readObject(townTile);
	}
}
