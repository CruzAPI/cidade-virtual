package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.service.BlockData;

import java.io.IOException;

public class BlockDataWriter extends ObjectWriter<BlockData>
{
	public BlockDataWriter(Writers writers)
	{
		super(writers, BlockData.class);
	}
	
	@Override
	protected void writeObject(BlockData blockData) throws IOException
	{
		final byte[] main = new byte[1];
		
		main[0] |= (byte) (blockData.hasHardness() ? 0b10000000 : 0b00000000);
		
		out.write(main);
		
		if((main[0] & 0b10000000) == 0b10000000)
		{
			byte bitMap = 0b00000000;
			
			bitMap |= (byte) (blockData.hasHardness() ? 0b10000000 : 0b00000000);
			
			out.write(bitMap);
		}
	}
}
