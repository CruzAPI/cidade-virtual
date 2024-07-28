package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.sk89q.worldedit.math.Vector3;

import java.io.IOException;

public class Vector3Writer extends ObjectWriter<Vector3>
{
	public Vector3Writer(Writers writers)
	{
		super(writers, Vector3.class);
	}
	
	@Override
	protected void writeObject(Vector3 vector3) throws IOException
	{
		out.writeDouble(vector3.x());
		out.writeDouble(vector3.y());
		out.writeDouble(vector3.z());
	}
}
