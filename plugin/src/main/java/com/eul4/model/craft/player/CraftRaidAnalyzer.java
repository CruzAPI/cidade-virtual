package com.eul4.model.craft.player;

import com.eul4.hotbar.RaidAnalyzerHotbar;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.RaidAnalyzer;
import com.eul4.model.town.Town;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
public class CraftRaidAnalyzer extends CraftPluginPlayer implements RaidAnalyzer
{
	private final RaidAnalyzerHotbar hotbar = new RaidAnalyzerHotbar(this);
	
	private Town analyzingTown;
	
	public CraftRaidAnalyzer(PluginPlayer pluginPlayer)
	{
		super(pluginPlayer);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		hotbar.reset();
		
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(true);
		player.setFlying(true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
	}
	
	@Override
	public void analyzeTown(Town town)
	{
		analyzingTown = town;
		
		hotbar.reset();
		player.teleport(town.getLocation().toHighestLocation());
	}
	
	@Override
	public void attack()
	{
		//TODO: convert to RaidPlayer
		Bukkit.broadcastMessage("attak!");
	}
	
	@Override
	public void reroll()
	{
		Bukkit.broadcastMessage("reroll!");
	}
	
	@Override
	public void cancel()
	{
		Bukkit.broadcastMessage("cancel!");
	}
}
