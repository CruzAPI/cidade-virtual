package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.town.structure.PhysicalDeposit;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

@Getter
public abstract class PhysicalDepositReader<N extends Number & Comparable<N>, D extends PhysicalDeposit<N>> extends StructureReader<D>
{
	private final Reader<D> reader;
	
	public PhysicalDepositReader(Readers readers, Class<D> type) throws InvalidVersionException
	{
		super(readers, type);
		
		final ObjectType objectType = PluginObjectType.PHYSICAL_DEPOSIT;
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
	
	private void readerVersion0(D physicalDeposit) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(physicalDeposit);
	}
}
