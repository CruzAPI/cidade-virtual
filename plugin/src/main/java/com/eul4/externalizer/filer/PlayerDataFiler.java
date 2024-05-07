package com.eul4.externalizer.filer;

import com.eul4.Main;
import com.eul4.Versions;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.externalizer.reader.GenericPluginPlayerReader;
import com.eul4.externalizer.writer.GenericPluginPlayerWriter;
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
public class PlayerDataFiler
{
	private static final byte VERSION = 0;
	
	private final Main plugin;
	
	@Getter
	private final Map<UUID, PluginPlayer> memoryPlayers = new HashMap<>();
	
	public void saveMemoryPlayers()
	{
		final Iterator<PluginPlayer> iterator = memoryPlayers.values().iterator();
		
		while(iterator.hasNext())
		{
			PluginPlayer pluginPlayer = iterator.next();
			
			save(pluginPlayer);
			
			if(!plugin.getPlayerManager().isValid(pluginPlayer))
			{
				iterator.remove();
			}
		}
	}
	
	private void save(PluginPlayer pluginPlayer)
	{
		pluginPlayer.savePlayerData();
		File tmp = null;
		
		try
		{
			File file = plugin.getDataFileManager().createPlayerDataFile(pluginPlayer.getUniqueId());
			tmp = new File(file.getParent(), "." + file.getName() + ".tmp");
			
			try(FileOutputStream fileOutputStream = new FileOutputStream(tmp);
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream))
			{
				writeVersions(out);
				new GenericPluginPlayerWriter(out).writeReference(pluginPlayer);
				out.flush();
				fileOutputStream.write(byteArrayOutputStream.toByteArray());
			}
			
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
	
	public PluginPlayer loadFromMemoryOrDiskLogging(Player player)
	{
		return Optional.ofNullable(loadFromMemoryLogging(player)).orElseGet(() -> loadFromDiskLogging(player));
	}
	
	public PluginPlayer loadFromMemoryLogging(Player player)
	{
		return logLoadFromMemory(loadFromMemory(player));
	}
	
	public PluginPlayer loadFromMemory(Player player)
	{
		return memoryPlayers.get(player.getUniqueId());
	}
	
	private PluginPlayer logLoadFromMemory(PluginPlayer pluginPlayer)
	{
		if(pluginPlayer != null)
		{
			plugin.getLogger().info(MessageFormat.format("{0} data loaded from memory: {1}",
					pluginPlayer.getPlayer().getName(),
					pluginPlayer.getType().getSimpleName()));
		}
		
		return pluginPlayer;
	}
	
	private PluginPlayer logLoadFromDisk(PluginPlayer pluginPlayer, Player player)
	{
		if(pluginPlayer != null)
		{
			plugin.getLogger().info(MessageFormat.format("{0} data loaded from disk: {1}",
					pluginPlayer.getPlayer().getName(),
					pluginPlayer.getType().getSimpleName()));
		}
		else
		{
			plugin.getLogger().info(MessageFormat.format("{0} data not found in disk!",
					player.getName()));
		}
		
		return pluginPlayer;
	}
	
	public PluginPlayer loadFromDiskLogging(Player player)
	{
		return logLoadFromDisk(loadFromDisk(player), player);
	}
	
	public PluginPlayer loadFromDisk(Player player)
	{
		final File file = plugin.getDataFileManager().getPlayerDataFile(player.getUniqueId());
		
		if(!file.exists() || file.length() == 0L)
		{
			plugin.getLogger().warning(MessageFormat.format("{0} data file is empty or not exists!", player.getName()));
			return null;
		}
		
		try(FileInputStream fileInputStream = new FileInputStream(file);
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ByteStreams.toByteArray(fileInputStream));
				ObjectInputStream in = new ObjectInputStream(byteArrayInputStream))
		{
			PluginPlayer pluginPlayer = new GenericPluginPlayerReader(in, readVersions(in), player, plugin).readReference();
			plugin.getLogger().info(MessageFormat.format("{0} data loaded: {1}", player.getName(), pluginPlayer));
			return pluginPlayer;
		}
		catch(Exception e)
		{
			plugin.getLogger().log(Level.SEVERE, MessageFormat.format("Failed to load {0} player_data. uuid={1}",
					player.getName(),
					player.getUniqueId()), e);
			return null;
		}
	}
	
	private Versions readVersions(ObjectInput in) throws InvalidVersionException, IOException
	{
		byte version = in.readByte();
		
		if(version == 0)
		{
			return Versions.builder()
					.commonPlayerDataVersion(in.readByte())
					.commonPlayerVersion(in.readByte())
					.inventoryVersion(in.readByte())
					.itemStackVersion(in.readByte())
					.locationVersion(in.readByte())
					.objectVersion(in.readByte())
					.playerDataVersion(in.readByte())
					.potionEffectCollectionVersion(in.readByte())
					.potionEffectVersion(in.readByte())
					.adminVersion(in.readByte())
					.attackerVersion(in.readByte())
					.genericPluginPlayerVersion(in.readByte())
					.pluginPlayerVersion(in.readByte())
					.raidAnalyzerVersion(in.readByte())
					.townPlayerDataVersion(in.readByte())
					.townPlayerVersion(in.readByte())
					.build();
		}
		
		throw new InvalidVersionException("Invalid PlayerDataFiler version: " + version);
	}
	
	private void writeVersions(ObjectOutput out) throws IOException
	{
		out.writeByte(VERSION);
		
		out.writeByte(Versions.COMMON_PLAYER_DATA);
		out.writeByte(Versions.COMMON_PLAYER);
		out.writeByte(Versions.INVENTORY);
		out.writeByte(Versions.ITEM_STACK);
		out.writeByte(Versions.LOCATION);
		out.writeByte(Versions.OBJECT);
		out.writeByte(Versions.PLAYER_DATA);
		out.writeByte(Versions.POTION_EFFECT_COLLECTION);
		out.writeByte(Versions.POTION_EFFECT);
		out.writeByte(Versions.ADMIN);
		out.writeByte(Versions.ATTACKER);
		out.writeByte(Versions.GENERIC_PLUGIN_PLAYER);
		out.writeByte(Versions.PLUGIN_PLAYER);
		out.writeByte(Versions.RAID_ANALYZER);
		out.writeByte(Versions.TOWN_PLAYER_DATA);
		out.writeByte(Versions.TOWN_PLAYER);
	}
}
