package com.eul4.common.externalizer.writer;

import com.eul4.common.type.player.Writers;
import org.bukkit.block.Block;

import java.io.IOException;
import java.util.UUID;

public class BlockWriter extends ObjectWriter<Block>
{
	public BlockWriter(Writers writers)
	{
		super(writers);
	}
	
	@Override
	protected void writeObject(Block block) throws IOException
	{
		UUID uuid = block.getWorld().getUID();
		
		out.writeLong(uuid.getMostSignificantBits());
		out.writeLong(uuid.getLeastSignificantBits());
		
		out.writeInt(block.getX());
		out.writeInt(block.getY());
		out.writeInt(block.getZ());
	}
}
