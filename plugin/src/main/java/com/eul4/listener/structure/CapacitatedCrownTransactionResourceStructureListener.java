package com.eul4.listener.structure;

import com.eul4.Main;
import com.eul4.event.StructureDestroyEvent;
import com.eul4.model.inventory.craft.CraftArmoryMenuGui;
import com.eul4.model.player.physical.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Armory;
import com.eul4.model.town.structure.CapacitatedCrownTransactionResourceStructure;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@RequiredArgsConstructor
public class CapacitatedCrownTransactionResourceStructureListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onDestroy(StructureDestroyEvent event)
	{
		if(!(event.getStructure() instanceof CapacitatedCrownTransactionResourceStructure structure))
		{
			return;
		}
		
		structure.placeTransactionalResources();
	}
}
