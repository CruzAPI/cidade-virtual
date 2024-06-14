package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.hotbar.RaidSpectatorHotbar;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.RaidSpectator;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.type.player.SpiritualPlayerType;
import com.eul4.wrapper.TownAttack;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
public class CraftRaidSpectator extends CraftSpiritualPlayer implements RaidSpectator
{
	private final RaidSpectatorHotbar hotbar = new RaidSpectatorHotbar(this);
	
	public CraftRaidSpectator(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	public CraftRaidSpectator(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		hotbar.reset();
		
		player.teleport(getTown().getLocation().toHighestLocation());
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(true);
		player.setFlying(true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
	}
	
	@Override
	public SpiritualPlayerType getPlayerType()
	{
		return SpiritualPlayerType.RAID_SPECTATOR;
	}
	
	@Override
	public PluginPlayer reload()
	{
		if(!hasTown() || !getTown().isUnderAttack())
		{
			return (PluginPlayer) plugin.getPlayerManager().register(this, getReincarnationType());
		}
		
		reset();
		return this;
	}
	
	@Override
	public void defend()
	{
		if(!getTown().getCurrentAttack().canDefenderRespawn())
		{
			player.sendMessage("message"); //TODO create message
			return;
		}
		plugin.getPlayerManager().register(this, SpiritualPlayerType.DEFENDER);
	}
	
	@Override
	public void vanilla()
	{
		plugin.getPlayerManager().register(this, PhysicalPlayerType.VANILLA_PLAYER);
	}
	
	@Override
	public void onFinishingTownAttack()
	{
		RaidSpectator.super.onFinishingTownAttack();
	}
}
