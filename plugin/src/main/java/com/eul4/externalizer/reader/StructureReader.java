package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.HologramReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Reader;
import com.eul4.enums.StructureStatus;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;
import java.util.UUID;

@Getter
public abstract class StructureReader<S extends Structure> extends ObjectReader<S>
{
	private final Reader<S> reader;
	
	public StructureReader(Readers readers, Class<S> type) throws InvalidVersionException
	{
		super(readers, type);
		
		final ObjectType objectType = PluginObjectType.STRUCTURE;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			break;
		case 1:
			this.reader = this::readerVersion1;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private S readerVersion0(S structure) throws IOException, ClassNotFoundException
	{
		structure.setDefaultUUID();
		structure.setDefaultCenterPosition();
		
		structure.setCenterTownBlock(readers.getReader(TownBlockReader.class).readReference(structure.getTown()));
		structure.setLevel(in.readInt());
		structure.setRotation(in.readInt());
		structure.setTownBlockSet(readers.getReader(TownBlockSetReader.class).readReference(structure.getTown()));
		structure.setStatus(StructureStatus.values()[in.readInt()]);
		structure.setBuildTicks(in.readInt());
		structure.setTotalBuildTicks(in.readInt());
		structure.setHologram(readers.getReader(HologramReader.class).readReference(structure.getTown().getPlugin()));
		
		return structure;
	}
	
	private S readerVersion1(S structure) throws IOException, ClassNotFoundException
	{
		structure.setUUID(new UUID(in.readLong(), in.readLong()));
		structure.setCenterTownBlock(readers.getReader(TownBlockReader.class).readReference(structure.getTown()));
		structure.setLevel(in.readInt());
		structure.setRotation(in.readInt());
		structure.setTownBlockSet(readers.getReader(TownBlockSetReader.class).readReference(structure.getTown()));
		structure.setStatus(StructureStatus.values()[in.readInt()]);
		structure.setBuildTicks(in.readInt());
		structure.setTotalBuildTicks(in.readInt());
		structure.setHologram(readers.getReader(HologramReader.class).readReference(structure.getTown().getPlugin()));
		structure.setCenterPosition(readers.getReader(Vector3Reader.class).readReference());
		
		return structure;
	}
	
	public abstract S readReference(Town town) throws IOException, ClassNotFoundException;
	
	public abstract ParameterizedReadable<S, Town> getParameterizedReadable();
	
	public S readReference(S structure) throws IOException, ClassNotFoundException
	{
		return super.readReference(() -> structure);
	}
}
