package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.util.BlockVector;

import java.io.IOException;

//TODO unused...
public class BlockVector3Writer extends ObjectWriter<BlockVector3>
{
	public BlockVector3Writer(Writers writers)
	{
		super(writers, BlockVector3.class);
	}
	
	@Override
	protected void writeObject(BlockVector3 blockVector3) throws IOException
	{
		out.writeInt(blockVector3.getBlockX());
		out.writeInt(blockVector3.getBlockY());
		out.writeInt(blockVector3.getBlockZ());
	}
}
