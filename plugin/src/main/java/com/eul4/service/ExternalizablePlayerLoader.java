package com.eul4.service;

import com.eul4.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.player.PluginPlayer;
import com.eul4.util.FileUtil;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

@RequiredArgsConstructor
public class ExternalizablePlayerLoader
{
	private final Main plugin;
	
	@Getter
	private final Map<UUID, PluginPlayer> unsavedPlayers = new HashMap<>();
	
	public void savePlayers()
	{
		final Iterator<PluginPlayer> iterator = unsavedPlayers.values().iterator();
		
		while(iterator.hasNext())
		{
			savePlayer(iterator);
		}
	}
	
	private void savePlayer(Iterator<PluginPlayer> iterator)
	{
		PluginPlayer pluginPlayer = iterator.next();
		
		savePlayer(pluginPlayer);
		
		if(!plugin.getPlayerManager().isValid(pluginPlayer))
		{
			iterator.remove();
		}
	}
	
	private void savePlayer(PluginPlayer pluginPlayer)
	{
		File tmp = null;
		
		try
		{
			File file = plugin.getDataFileManager().createPlayerDataFile(pluginPlayer.getUniqueId());
			tmp = new File(file.getParent(), "." + file.getName() + ".tmp");
			
			savePlayer(pluginPlayer, tmp);
			
			if(tmp.renameTo(file))
			{
				plugin.getLogger().info(MessageFormat.format("{0} player_data saved! uuid={1} length={2}",
						pluginPlayer.getPlayer().getName(),
						pluginPlayer.getUniqueId(),
						file.length()));
			}
			else
			{
				throw new IOException("Failed to replace the old player_data file with the new one.");
			}
		}
		catch(Exception e)
		{
			plugin.getLogger().log(Level.SEVERE, "Failed to create player_data file. uuid=" + pluginPlayer.getUniqueId(), e);
		}
		finally
		{
			FileUtil.deleteTempFile(tmp, plugin.getLogger());
		}
	}
	
	private void savePlayer(PluginPlayer pluginPlayer, File file) throws IOException
	{
		try(FileOutputStream fileOutputStream = new FileOutputStream(file);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream))
		{
			plugin.getPluginPlayerExternalizer().write(pluginPlayer, out);
			out.flush();
			fileOutputStream.write(byteArrayOutputStream.toByteArray());
		}
	}
	
	public PluginPlayer loadPlayer(Player player)
	{
		final File file = plugin.getDataFileManager().getPlayerDataFile(player.getUniqueId());
		
		if(!file.exists() || file.length() == 0L)
		{
			return null;
		}
		
		try(FileInputStream fileInputStream = new FileInputStream(file);
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ByteStreams.toByteArray(fileInputStream));
				ObjectInputStream in = new ObjectInputStream(byteArrayInputStream))
		{
			PluginPlayer pluginPlayer = plugin.getPluginPlayerExternalizer().read(player, in);
			plugin.getLogger().info(player.getName() + " player_data loaded!");
			return pluginPlayer;
		}
		catch(Exception e)
		{
			plugin.getLogger().log(Level.SEVERE, MessageFormat.format("Failed to load {0} player_data. uuid={1}",
					player.getName(),
					player.getUniqueId()), e);
		}
		
		return null;
	}
}
