package com.eul4.externalizer;

import com.eul4.service.BlockData;

import java.io.*;

public class BlockDataExternalizer
{
	public BlockData read(long version, DataInput in) throws IOException
	{
		if(version == 1L)
		{
			BlockData blockData = new BlockData();
			
			byte[] main = new byte[1];
			
			in.readFully(main);
			
			if((main[0] & 0b10000000) == 0b10000000)
			{
				byte bitMap = in.readByte();
				
				blockData.hasHardness((bitMap & 0b10000000) == 0b10000000);
			}
			
			return blockData;
		}
		else
		{
			throw new RuntimeException();
		}
	}
	
	public void write(BlockData blockData, DataOutput out) throws IOException
	{
		final byte[] main = getMainBytes(blockData);
		
		out.write(main);
		
		if((main[0] & 0b10000000) == 0b10000000)
		{
			byte bitMap = 0b00000000;
			
			bitMap |= (byte) (blockData.hasHardness() ? 0b10000000 : 0b00000000);
			
			out.write(bitMap);
		}
	}
	
	private byte[] getMainBytes(BlockData blockData)
	{
		final byte[] main = new byte[1];
		
		main[0] |= (byte) (blockData.hasHardness() ? 0b10000000 : 0b00000000);
		
		return main;
	}
}
