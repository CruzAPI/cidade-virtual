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

public class LikeGeneratorReader extends GeneratorReader<LikeGenerator>
{
	private final Reader<LikeGenerator> reader;
	@Getter
	private final ParameterizedReadable<LikeGenerator, Town> parameterizedReadable;
	
	public LikeGeneratorReader(Readers readers) throws InvalidVersionException
	{
		super(readers, LikeGenerator.class);
		
		final ObjectType objectType = PluginObjectType.LIKE_GENERATOR;
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
	
	private Readable<LikeGenerator> parameterizedReadableVersion0(Town town)
	{
		return () -> new CraftLikeGenerator(town);
	}
	
	@Override
	public LikeGenerator readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected LikeGenerator readObject(LikeGenerator likeGenerator) throws IOException, ClassNotFoundException
	{
		super.readObject(likeGenerator);
		return reader.readObject(likeGenerator);
	}
}
