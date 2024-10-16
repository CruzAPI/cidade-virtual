package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.BigDecimalWriter;
import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.externalizer.writer.UUIDWriter;
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
		UUIDWriter uuidWriter = writers.getWriter(UUIDWriter.class);
		BigDecimalWriter bigDecimalWriter = writers.getWriter(BigDecimalWriter.class);
		
		uuidWriter.writeReferenceNotNull(capacitatedCrownHolder.getTownUniqueId());
		bigDecimalWriter.writeReferenceNotNull(capacitatedCrownHolder.getBalance());
	}
}
