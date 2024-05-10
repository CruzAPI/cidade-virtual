package com.eul4.common.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.wrapper.CommonVersions;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.UUID;

public class BlockReader extends ObjectReader<Block>
{
	private final Reader<Block> reader;
	private final ParameterizedReadable<Block, Plugin> parameterizedReadable;
	
	public BlockReader(ObjectInput in, CommonVersions commonVersions) throws InvalidVersionException
	{
		super(in, commonVersions);
		
		if(commonVersions.getBlockVersion() == 0)
		{
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
		}
		else
		{
			throw new InvalidVersionException("Invalid Block version: " + commonVersions.getBlockVersion());
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
	
	private Block readerVersion0(Block block) throws IOException, ClassNotFoundException
	{
		return block;
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
