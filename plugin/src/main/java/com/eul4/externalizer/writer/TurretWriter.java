package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.EntityWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.Turret;

import java.io.IOException;

public class TurretWriter extends StructureWriter<Turret>
{
	public TurretWriter(Writers writers)
	{
		super(writers, Turret.class);
	}
	
	@Override
	protected void writeObject(Turret turret) throws IOException
	{
		super.writeObject(turret);
		
		writers.getWriter(EntityWriter.class).writeReferenceNotNull(turret.getEvoker());
	}
}
