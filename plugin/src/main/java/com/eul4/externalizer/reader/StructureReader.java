package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.HologramReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.Reader;
import com.eul4.enums.StructureStatus;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import com.eul4.type.player.PluginObjectType;

import java.io.IOException;

public abstract class StructureReader<S extends Structure> extends ObjectReader<S>
{
	private final Reader<S> reader;
	
	public StructureReader(Readers readers) throws InvalidVersionException
	{
		super(readers);
		
		final ObjectType objectType = PluginObjectType.STRUCTURE;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private S readerVersion0(S structure) throws IOException, ClassNotFoundException
	{
		structure.setCenterTownBlock(readers.getReader(TownBlockReader.class).readReference(structure.getTown()));
		structure.setLevel(in.readInt());
		structure.setRotation(in.readInt());
//		TODO: townBlocks = townSerializer.readStructureTownBlocks(this, in);
		structure.setStatus(StructureStatus.values()[in.readInt()]);
		structure.setBuildTicks(in.readInt());
		structure.setTotalBuildTicks(in.readInt());
		structure.setHologram(readers.getReader(HologramReader.class).readReference(structure.getTown().getPlugin()));
		
		return structure;
	}
	
	public abstract S readReference(Town town) throws IOException, ClassNotFoundException;
	
	@Override
	protected S readObject(S structure) throws IOException, ClassNotFoundException
	{
		return reader.readObject(structure);
	}
}
