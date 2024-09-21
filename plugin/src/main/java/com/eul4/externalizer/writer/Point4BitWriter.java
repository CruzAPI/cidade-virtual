package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.awt.*;
import java.io.IOException;

public class Point4BitWriter extends ObjectWriter<Point>
{
	public Point4BitWriter(Writers writers)
	{
		super(writers, Point.class);
	}
	
	@Override
	protected void writeObject(Point point) throws IOException
	{
		byte compact = (byte) ((point.x & 0x0F) << 4 | (point.y & 0x0F));
		out.writeByte(compact);
	}
}
