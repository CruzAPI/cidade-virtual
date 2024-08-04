package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.event.CommonPlayerRegisterEvent;
import com.eul4.event.AssistantRemoveEvent;
import com.eul4.event.AssistantSpawnEvent;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
public class AssistantTargetTaskListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onAssistantSpawn(AssistantSpawnEvent event)
	{
		event.getTown().scheduleAssistantTargetTaskIfPossible();
	}
	
	@EventHandler
	public void onAssistantRemove(AssistantRemoveEvent event)
	{
		event.getTown()
				.findAssistantTargetTask()
				.ifPresent(BukkitRunnable::cancel);
	}
	
	@EventHandler
	public void onTownPlayerRegister(CommonPlayerRegisterEvent event)
	{
		Optional.of(event.getCommonPlayer())
				.map(PluginPlayer.class::cast)
				.map(PluginPlayer::getTown)
				.ifPresent(Town::scheduleAssistantTargetTaskIfPossible);
	}
	
	@EventHandler
	public void onNonTownPlayerRegister(CommonPlayerRegisterEvent event)
	{
		Optional.of(event.getCommonPlayer())
				.map(PluginPlayer.class::cast)
				.map(PluginPlayer::getTown)
				.filter(not(Town::canScheduleAssistantTargetTask))
				.map(Town::getAssistantTargetTask)
				.ifPresent(BukkitRunnable::cancel);
	}
}
