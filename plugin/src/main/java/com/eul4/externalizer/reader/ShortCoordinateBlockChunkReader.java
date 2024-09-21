package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.awt.*;
import java.io.IOException;

@Getter
public class ShortCoordinateBlockChunkReader extends ObjectReader<Block>
{
	private final Reader<Block> reader;
	private final ParameterizedReadable<Block, Chunk> parameterizedReadable;
	
	public ShortCoordinateBlockChunkReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Block.class);
		
		final ObjectType objectType = PluginObjectType.SHORT_COORDINATE_BLOCK_CHUNK;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		case 1:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion1;
			break;
		case 2:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion2;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private Readable<Block> parameterizedReadableVersion0(Chunk chunk)
	{
		return () ->
		{
			short coordinates = in.readShort();
			
			final int x = (coordinates >> 12) & 0x0F;
			final int z = (coordinates >> 8) & 0x0F;
			final int y = (coordinates) & 0xFF;
			
			return chunk.getBlock(x, y, z);
		};
	}
	
	private Readable<Block> parameterizedReadableVersion1(Chunk chunk)
	{
		return () ->
		{
			Point point = readers.getReader(Point4BitReader.class).readReference();
			
			final int x = point.x;
			final int z = point.y;
			final int y = in.readShort();
			
			return chunk.getBlock(x, y, z);
		};
	}
	
	private Readable<Block> parameterizedReadableVersion2(Chunk chunk)
	{
		return () ->
		{
			Point point = readers.getReader(Point4BitReader.class).readObject();
			
			final int x = point.x;
			final int z = point.y;
			final int y = in.readShort();
			
			return chunk.getBlock(x, y, z);
		};
	}
	
	public Block readObject(Chunk chunk) throws IOException, ClassNotFoundException
	{
		Block block = parameterizedReadable.getReadable(chunk).read();
		
		reader.readObject(block);
		
		return block;
	}
	
	public Block readReference(Chunk chunk) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(chunk));
	}
}
