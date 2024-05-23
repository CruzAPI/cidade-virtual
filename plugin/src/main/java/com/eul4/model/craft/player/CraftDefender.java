package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.Defender;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.town.Town;
import com.eul4.type.player.SpiritualPlayerType;
import com.eul4.wrapper.TownAttack;
import lombok.Getter;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Optional;

@Getter
public class CraftDefender extends CraftSpiritualPlayer implements Defender
{
	public CraftDefender(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	public CraftDefender(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
	}
	
	@Override
	public SpiritualPlayerType getPlayerType()
	{
		return SpiritualPlayerType.DEFENDER;
	}
	
	@Override
	public void reset()
	{
		super.reset();
		
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(false);
		teleportToTownHall();
		
		player.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F); //TODO: Pitch class
		
		if(getTown().getCurrentAttack().getDefenderDeathCount() > 0)
		{
			player.showTitle(getRespawnedTitle());
		}
		else
		{
			player.showTitle(getDefenderModeTitle());
		}
	}
	
	@Override
	public void onFinishingTownAttack()
	{
		Defender.super.onFinishingTownAttack();
	}
	
	@Override
	public PluginPlayer reload()
	{
		Town town = getTown();
		
		if(town == null)
		{
			return reincarnate();
		}
		
		TownAttack townAttack = town.getCurrentAttack();
		
		if(townAttack == null || townAttack.isFinished())
		{
			return reincarnate();
		}
		
		if(!townAttack.canDefenderRespawn())
		{
			return (PluginPlayer) plugin.getPlayerManager().register(this, SpiritualPlayerType.DEFENDER_SPECTATOR);
		}
		
		reset();
		return this;
	}
	
	@Override
	public PluginPlayer load()
	{
		if(player.isDead())
		{
			return this;
		}
		
		return super.load();
	}
	
	@Override
	public Optional<TownAttack> findTownAttack() //TODO: need to work after disk serialization
	{
		return Optional.ofNullable(getTown().getCurrentAttack());
	}
	
	private Title getRespawnedTitle()
	{
		return Title.title(
				PluginMessage.TITLE_RESPAWNED.translate(this),
				PluginMessage.SUBTITLE_RESPAWNED.translate(this),
				Title.Times.times(
						Duration.ZERO,
						Duration.ofMillis(1500L),
						Duration.ofMillis(2000L)));
	}
	
	private Title getDefenderModeTitle()
	{
		return Title.title(
				PluginMessage.TITLE_DEFENDER_MODE.translate(this),
				PluginMessage.SUBTITLE_RESPAWNED.translate(this),
				Title.Times.times(
						Duration.ZERO,
						Duration.ofMillis(1500L),
						Duration.ofMillis(2000L)));
	}
}
