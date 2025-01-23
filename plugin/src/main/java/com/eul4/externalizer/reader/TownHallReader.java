package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftTownHall;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.structure.TownHall;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

@Getter
public class TownHallReader extends StructureReader<TownHall>
{
	private final Reader<TownHall> reader;
	private final ParameterizedReadable<TownHall, Town> parameterizedReadable;
	
	public TownHallReader(Readers readers) throws InvalidVersionException
	{
		super(readers, TownHall.class);
		
		final ObjectType objectType = PluginObjectType.TOWN_HALL;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		case 1:
			this.reader = this::readerVersion1;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private void readerVersion0(TownHall townHall) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(townHall);
		
		townHall.setDefaultCapacitatedCrownHolder();
	}
	
	private void readerVersion1(TownHall townHall) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(townHall);
		
		townHall.setCapacitatedCrownHolder(readers.getReader(CapacitatedCrownHolderReader.class)
				.readReference(townHall.getTown().getPlugin()));
	}
	
	private Readable<TownHall> parameterizedReadableVersion0(Town town)
	{
		return () -> new CraftTownHall(town);
	}
	
	@Override
	public TownHall readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
}
