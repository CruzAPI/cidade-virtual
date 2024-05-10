package com.eul4.externalizer.reader;

import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftLikeDeposit;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.LikeDeposit;

import java.io.IOException;
import java.io.ObjectInput;

public class LikeDepositReader extends DepositReader<LikeDeposit>
{
	private final Reader<LikeDeposit> reader;
	private final ParameterizedReadable<LikeDeposit, Town> parameterizedReadable;
	
	public LikeDepositReader(ObjectInput in, Versions versions) throws InvalidVersionException
	{
		super(in, versions);
		
		if(versions.getLikeDepositVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid LikeDeposit version: " + versions.getLikeDepositVersion());
		}
	}
	
	private Readable<LikeDeposit> parameterizedReadableVersion0(Town town) throws IOException, ClassNotFoundException
	{
		return () -> new CraftLikeDeposit(town);
	}
	
	private LikeDeposit readerVersion0(LikeDeposit likeDeposit) throws IOException, ClassNotFoundException
	{
		//TODO: read deposit fields...
		
		return likeDeposit;
	}
	
	@Override
	public LikeDeposit readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected LikeDeposit readObject(LikeDeposit likeDeposit) throws IOException, ClassNotFoundException
	{
		return reader.readObject(likeDeposit);
	}
}
