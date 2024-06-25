package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftCannon;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Cannon;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

@Getter
public class CannonReader extends StructureReader<Cannon>
{
	private final Reader<Cannon> reader;
	private final ParameterizedReadable<Cannon, Town> parameterizedReadable;
	
	public CannonReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Cannon.class);
		
		final ObjectType objectType = PluginObjectType.CANNON;
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
	
	private void readerVersion0(Cannon cannon) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(cannon);
	}
	
	private Readable<Cannon> parameterizedReadableVersion0(Town town)
	{
		return () -> new CraftCannon(town);
	}
	
	@Override
	public Cannon readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
}
