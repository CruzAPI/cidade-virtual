package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftDislikeDeposit;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.DislikeDeposit;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;

public class DislikeDepositReader extends DepositReader<DislikeDeposit>
{
	private final Reader<DislikeDeposit> reader;
	@Getter
	private final ParameterizedReadable<DislikeDeposit, Town> parameterizedReadable;
	
	public DislikeDepositReader(Readers readers) throws InvalidVersionException
	{
		super(readers, DislikeDeposit.class);
		
		final ObjectType objectType = PluginObjectType.DISLIKE_DEPOSIT;
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
	
	private Readable<DislikeDeposit> parameterizedReadableVersion0(Town town)
	{
		return () -> new CraftDislikeDeposit(town);
	}
	
	@Override
	public DislikeDeposit readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
	
	@Override
	protected DislikeDeposit readObject(DislikeDeposit dislikeDeposit) throws IOException, ClassNotFoundException
	{
		super.readObject(dislikeDeposit);
		return reader.readObject(dislikeDeposit);
	}
}
