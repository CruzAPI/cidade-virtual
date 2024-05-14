package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftDislikeGenerator;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.DislikeGenerator;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

public class DislikeGeneratorReader extends GeneratorReader<DislikeGenerator>
{
	private final Reader<DislikeGenerator> reader;
	@Getter
	private final ParameterizedReadable<DislikeGenerator, Town> parameterizedReadable;
	
	public DislikeGeneratorReader(Readers readers) throws InvalidVersionException
	{
		super(readers, DislikeGenerator.class);
		
		final ObjectType objectType = PluginObjectType.DISLIKE_GENERATOR;
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
	
	private Readable<DislikeGenerator> parameterizedReadableVersion0(Town town)
	{
		return () -> new CraftDislikeGenerator(town);
	}
	
	@Override
	public DislikeGenerator readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected DislikeGenerator readObject(DislikeGenerator dislikeGenerator) throws IOException, ClassNotFoundException
	{
		super.readObject(dislikeGenerator);
		return reader.readObject(dislikeGenerator);
	}
}
