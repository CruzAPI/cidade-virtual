package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.BlockWriter;
import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.model.town.Town;

import java.io.IOException;

public class TownWriter extends ObjectWriter<Town>
{
	public TownWriter(Writers writers)
	{
		super(writers, Town.class);
	}
	
	@Override
	protected void writeObject(Town town) throws IOException
	{
		out.writeLong(town.getOwnerUUID().getMostSignificantBits());
		out.writeLong(town.getOwnerUUID().getLeastSignificantBits());
		writers.getWriter(BlockWriter.class).writeReference(town.getBlock());
		
		writers.getWriter(TownBlockMapWriter.class).writeReference(town.getTownBlockMap());
		writers.getWriter(TownTileMapWriter.class).writeReference(town.getTownTileMap());
		writers.getWriter(StructureSetWriter.class).writeReference(town.getStructureSet());
		writers.getWriter(GenericStructureWriter.class).writeReference(town.getMovingStructure());
		writers.getWriter(TownHallWriter.class).writeReference(town.getTownHall());
		writers.getWriter(ArmoryWriter.class).writeReference(town.getArmory());
		out.writeInt(town.getLikes());
		out.writeInt(town.getDislikes());
		out.writeDouble(town.getHardness());
		out.writeLong(town.getLastAttackFinishTick());
		
		writers.getWriter(BoughtTileMapByDepthWriter.class).writeReferenceNotNull(town.getBoughtTileMapByDepth());
	}
}
