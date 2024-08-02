package com.eul4.listener.player.tutorial.step;

import com.eul4.Main;
import com.eul4.event.StructureInteractEvent;
import com.eul4.event.StructureReadyEvent;
import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.WaitFinishTownHallStep;
import com.eul4.model.town.structure.Structure;
import com.eul4.model.town.structure.TownHall;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class WaitFinishTownHallStepListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onTownHallReady(StructureReadyEvent event)
	{
		Structure structure = event.getStructure();
		Player player = structure.getTown().getPlayer();
		
		if(player == null
				|| !(structure instanceof TownHall)
				|| !(plugin.getPlayerManager().get(player) instanceof TutorialTownPlayer tutorialTownPlayer)
				|| !(tutorialTownPlayer.getCurrentStep() instanceof WaitFinishTownHallStep waitFinishTownHallStep))
		{
			return;
		}
		
		waitFinishTownHallStep.completeStep();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTownHallInteraction(StructureInteractEvent event)
	{
		Structure structure = event.getStructure();
		
		if(!(structure instanceof TownHall)
				|| !(structure.getTown().getPluginPlayer() instanceof TutorialTownPlayer tutorialTownPlayer)
				|| !(tutorialTownPlayer.getCurrentStep() instanceof WaitFinishTownHallStep))
		{
			return;
		}
		
		event.setCancelled(false);
	}
}
