package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.externalizer.writer.SpiritualPlayerWriter;
import com.eul4.hotbar.RaidAnalyzerHotbar;
import com.eul4.hotbar.RaidSpectatorHotbar;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.RaidAnalyzer;
import com.eul4.model.player.RaidSpectator;
import com.eul4.model.town.Town;
import com.eul4.type.player.PluginPlayerType;
import com.eul4.type.player.SpiritualPlayerType;
import com.eul4.util.MessageUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.function.Predicate.not;

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
	public boolean mustSavePlayerData()
	{
		return false;
	}
	
	@Override
	public SpiritualPlayerType getPlayerType()
	{
		return SpiritualPlayerType.RAID_SPECTATOR;
	}
	
	@Override
	public void defend()
	{
		//TODO: Convert to Defender
	}
	
	@Override
	public void vanilla()
	{
		//TODO: Convert to VanillaPlayer
	}
}
