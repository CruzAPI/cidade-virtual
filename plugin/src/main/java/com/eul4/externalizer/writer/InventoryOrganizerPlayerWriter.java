package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.player.InventoryOrganizerPlayer;

import java.io.IOException;

public class InventoryOrganizerPlayerWriter extends SpiritualPlayerWriter<InventoryOrganizerPlayer>
{
	public InventoryOrganizerPlayerWriter(Writers writers)
	{
		super(writers, InventoryOrganizerPlayer.class);
	}
	
	@Override
	protected void writeObject(InventoryOrganizerPlayer inventoryOrganizerPlayer) throws IOException
	{
		super.writeObject(inventoryOrganizerPlayer);
	}
}
