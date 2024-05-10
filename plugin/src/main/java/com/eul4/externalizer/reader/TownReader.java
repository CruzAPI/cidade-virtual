package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.BlockReader;
import com.eul4.common.externalizer.reader.LocationReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.CraftTown;
import com.eul4.model.town.Town;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.UUID;

public class TownReader extends ObjectReader<Town>
{
	private final Reader<Town> reader;
	private final ParameterizedReadable<Town, Main> parameterizedReadable;
	
	private final TownBlockMapReader townBlockMapReader;
	private final TownTileMapReader townTileMapReader;
	private final StructureSetReader structureSetReader;
	private final StructureSetReader structureSetReader;
	private final LocationReader locationReader;
	private final BlockReader blockReader;
	
	public TownReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		this.townBlockMapReader = new TownBlockMapReader(in, versions);
		this.townTileMapReader = new TownTileMapReader(in, versions);
		this.structureSetReader = new StructureSetReader(in, versions);
		this.locationReader = new LocationReader(in, versions);
		this.blockReader = new BlockReader(in, versions);
		
		if(versions.getTownVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid Town version: " + versions.getTownVersion());
		}
	}
	
	private Readable<Town> parameterizedReadableVersion0(Main plugin) throws IOException, ClassNotFoundException
	{
		return () -> new CraftTown(new UUID(in.readLong(), in.readLong()),
				blockReader.readReference(plugin),
				plugin);
	}
	
	private Town readerVersion0(Town town) throws IOException, ClassNotFoundException
	{
//		TODO: read town
		
		town.setTownBlocks(townBlockMapReader.readReference(town));
		town.setTownTiles(townTileMapReader.readReference(town));
		town.setStructures(structureSetReader.readReference(town));
		town.setMovingStructure(stru);
//		movingStructure = townSerializer.readStructureReference(this, in);
//		townHall = (TownHall) Objects.requireNonNull(townSerializer.readStructureReference(this, in));
//		likes = in.readInt();
//		dislikes = in.readInt();
		return town;
	}
	
	public Town readReference(Main plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
	
	@Override
	protected Town readObject(Town town) throws IOException, ClassNotFoundException
	{
		return reader.readObject(town);
	}
}
