package com.eul4.listener.player.tutorial.step;

import com.eul4.Main;
import com.eul4.event.AssistantInteractEvent;
import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.AssistantStep;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

@RequiredArgsConstructor
public class AssistantStepListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onAssistantInteraction(AssistantInteractEvent event)
	{
		if(!(event.getPluginPlayer() instanceof TutorialTownPlayer tutorialTownPlayer)
				|| !(tutorialTownPlayer.getCurrentStep() instanceof AssistantStep assistantStep))
		{
			return;
		}
		
		if(event.getTown() == tutorialTownPlayer.getTown())
		{
			assistantStep.completeStep();
		}
	}
}
