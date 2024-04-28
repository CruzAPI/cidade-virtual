package com.eul4.model.craft.player;

import com.eul4.hotbar.RaidAnalyzerHotbar;
import com.eul4.model.player.Admin;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.RaidAnalyzer;
import com.eul4.model.town.Town;
import com.eul4.type.player.PluginCommonPlayerType;
import com.eul4.type.player.PluginPlayerType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;

@Getter
public class CraftRaidAnalyzer extends CraftPluginPlayer implements RaidAnalyzer
{
	private final RaidAnalyzerHotbar hotbar = new RaidAnalyzerHotbar(this);
	private final Set<Town> skippedTowns = new HashSet<>();
	private final PluginCommonPlayerType.Type lastLoadableCommonPlayerType;

	private Town analyzingTown;
	
	public CraftRaidAnalyzer(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
		
		lastLoadableCommonPlayerType = pluginPlayer.getLastLoadableCommonPlayerType();
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
	
	@Override
	public PluginCommonPlayerType<RaidAnalyzer> getCommonPlayerType()
	{
		return PluginCommonPlayerType.RAID_ANALYZER;
	}
	
	@Override
	public PluginCommonPlayerType.Type getLastLoadableCommonPlayerType()
	{
		return lastLoadableCommonPlayerType;
	}
}
