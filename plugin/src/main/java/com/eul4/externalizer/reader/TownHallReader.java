package com.eul4.externalizer.reader;

import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftTownHall;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.TownHall;

import java.io.IOException;
import java.io.ObjectInput;

public class TownHallReader extends StructureReader<TownHall>
{
	private final Reader<TownHall> reader;
	private final ParameterizedReadable<TownHall, Town> parameterizedReadable;
	
	public TownHallReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		if(versions.getTownHallVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid TownHall version: " + versions.getTownHallVersion());
		}
	}
	
	private Readable<TownHall> parameterizedReadableVersion0(Town town) throws IOException, ClassNotFoundException
	{
		return () -> new CraftTownHall(town);
	}
	
	private TownHall readerVersion0(TownHall townHall) throws IOException, ClassNotFoundException
	{
		//TODO: read deposit fields...
		
		return townHall;
	}
	
	@Override
	public TownHall readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected TownHall readObject(TownHall townHall) throws IOException, ClassNotFoundException
	{
		return reader.readObject(townHall);
	}
}
