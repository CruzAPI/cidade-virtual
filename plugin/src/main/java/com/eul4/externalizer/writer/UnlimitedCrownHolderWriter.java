package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.BigDecimalWriter;
import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.externalizer.writer.UUIDWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.holder.UnlimitedCrownHolder;

import java.io.IOException;

public class UnlimitedCrownHolderWriter extends ObjectWriter<UnlimitedCrownHolder>
{
	public UnlimitedCrownHolderWriter(Writers writers)
	{
		super(writers, UnlimitedCrownHolder.class);
	}
	
	@Override
	protected void writeObject(UnlimitedCrownHolder unlimitedCrownHolder) throws IOException
	{
		UUIDWriter uuidWriter = writers.getWriter(UUIDWriter.class);
		BigDecimalWriter bigDecimalWriter = writers.getWriter(BigDecimalWriter.class);
		
		uuidWriter.writeReferenceNotNull(unlimitedCrownHolder.getUniqueId());
		bigDecimalWriter.writeReferenceNotNull(unlimitedCrownHolder.getBalance());
	}
}
