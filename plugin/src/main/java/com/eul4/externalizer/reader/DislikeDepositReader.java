package com.eul4.externalizer.reader;

import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftDislikeDeposit;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Deposit;
import com.eul4.model.town.structure.DislikeDeposit;

import java.io.IOException;
import java.io.ObjectInput;

public class DislikeDepositReader extends DepositReader<DislikeDeposit>
{
	private final Reader<DislikeDeposit> reader;
	private final ParameterizedReadable<DislikeDeposit, Town> parameterizedReadable;
	
	public DislikeDepositReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		if(versions.getDislikeDepositVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid DislikeDeposit version: " + versions.getDislikeDepositVersion());
		}
	}
	
	private Readable<DislikeDeposit> parameterizedReadableVersion0(Town town) throws IOException, ClassNotFoundException
	{
		return () -> new CraftDislikeDeposit(town);
	}
	
	private DislikeDeposit readerVersion0(DislikeDeposit dislikeDeposit) throws IOException, ClassNotFoundException
	{
		//TODO: read deposit fields...
		
		return dislikeDeposit;
	}
	
	@Override
	public DislikeDeposit readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected DislikeDeposit readObject(DislikeDeposit dislikeDeposit) throws IOException, ClassNotFoundException
	{
		return reader.readObject(dislikeDeposit);
	}
}
