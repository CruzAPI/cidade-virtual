package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.BigDecimalWriter;
import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.wrapper.CrownInfo;
import com.eul4.wrapper.CryptoInfo;

import java.io.IOException;

public class CrownInfoWriter extends ObjectWriter<CrownInfo>
{
	public CrownInfoWriter(Writers writers)
	{
		super(writers, CrownInfo.class);
	}
	
	@Override
	protected void writeObject(CrownInfo crownInfo) throws IOException
	{
		BigDecimalWriter bigDecimalWriter = writers.getWriter(BigDecimalWriter.class);
		
		bigDecimalWriter.writeReferenceNotNull(crownInfo.getServerTreasure());
		bigDecimalWriter.writeReferenceNotNull(crownInfo.getJackpot());
		bigDecimalWriter.writeReferenceNotNull(crownInfo.getTownHallVault());
		bigDecimalWriter.writeReferenceNotNull(crownInfo.getEul4Insights());
	}
}
