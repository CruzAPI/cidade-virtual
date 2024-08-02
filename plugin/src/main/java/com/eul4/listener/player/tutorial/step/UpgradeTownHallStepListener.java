package com.eul4.listener.player.tutorial.step;

import com.eul4.Main;
import com.eul4.event.StructureInteractEvent;
import com.eul4.event.StructureUpgradeEvent;
import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.UpgradeTownHallStep;
import com.eul4.model.town.structure.Structure;
import com.eul4.model.town.structure.TownHall;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class UpgradeTownHallStepListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onUpgradeTownHall(StructureUpgradeEvent event)
	{
		Structure structure = event.getStructure();
		Player player = structure.getTown().getPlayer();
		
		if(player == null
				|| !(structure instanceof TownHall)
				|| !(plugin.getPlayerManager().get(player) instanceof TutorialTownPlayer tutorialTownPlayer)
				|| !(tutorialTownPlayer.getCurrentStep() instanceof UpgradeTownHallStep upgradeTownHallStep))
		{
			return;
		}
		
		upgradeTownHallStep.completeStep();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTownHallInteraction(StructureInteractEvent event)
	{
		Structure structure = event.getStructure();
		
		if(!(structure instanceof TownHall)
				|| !(structure.getTown().getPluginPlayer() instanceof TutorialTownPlayer tutorialTownPlayer)
				|| !(tutorialTownPlayer.getCurrentStep() instanceof UpgradeTownHallStep))
		{
			return;
		}
		
		event.setCancelled(false);
	}
}
