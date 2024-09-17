package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.BigDecimalWriter;
import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.wrapper.CryptoInfo;

import java.io.IOException;

public class CryptoInfoWriter extends ObjectWriter<CryptoInfo>
{
	public CryptoInfoWriter(Writers writers)
	{
		super(writers, CryptoInfo.class);
	}
	
	@Override
	protected void writeObject(CryptoInfo cryptoInfo) throws IOException
	{
		BigDecimalWriter bigDecimalWriter = writers.getWriter(BigDecimalWriter.class);
		
		bigDecimalWriter.writeReferenceNotNull(cryptoInfo.getMarketCap());
		bigDecimalWriter.writeReferenceNotNull(cryptoInfo.getCirculatingSupply());
	}
}
