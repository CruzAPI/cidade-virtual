package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.structure.Deposit;
import com.eul4.type.player.PluginObjectType;

import java.io.IOException;

public abstract class DepositReader<D extends Deposit> extends StructureReader<D>
{
	private final Reader<D> reader;
	
	public DepositReader(Readers readers) throws InvalidVersionException
	{
		super(readers);
		
		final ObjectType objectType = PluginObjectType.DEPOSIT;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private D readerVersion0(D deposit) throws IOException, ClassNotFoundException
	{
		//TODO: read deposit fields...
		
		return deposit;
	}
	
	@Override
	protected D readObject(D deposit) throws IOException, ClassNotFoundException
	{
		return reader.readObject(deposit);
	}
}
