package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftTownHall;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.TownHall;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

public class TownHallReader extends StructureReader<TownHall>
{
	private final Reader<TownHall> reader;
	@Getter
	private final ParameterizedReadable<TownHall, Town> parameterizedReadable;
	
	public TownHallReader(Readers readers) throws InvalidVersionException
	{
		super(readers, TownHall.class);
		
		final ObjectType objectType = PluginObjectType.TOWN_HALL;
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
	
	private Readable<TownHall> parameterizedReadableVersion0(Town town)
	{
		return () -> new CraftTownHall(town);
	}
	
	@Override
	public TownHall readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected TownHall readObject(TownHall townHall) throws IOException, ClassNotFoundException
	{
		super.readObject(townHall);
		return reader.readObject(townHall);
	}
}
