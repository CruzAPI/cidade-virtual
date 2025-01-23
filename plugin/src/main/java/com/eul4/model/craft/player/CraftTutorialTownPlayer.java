package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.common.wrapper.Pitch;
import com.eul4.i18n.TutorialTownMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.physical.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.CheckpointStepEnum;
import com.eul4.model.player.tutorial.step.Step;
import com.eul4.type.player.PhysicalPlayerType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class CraftTutorialTownPlayer extends CraftTownPlayer implements TutorialTownPlayer
{
	private transient Step step;
	private transient TutorialTownPlayerTask tutorialTownPlayerTask;
	
	public CraftTutorialTownPlayer(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	public CraftTutorialTownPlayer(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		clearChat();
		
		if(hasTown())
		{
			scheduleCheckpointStep();
			scheduleTutorialTownPlayerTask();
		}
	}
	
	private void cancelCurrentStep()
	{
		Optional.ofNullable(step).ifPresent(Step::cancel);
	}
	
	public void scheduleTutorialTownPlayerTask()
	{
		Optional.ofNullable(tutorialTownPlayerTask).ifPresent(BukkitRunnable::cancel);
		
		tutorialTownPlayerTask = new TutorialTownPlayerTask();
		tutorialTownPlayerTask.runTaskTimer(plugin, 0L, 5L * 20L);
	}
	
	@Override
	public void scheduleStep(Step step)
	{
		cancelCurrentStep();
		this.step = step;
		tutorialTownPlayerData.setCheckpointStep(step.getCheckpointStep());
		step.runTaskTimer(plugin, 0L, 1L);
	}
	
	@Override
	public boolean mustSavePlayerData()
	{
		return true;
	}
	
	@Override
	public PhysicalPlayerType getPlayerType()
	{
		return PhysicalPlayerType.TUTORIAL_TOWN_PLAYER;
	}
	
	@Override
	public PluginPlayer load()
	{
		return super.load();
	}
	
	@Override
	public PluginPlayer reload()
	{
		return player.isOp() ? finishTutorial() : super.reload();
	}
	
	@Override
	public void invalidate()
	{
		cancelCurrentStep();
		Optional.ofNullable(tutorialTownPlayerTask).ifPresent(BukkitRunnable::cancel);
	}
	
	@Override
	public Step getCurrentStep()
	{
		return step;
	}
	
	@Override
	public PluginPlayer finishTutorial()
	{
		getTown().setFinishedTutorial(true);
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().register(this, PhysicalPlayerType.TOWN_PLAYER);
		
		pluginPlayer.clearChat();
		pluginPlayer.sendMessage(TutorialTownMessage.TUTORIAL_FINISHED);
		
		Player player = pluginPlayer.getPlayer();
		
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, Pitch.min());
		
		return pluginPlayer;
	}
	
	private void scheduleCheckpointStep()
	{
		Step checkpointStep = getCheckpointStep().newStep(this);
		scheduleStep(checkpointStep);
	}
	
	private CheckpointStepEnum getCheckpointStep()
	{
		return Optional
				.ofNullable(tutorialTownPlayerData.getCheckpointStep())
				.orElse(CheckpointStepEnum.STEP_1);
	}
	
	private class TutorialTownPlayerTask extends BukkitRunnable
	{
		@Override
		public void run()
		{
			if(hasTown() && !getTown().isInside(player.getLocation()))
			{
				teleportToTownHall();
			}
		}
	}
}
