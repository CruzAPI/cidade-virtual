package com.eul4.service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BlockDataSerializer
{
	public BlockData deserialize(byte[] bytes)
	{
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		
		final byte[] main = new byte[2];
		
		byteBuffer.get(main);
		
		BlockData blockData = new BlockData();
		
		if((main[0] & 0b10000000) == 0b10000000)
		{
			byte bitMap = byteBuffer.get();
			
			blockData.hasHardness((bitMap & 0b10000000) == 0b10000000);
		}
		
		return blockData;
	}
	
	public byte[] serialize(BlockData blockData)
	{
		try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream))
		{
			final byte[] main = getMainBytes(blockData);
			
			dataOutputStream.write(main);
			
			if((main[0] & 0b10000000) == 0b10000000)
			{
				byte bitMap = 0b00000000;
				
				bitMap |= (byte) (blockData.hasHardness() ? 0b10000000 : 0b00000000);
				
				dataOutputStream.write(bitMap);
			}
			
			return byteArrayOutputStream.toByteArray();
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private byte[] getMainBytes(BlockData blockData)
	{
		final byte[] main = new byte[2];
		
		main[0] |= (byte) (blockData.hasHardness() ? 0b10000000 : 0b00000000);
		
		return main;
	}
}
