package com.eul4.externalizer.reader;

import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftLikeGenerator;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.LikeGenerator;

import java.io.IOException;
import java.io.ObjectInput;

public class LikeGeneratorReader extends GeneratorReader<LikeGenerator>
{
	private final Reader<LikeGenerator> reader;
	private final ParameterizedReadable<LikeGenerator, Town> parameterizedReadable;
	
	public LikeGeneratorReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		if(versions.getLikeGeneratorVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid LikeGenerator version: " + versions.getLikeGeneratorVersion());
		}
	}
	
	private Readable<LikeGenerator> parameterizedReadableVersion0(Town town) throws IOException, ClassNotFoundException
	{
		return () -> new CraftLikeGenerator(town);
	}
	
	private LikeGenerator readerVersion0(LikeGenerator likeGenerator) throws IOException, ClassNotFoundException
	{
		//TODO: read generator fields...
		
		return likeGenerator;
	}
	
	@Override
	public LikeGenerator readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected LikeGenerator readObject(LikeGenerator likeGenerator) throws IOException, ClassNotFoundException
	{
		return reader.readObject(likeGenerator);
	}
}
