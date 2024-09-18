package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.Deposit;
import com.eul4.model.town.structure.PhysicalDeposit;

import java.io.IOException;

public abstract class PhysicalDepositWriter<N extends Number, D extends PhysicalDeposit<N>> extends StructureWriter<D>
{
	public PhysicalDepositWriter(Writers writers, Class<D> type)
	{
		super(writers, type);
	}
	
	@Override
	protected void writeObject(D physicalDeposit) throws IOException
	{
		super.writeObject(physicalDeposit);
	}
}
