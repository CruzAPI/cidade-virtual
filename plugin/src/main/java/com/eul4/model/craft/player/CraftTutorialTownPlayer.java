package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.common.wrapper.Pitch;
import com.eul4.i18n.TutorialTownMessage;
import com.eul4.model.craft.player.tutorial.step.CraftStep1;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.TutorialTownPlayer;
import com.eul4.model.player.tutorial.step.Step;
import com.eul4.type.player.PhysicalPlayerType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Optional;

public class CraftTutorialTownPlayer extends CraftTownPlayer implements TutorialTownPlayer
{
	private Step step;
	
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
		
		scheduleStep(new CraftStep1(this));
	}
	
	private void cancelCurrentStep()
	{
		Optional.ofNullable(step).ifPresent(Step::cancel);
	}
	
	@Override
	public void scheduleStep(Step step)
	{
		cancelCurrentStep();
		this.step = step;
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
		return super.reload();
	}
	
	@Override
	public void invalidate()
	{
		cancelCurrentStep();
	}
	
	@Override
	public Step getCurrentStep()
	{
		return step;
	}
	
	@Override
	public PluginPlayer finishTutorial()
	{
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().register(this, PhysicalPlayerType.TOWN_PLAYER);
		
		pluginPlayer.clearChat();
		pluginPlayer.sendMessage(TutorialTownMessage.TUTORIAL_FINISHED);
		
		Player player = pluginPlayer.getPlayer();
		
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, Pitch.min());
		
		return pluginPlayer;
	}
}
