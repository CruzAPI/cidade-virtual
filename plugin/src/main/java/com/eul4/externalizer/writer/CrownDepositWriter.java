package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.CrownDeposit;
import com.eul4.model.town.structure.LikeDeposit;

import java.io.IOException;
import java.math.BigDecimal;

public class CrownDepositWriter extends PhysicalDepositWriter<BigDecimal, CrownDeposit>
{
	public CrownDepositWriter(Writers writers)
	{
		super(writers, CrownDeposit.class);
	}
	
	@Override
	protected void writeObject(CrownDeposit crownDeposit) throws IOException
	{
		super.writeObject(crownDeposit);
		
		writers.getWriter(CapacitatedCrownHolderWriter.class)
				.writeReferenceNotNull(crownDeposit.getCapacitatedCrownHolder());
	}
}
