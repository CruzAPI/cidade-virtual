package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.service.BlockData;
import com.eul4.type.player.PluginObjectType;

import java.io.IOException;

public class BlockDataReader extends ObjectReader<BlockData>
{
	private final Reader<BlockData> reader;
	private final Readable<BlockData> readable;
	
	public BlockDataReader(Readers readers) throws InvalidVersionException
	{
		super(readers, BlockData.class);
		
		final ObjectType objectType = PluginObjectType.BLOCK_DATA;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			this.readable = this::readableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private BlockData readerVersion0(BlockData blockData) throws IOException
	{
		byte[] main = new byte[1];
		
		in.readFully(main);
		
		if((main[0] & 0b10000000) == 0b10000000)
		{
			byte bitmap = in.readByte();
			
			blockData.hasHardness((bitmap & 0b10000000) == 0b10000000);
		}
		
		return blockData;
	}
	
	private BlockData readableVersion0()
	{
		return new BlockData();
	}
	
	public BlockData readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
	
	protected BlockData readObject() throws IOException, ClassNotFoundException
	{
		return readObject(readable.read());
	}
	
	@Override
	protected BlockData readObject(BlockData blockData) throws IOException, ClassNotFoundException
	{
		return reader.readObject(blockData);
	}
}
