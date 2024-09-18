package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.structure.CraftCrownDeposit;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.CrownDeposit;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;

import java.io.IOException;
import java.math.BigDecimal;

@Getter
public class CrownDepositReader extends PhysicalDepositReader<BigDecimal, CrownDeposit>
{
	private final Reader<CrownDeposit> reader;
	private final ParameterizedReadable<CrownDeposit, Town> parameterizedReadable;
	
	public CrownDepositReader(Readers readers) throws InvalidVersionException
	{
		super(readers, CrownDeposit.class);
		
		final ObjectType objectType = PluginObjectType.CROWN_DEPOSIT;
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
	
	private void readerVersion0(CrownDeposit crownDeposit) throws IOException, ClassNotFoundException
	{
		super.getReader().readObject(crownDeposit);
		
		crownDeposit.setHolder(readers.getReader(CapacitatedCrownHolderReader.class).readReference());
	}
	
	private Readable<CrownDeposit> parameterizedReadableVersion0(Town town)
	{
		return () -> new CraftCrownDeposit(town);
	}
	
	@Override
	public CrownDeposit readReference(Town town) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(town));
	}
}
