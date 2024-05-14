package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.structure.Deposit;
import com.eul4.type.player.PluginObjectType;

import java.io.IOException;

public abstract class DepositReader<D extends Deposit> extends StructureReader<D>
{
	private final Reader<D> reader;
	
	public DepositReader(Readers readers, Class<D> type) throws InvalidVersionException
	{
		super(readers, type);
		
		final ObjectType objectType = PluginObjectType.DEPOSIT;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	@Override
	protected D readObject(D deposit) throws IOException, ClassNotFoundException
	{
		super.readObject(deposit);
		return reader.readObject(deposit);
	}
}
