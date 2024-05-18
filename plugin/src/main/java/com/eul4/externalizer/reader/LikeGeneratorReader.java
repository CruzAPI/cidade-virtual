package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftLikeGenerator;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.LikeGenerator;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

@Getter
public class LikeGeneratorReader extends GeneratorReader<LikeGenerator>
{
	private final Reader<LikeGenerator> reader;
	private final ParameterizedReadable<LikeGenerator, Town> parameterizedReadable;
	
	public LikeGeneratorReader(Readers readers) throws InvalidVersionException
	{
		super(readers, LikeGenerator.class);
		
		final ObjectType objectType = PluginObjectType.LIKE_GENERATOR;
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
	
	private Readable<LikeGenerator> parameterizedReadableVersion0(Town town)
	{
		return () -> new CraftLikeGenerator(town);
	}
	
	private void readerVersion0(LikeGenerator likeGenerator) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(likeGenerator);
	}
	
	@Override
	public LikeGenerator readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
}
