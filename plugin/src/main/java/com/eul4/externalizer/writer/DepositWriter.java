package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.Deposit;

import java.io.IOException;

public abstract class DepositWriter<D extends Deposit> extends StructureWriter<D>
{
	public DepositWriter(Writers writers, Class<D> type)
	{
		super(writers, type);
	}
	
	@Override
	protected void writeObject(D deposit) throws IOException
	{
		super.writeObject(deposit);
	}
}
