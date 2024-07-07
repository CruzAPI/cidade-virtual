package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftTurret;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Turret;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

@Getter
public class TurretReader extends StructureReader<Turret>
{
	private final Reader<Turret> reader;
	private final ParameterizedReadable<Turret, Town> parameterizedReadable;
	
	public TurretReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Turret.class);
		
		final ObjectType objectType = PluginObjectType.TURRET;
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
	
	private void readerVersion0(Turret turret) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(turret);
	}
	
	private Readable<Turret> parameterizedReadableVersion0(Town town)
	{
		return () -> new CraftTurret(town);
	}
	
	@Override
	public Turret readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
}
