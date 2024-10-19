package com.eul4.listener.player.tutorial.step;

import com.eul4.Main;
import com.eul4.event.LikeGeneratorCollectEvent;
import com.eul4.event.StructureInteractEvent;
import com.eul4.model.player.physical.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CollectLikesStep;
import com.eul4.model.town.structure.LikeGenerator;
import com.eul4.model.town.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class CollectLikesStepListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onLikeGeneratorCollect(LikeGeneratorCollectEvent event)
	{
		Player player = event.getTown().getPlayer();
		
		if(player == null
				|| !(plugin.getPlayerManager().get(player) instanceof TutorialTownPlayer tutorialTownPlayer)
				|| !(tutorialTownPlayer.getCurrentStep() instanceof CollectLikesStep collectLikesStep))
		{
			return;
		}
		
		event.setSendMessage(false);
		
		collectLikesStep.setCollectedAmount(event.getAmountCollected());
		collectLikesStep.completeStep();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLikeGeneratorInteraction(StructureInteractEvent event)
	{
		Structure structure = event.getStructure();
		
		if(!(structure instanceof LikeGenerator)
				|| !(structure.getTown().getPluginPlayer() instanceof TutorialTownPlayer tutorialTownPlayer)
				|| !(tutorialTownPlayer.getCurrentStep() instanceof CollectLikesStep))
		{
			return;
		}
		
		event.setCancelled(false);
	}
}
