package com.eul4.listener.player.tutorial.step;

import com.eul4.Main;
import com.eul4.event.DislikeGeneratorCollectEvent;
import com.eul4.event.StructureInteractEvent;
import com.eul4.model.player.physical.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CollectDislikesStep;
import com.eul4.model.town.structure.DislikeGenerator;
import com.eul4.model.town.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class CollectDislikesStepListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onDislikeGeneratorCollect(DislikeGeneratorCollectEvent event)
	{
		Player player = event.getTown().getPlayer();
		
		if(player == null
				|| !(plugin.getPlayerManager().get(player) instanceof TutorialTownPlayer tutorialTownPlayer)
				|| !(tutorialTownPlayer.getCurrentStep() instanceof CollectDislikesStep collectDislikesStep))
		{
			return;
		}
		
		event.setSendMessage(false);
		
		collectDislikesStep.setCollectedAmount(event.getAmountCollected());
		collectDislikesStep.completeStep();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDislikeGeneratorInteraction(StructureInteractEvent event)
	{
		Structure structure = event.getStructure();
		
		if(!(structure instanceof DislikeGenerator)
				|| !(structure.getTown().getPluginPlayer() instanceof TutorialTownPlayer tutorialTownPlayer)
				|| !(tutorialTownPlayer.getCurrentStep() instanceof CollectDislikesStep))
		{
			return;
		}
		
		event.setCancelled(false);
	}
}
