package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.BigDecimalWriter;
import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.holder.CapacitatedCrownHolder;

import java.io.IOException;

public class CapacitatedCrownHolderWriter extends ObjectWriter<CapacitatedCrownHolder>
{
	public CapacitatedCrownHolderWriter(Writers writers)
	{
		super(writers, CapacitatedCrownHolder.class);
	}
	
	@Override
	protected void writeObject(CapacitatedCrownHolder capacitatedCrownHolder) throws IOException
	{
		BigDecimalWriter bigDecimalWriter = writers.getWriter(BigDecimalWriter.class);
		
		bigDecimalWriter.writeReferenceNotNull(capacitatedCrownHolder.getBalance());
	}
}
