package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.StabilityFormula;
import lombok.Getter;

import java.io.IOException;

@Getter
public class StabilityFormularReader extends ObjectReader<StabilityFormula>
{
	private final Reader<StabilityFormula> reader;
	private final Readable<StabilityFormula> readable;
	
	public StabilityFormularReader(Readers readers) throws InvalidVersionException
	{
		super(readers, StabilityFormula.class);
		
		final ObjectType objectType = PluginObjectType.STABILITY_FORMULA;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.readable = this::readableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private StabilityFormula readableVersion0() throws IOException
	{
		boolean stable = in.readBoolean();
		
		if(stable)
		{
			return StabilityFormula.STABLE;
		}
		else
		{
			return new StabilityFormula(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
		}
	}
	
	public StabilityFormula readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
}
