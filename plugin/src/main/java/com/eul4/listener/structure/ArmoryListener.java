package com.eul4.listener.structure;

import com.eul4.Main;
import com.eul4.model.inventory.craft.CraftArmoryMenuGui;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Armory;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@RequiredArgsConstructor
public class ArmoryListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onNpcInteract(PlayerInteractEntityEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer))
		{
			return;
		}
		
		Town town = townPlayer.getTown();
		Armory armory = town != null ? town.getArmory() : null;
		
		if(armory == null || armory.getNPC() != event.getRightClicked())
		{
			return;
		}
		
		townPlayer.openGui(new CraftArmoryMenuGui(townPlayer, armory));
	}
}
