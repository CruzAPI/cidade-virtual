package com.eul4.util;

import com.eul4.common.wrapper.Pitch;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SoundUtil
{
	public static void playPlongIfPlayer(Entity entity)
	{
		if(entity instanceof Player player)
		{
			playPlong(player);
		}
	}
	
	public static void playPlong(Player player)
	{
		player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, Pitch.getPitch(3));
	}
	
	public static void playPlingIfPlayer(Entity entity)
	{
		if(entity instanceof Player player)
		{
			playPling(player);
		}
	}
	
	public static void playPling(Player player)
	{
		player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, Pitch.max());
	}
	
	public static void playPlingPlong(Player player, Plugin plugin)
	{
		player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, Pitch.Db2);
		plugin.getServer().getScheduler().runTaskLater(plugin,
				() -> player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, Pitch.Gb2), 2L);
	}
	
	public static void playPiano(Player player)
	{
		player.playSound(player, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1.0F, Pitch.Eb1);
	}
}
