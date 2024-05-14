package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.UUID;

public class BlockReader extends ObjectReader<Block>
{
	private final Reader<Block> reader;
	private final ParameterizedReadable<Block, Plugin> parameterizedReadable;
	
	public BlockReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Block.class);
		
		final ObjectType objectType = CommonObjectType.BLOCK;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = Reader.identity();
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private Readable<Block> parameterizedReadableVersion0(Plugin plugin)
	{
		return () ->
		{
			UUID uuid = new UUID(in.readLong(), in.readLong());
			
			int x = in.readInt();
			int y = in.readInt();
			int z = in.readInt();
			
			return plugin.getServer().getWorld(uuid).getBlockAt(x, y, z);
		};
	}
	
	public Block readReference(Plugin plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
	
	@Override
	protected Block readObject(Block block) throws IOException, ClassNotFoundException
	{
		return reader.readObject(block);
	}
}
