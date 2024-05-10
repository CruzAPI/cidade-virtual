package com.eul4.externalizer.reader;

import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.BlockReader;
import com.eul4.common.externalizer.reader.HologramReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.CraftTownTile;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownTile;

import java.io.IOException;
import java.io.ObjectInput;

public class TownTileReader extends ObjectReader<TownTile>
{
	private final BlockReader blockReader;
	
	private final Reader<TownTile> reader;
	private final ParameterizedReadable<TownTile, Town> parameterizedReadable;
	
	public TownTileReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		this.blockReader = new BlockReader(in, versions);
		
		if(versions.getTownBlockMapVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid TownBlockMap version: " + versions.getTownBlockMapVersion());
		}
	}
	
	private Readable<TownTile> parameterizedReadableVersion0(Town town)
	{
		return () -> new CraftTownTile(town, blockReader.readReference(town.getPlugin()), in.readBoolean());
	}
	
	private TownTile readerVersion0(TownTile townTile) throws IOException, ClassNotFoundException
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
