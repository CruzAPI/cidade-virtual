package com.eul4.listener.player.tutorial.step;

import com.eul4.Main;
import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.AssistantStep;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@RequiredArgsConstructor
public class AssistantStepListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onAssistantClick(PlayerInteractEntityEvent event)
	{
		Player player = event.getPlayer();
		
		if(!(plugin.getPlayerManager().get(player) instanceof TutorialTownPlayer tutorialTownPlayer)
				|| !(tutorialTownPlayer.getCurrentStep() instanceof AssistantStep assistantStep))
		{
			return;
		}
		
		if(event.getRightClicked() == assistantStep.getAssistant())
		{
			assistantStep.completeStep();
		}
	}
}
