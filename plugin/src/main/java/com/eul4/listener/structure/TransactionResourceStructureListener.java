package com.eul4.listener.structure;

import com.eul4.Main;
import com.eul4.event.StructureDestroyEvent;
import com.eul4.event.TransactionalResourceStructureStealEvent;
import com.eul4.model.town.structure.TransactionalResourceStructure;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class TransactionResourceStructureListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onDestroy(StructureDestroyEvent event)
	{
		if(!(event.getStructure() instanceof TransactionalResourceStructure structure))
		{
			return;
		}
		
		structure.placeTransactionalResources();
	}
	
	@EventHandler
	public void onSteal(TransactionalResourceStructureStealEvent event)
	{
		event.getStructure().updateHologram();
	}
}
