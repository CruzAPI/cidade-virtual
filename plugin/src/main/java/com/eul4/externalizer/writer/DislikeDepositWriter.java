package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.DislikeDeposit;

import java.io.IOException;

public class DislikeDepositWriter extends DepositWriter<DislikeDeposit>
{
	public DislikeDepositWriter(Writers writers)
	{
		super(writers, DislikeDeposit.class);
	}
	
	@Override
	protected void writeObject(DislikeDeposit dislikeDeposit) throws IOException
	{
		super.writeObject(dislikeDeposit);
	}
}
