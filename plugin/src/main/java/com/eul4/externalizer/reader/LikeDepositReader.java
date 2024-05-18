package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftLikeDeposit;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.LikeDeposit;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

@Getter
public class LikeDepositReader extends DepositReader<LikeDeposit>
{
	private final Reader<LikeDeposit> reader;
	private final ParameterizedReadable<LikeDeposit, Town> parameterizedReadable;
	
	public LikeDepositReader(Readers readers) throws InvalidVersionException
	{
		super(readers, LikeDeposit.class);
		
		final ObjectType objectType = PluginObjectType.LIKE_DEPOSIT;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private Readable<LikeDeposit> parameterizedReadableVersion0(Town town)
	{
		return () -> new CraftLikeDeposit(town);
	}
	
	@Override
	public LikeDeposit readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	private void readerVersion0(LikeDeposit likeDeposit) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(likeDeposit);
	}
}
