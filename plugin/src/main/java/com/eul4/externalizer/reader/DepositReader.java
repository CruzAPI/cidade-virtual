package com.eul4.externalizer.reader;

import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.structure.Deposit;

import java.io.IOException;
import java.io.ObjectInput;

public abstract class DepositReader<D extends Deposit> extends StructureReader<D>
{
	private final Reader<D> reader;
	
	public DepositReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		if(versions.getDepositVersion() == 0)
		{
			this.reader = this::readerVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid Deposit version: " + versions.getDepositVersion());
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
