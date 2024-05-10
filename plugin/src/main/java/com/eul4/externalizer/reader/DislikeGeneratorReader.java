package com.eul4.externalizer.reader;

import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftDislikeGenerator;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.DislikeGenerator;

import java.io.IOException;
import java.io.ObjectInput;

public class DislikeGeneratorReader extends GeneratorReader<DislikeGenerator>
{
	private final Reader<DislikeGenerator> reader;
	private final ParameterizedReadable<DislikeGenerator, Town> parameterizedReadable;
	
	public DislikeGeneratorReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		if(versions.getDislikeGeneratorVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid DislikeGenerator version: " + versions.getDislikeGeneratorVersion());
		}
	}
	
	private Readable<DislikeGenerator> parameterizedReadableVersion0(Town town) throws IOException, ClassNotFoundException
	{
		return () -> new CraftDislikeGenerator(town);
	}
	
	private DislikeGenerator readerVersion0(DislikeGenerator dislikeGenerator) throws IOException, ClassNotFoundException
	{
		//TODO: read generator fields...
		
		return dislikeGenerator;
	}
	
	@Override
	public DislikeGenerator readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected DislikeGenerator readObject(DislikeGenerator dislikeGenerator) throws IOException, ClassNotFoundException
	{
		return reader.readObject(dislikeGenerator);
	}
}
