package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.hotbar.DefenderSpectatorHotbar;
import com.eul4.model.player.spiritual.DefenderSpectator;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.type.player.SpiritualPlayerType;
import com.eul4.wrapper.TownAttack;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
public class CraftDefenderSpectator extends CraftSpiritualPlayer implements DefenderSpectator
{
	private final DefenderSpectatorHotbar hotbar = new DefenderSpectatorHotbar(this);
	
	public CraftDefenderSpectator(Player player, Main plugin)
	{
		super(player, plugin);
	}
	
	public CraftDefenderSpectator(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		hotbar.reset();
		
		teleportToHighestTownHall();
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(true);
		player.setFlying(true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
	}
	
	@Override
	public SpiritualPlayerType getPlayerType()
	{
		return SpiritualPlayerType.DEFENDER_SPECTATOR;
	}
	
	@Override
	public PluginPlayer reload()
	{
		if(!hasTown() || !getTown().isUnderAttack())
		{
			return (PluginPlayer) plugin.getPlayerManager().register(this, PhysicalPlayerType.VANILLA_PLAYER);
		}
		
		TownAttack townAttack = getTown().getCurrentAttack();

		if(townAttack.isNotDefenderRespawnInCooldown())
		{
			return (PluginPlayer) plugin.getPlayerManager().register(this, SpiritualPlayerType.DEFENDER);
		}

		reset();
		return this;
	}
	
	@Override
	public void onFinishingTownAttack()
	{
		DefenderSpectator.super.onFinishingTownAttack();
	}
	
	@Override
	public DefenderSpectatorHotbar getHotbar()
	{
		return hotbar;
	}
	
	@Override
	public void quit()
	{
		plugin.getPlayerManager().register(this, SpiritualPlayerType.RAID_SPECTATOR);
	}
	
	@Override
	public void respawn()
	{
		plugin.getPlayerManager().register(this, SpiritualPlayerType.DEFENDER);
	}
}
