package com.eul4.common.externalizer.writer;

import com.eul4.common.type.player.Writers;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalWriter extends ObjectWriter<BigDecimal>
{
	public BigDecimalWriter(Writers writers)
	{
		super(writers, BigDecimal.class);
	}
	
	@Override
	protected void writeObject(BigDecimal bigDecimal) throws IOException
	{
		byte[] unscaledValueBytes = bigDecimal.unscaledValue().toByteArray();
		
		out.writeInt(unscaledValueBytes.length);
		out.write(unscaledValueBytes);
		out.write(bigDecimal.scale());
	}
}
