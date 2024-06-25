package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.InventoryReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftArmory;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Armory;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

@Getter
public class ArmoryReader extends StructureReader<Armory>
{
	private final Reader<Armory> reader;
	private final ParameterizedReadable<Armory, Town> parameterizedReadable;
	
	public ArmoryReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Armory.class);
		
		final ObjectType objectType = PluginObjectType.ARMORY;
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
	
	private void readerVersion0(Armory armory) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(armory);
		
		armory.setInventoryContents(readers.getReader(InventoryReader.class).readReference());
		armory.setBattleInventoryContents(readers.getReader(InventoryReader.class).readReference());
	}
	
	private Readable<Armory> parameterizedReadableVersion0(Town town)
	{
		return () -> new CraftArmory(town);
	}
	
	@Override
	public Armory readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
}
