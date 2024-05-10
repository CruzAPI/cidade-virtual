package com.eul4.externalizer.reader;

import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.HologramReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.wrapper.Reader;
import com.eul4.enums.StructureStatus;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.DislikeGenerator;
import com.eul4.model.town.structure.Structure;

import java.io.IOException;
import java.io.ObjectInput;

public abstract class StructureReader<S extends Structure> extends ObjectReader<S>
{
	private final Reader<S> reader;
	
	protected final TownBlockReader townBlockReader;
	protected final HologramReader hologramReader;
	
	public StructureReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		this.townBlockReader = new TownBlockReader(in, versions);
		this.hologramReader = new HologramReader(in, versions);
		
		if(versions.getStructureVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid Structure version: " + versions.getStructureVersion());
		}
	}
	
	private S readerVersion0(S structure) throws IOException, ClassNotFoundException
	{
		structure.setCenterTownBlock(townBlockReader.readReference(structure.getTown()));
		structure.setLevel(in.readInt());
		structure.setRotation(in.readInt());
//		TODO: townBlocks = townSerializer.readStructureTownBlocks(this, in);
		structure.setStatus(StructureStatus.values()[in.readInt()]);
		structure.setBuildTicks(in.readInt());
		structure.setTotalBuildTicks(in.readInt());
		structure.setHologram(hologramReader.readReference(structure.getTown().getPlugin()));
		
		return structure;
	}
	
	public abstract S readReference(Town town) throws IOException, ClassNotFoundException;
	
	@Override
	protected S readObject(S structure) throws IOException, ClassNotFoundException
	{
		return reader.readObject(structure);
	}
}
