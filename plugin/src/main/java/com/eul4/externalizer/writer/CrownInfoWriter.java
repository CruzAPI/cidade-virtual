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
		UnlimitedCrownHolderWriter unlimitedCrownHolderWriter = writers
				.getWriter(UnlimitedCrownHolderWriter.class);
		
		unlimitedCrownHolderWriter.writeReferenceNotNull(crownInfo.getServerTreasure());
		unlimitedCrownHolderWriter.writeReferenceNotNull(crownInfo.getJackpot());
		unlimitedCrownHolderWriter.writeReferenceNotNull(crownInfo.getTownHallVault());
		unlimitedCrownHolderWriter.writeReferenceNotNull(crownInfo.getEul4Insights());
	}
}
