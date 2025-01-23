package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.BigDecimalReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.externalizer.reader.UUIDReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.holder.UnlimitedCrownHolder;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

public class UnlimitedCrownHolderReader extends ObjectReader<UnlimitedCrownHolder>
{
	@Getter
	private final Reader<UnlimitedCrownHolder> reader;
	private final ParameterizedReadable<UnlimitedCrownHolder, Main> parameterizedReadable;
	
	public UnlimitedCrownHolderReader(Readers readers) throws InvalidVersionException
	{
		super(readers, UnlimitedCrownHolder.class);
		
		final ObjectType objectType = PluginObjectType.UNLIMITED_CROWN_HOLDER;
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
	
	private Readable<UnlimitedCrownHolder> parameterizedReadableVersion0(Main plugin)
	{
		return () ->
		{
			UUID uniqueId = readers.getReader(UUIDReader.class).readReference();
			BigDecimal balance = readers.getReader(BigDecimalReader.class).readReference();
			
			return new UnlimitedCrownHolder(plugin, uniqueId, balance);
		};
	}
	
	public UnlimitedCrownHolder readReference(Main plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
}
