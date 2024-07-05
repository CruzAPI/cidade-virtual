package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.EntityWriter;
import com.eul4.common.externalizer.writer.InventoryWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.structure.Armory;

import java.io.IOException;

public class ArmoryWriter extends StructureWriter<Armory>
{
	public ArmoryWriter(Writers writers)
	{
		super(writers, Armory.class);
	}
	
	@Override
	protected void writeObject(Armory armory) throws IOException
	{
		super.writeObject(armory);
		
		writers.getWriter(InventoryWriter.class).writeReferenceNotNull(armory.getStorageContents());
		writers.getWriter(InventoryWriter.class).writeReferenceNotNull(armory.getBattleInventoryContents());
		
		writers.getWriter(EntityWriter.class).writeReferenceNotNull(armory.getNPC());
	}
}
