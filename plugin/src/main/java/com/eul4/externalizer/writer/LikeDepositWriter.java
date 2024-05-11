package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.LikeDeposit;

import java.io.IOException;

public class LikeDepositWriter extends DepositWriter<LikeDeposit>
{
	public LikeDepositWriter(Writers writers)
	{
		super(writers);
	}
	
	@Override
	protected void writeObject(LikeDeposit likeDeposit) throws IOException
	{
	
	}
}
