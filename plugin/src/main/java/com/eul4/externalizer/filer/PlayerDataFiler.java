package com.eul4.externalizer.filer;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.Writers;
import com.eul4.common.util.FileUtil;
import com.eul4.externalizer.reader.GenericPluginPlayerReader;
import com.eul4.externalizer.writer.GenericPluginPlayerWriter;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

public class PlayerDataFiler extends PluginFiler
{
	private static final byte VERSION = 7;
	
	private static final ObjectType[]
	
	OBJECT_TYPES_V0 = new ObjectType[]
	{
		CommonObjectType.COMMON_PLAYER_DATA,
		CommonObjectType.COMMON_PLAYER,
		CommonObjectType.INVENTORY,
		CommonObjectType.ITEM_STACK,
		CommonObjectType.LOCATION,
		CommonObjectType.OBJECT,
		CommonObjectType.PLAYER_DATA,
		CommonObjectType.POTION_EFFECT_COLLECTION,
		CommonObjectType.POTION_EFFECT,
		PluginObjectType.ADMIN,
		PluginObjectType.ATTACKER,
		PluginObjectType.DEFENDER,
		PluginObjectType.GENERIC_PLUGIN_PLAYER,
		PluginObjectType.PLUGIN_PLAYER,
		PluginObjectType.PHYSICAL_PLAYER,
		PluginObjectType.SPIRITUAL_PLAYER,
		PluginObjectType.RAID_ANALYZER,
		PluginObjectType.RAID_SPECTATOR,
		PluginObjectType.TOWN_PLAYER_DATA,
		PluginObjectType.TOWN_PLAYER,
		PluginObjectType.VANILLA_PLAYER,
	},
	
	OBJECT_TYPES_V1 = new ObjectType[]
	{
		CommonObjectType.COMMON_PLAYER_DATA,
		CommonObjectType.COMMON_PLAYER,
		CommonObjectType.INVENTORY,
		CommonObjectType.ITEM_STACK,
		CommonObjectType.LOCATION,
		CommonObjectType.OBJECT,
		CommonObjectType.PLAYER_DATA,
		CommonObjectType.POTION_EFFECT_COLLECTION,
		CommonObjectType.POTION_EFFECT,
		PluginObjectType.ADMIN,
		PluginObjectType.ATTACKER,
		PluginObjectType.DEFENDER,
		PluginObjectType.GENERIC_PLUGIN_PLAYER,
		PluginObjectType.PLUGIN_PLAYER,
		PluginObjectType.PHYSICAL_PLAYER,
		PluginObjectType.SPAWN_PLAYER,
		PluginObjectType.SPIRITUAL_PLAYER,
		PluginObjectType.RAID_ANALYZER,
		PluginObjectType.RAID_SPECTATOR,
		PluginObjectType.TOWN_PLAYER_DATA,
		PluginObjectType.TOWN_PLAYER,
		PluginObjectType.VANILLA_PLAYER,
	},
	
	OBJECT_TYPES_V2 = new ObjectType[]
	{
		CommonObjectType.COMMON_PLAYER_DATA,
		CommonObjectType.COMMON_PLAYER,
		CommonObjectType.INVENTORY,
		CommonObjectType.ITEM_STACK,
		CommonObjectType.LOCATION,
		CommonObjectType.OBJECT,
		CommonObjectType.PLAYER_DATA,
		CommonObjectType.POTION_EFFECT_COLLECTION,
		CommonObjectType.POTION_EFFECT,
		PluginObjectType.ADMIN,
		PluginObjectType.ATTACKER,
		PluginObjectType.DEFENDER,
		PluginObjectType.GENERIC_PLUGIN_PLAYER,
		PluginObjectType.PLUGIN_PLAYER,
		PluginObjectType.PHYSICAL_PLAYER,
		PluginObjectType.SPAWN_PLAYER,
		PluginObjectType.SPIRITUAL_PLAYER,
		PluginObjectType.RAID_ANALYZER,
		PluginObjectType.RAID_SPECTATOR,
		PluginObjectType.TOWN_PLAYER_DATA,
		PluginObjectType.TOWN_PLAYER,
		PluginObjectType.TUTORIAL_TOWN_PLAYER,
		PluginObjectType.VANILLA_PLAYER,
	},
	
	OBJECT_TYPES_V3 = new ObjectType[]
	{
		CommonObjectType.COMMON_PLAYER_DATA,
		CommonObjectType.COMMON_PLAYER,
		CommonObjectType.INVENTORY,
		CommonObjectType.ITEM_STACK,
		CommonObjectType.LOCATION,
		CommonObjectType.OBJECT,
		CommonObjectType.PLAYER_DATA,
		CommonObjectType.POTION_EFFECT_COLLECTION,
		CommonObjectType.POTION_EFFECT,
		PluginObjectType.ADMIN,
		PluginObjectType.ATTACKER,
		PluginObjectType.CHECKPOINT_STEP_ENUM,
		PluginObjectType.DEFENDER,
		PluginObjectType.GENERIC_PLUGIN_PLAYER,
		PluginObjectType.PLUGIN_PLAYER,
		PluginObjectType.PHYSICAL_PLAYER,
		PluginObjectType.SPAWN_PLAYER,
		PluginObjectType.SPIRITUAL_PLAYER,
		PluginObjectType.RAID_ANALYZER,
		PluginObjectType.RAID_SPECTATOR,
		PluginObjectType.TOWN_PLAYER_DATA,
		PluginObjectType.TOWN_PLAYER,
		PluginObjectType.TUTORIAL_TOWN_PLAYER_DATA,
		PluginObjectType.TUTORIAL_TOWN_PLAYER,
		PluginObjectType.VANILLA_PLAYER,
	},
	
	OBJECT_TYPES_V4 = new ObjectType[]
	{
		CommonObjectType.COMMON_PLAYER_DATA,
		CommonObjectType.COMMON_PLAYER,
		CommonObjectType.INVENTORY,
		CommonObjectType.ITEM_STACK,
		CommonObjectType.LOCATION,
		CommonObjectType.OBJECT,
		CommonObjectType.PLAYER_DATA,
		CommonObjectType.POTION_EFFECT_COLLECTION,
		CommonObjectType.POTION_EFFECT,
		PluginObjectType.ADMIN,
		PluginObjectType.ATTACKER,
		PluginObjectType.CHECKPOINT_STEP_ENUM,
		PluginObjectType.DEFENDER,
		PluginObjectType.GENERIC_PLUGIN_PLAYER,
		PluginObjectType.HOME_MAP,
		PluginObjectType.PLUGIN_PLAYER,
		PluginObjectType.PHYSICAL_PLAYER,
		PluginObjectType.SPAWN_PLAYER,
		PluginObjectType.SPIRITUAL_PLAYER,
		PluginObjectType.RAID_ANALYZER,
		PluginObjectType.RAID_SPECTATOR,
		PluginObjectType.TOWN_PLAYER_DATA,
		PluginObjectType.TOWN_PLAYER,
		PluginObjectType.TUTORIAL_TOWN_PLAYER_DATA,
		PluginObjectType.TUTORIAL_TOWN_PLAYER,
		PluginObjectType.VANILLA_PLAYER_DATA,
		PluginObjectType.VANILLA_PLAYER,
	},
	
	OBJECT_TYPES_V5 = new ObjectType[]
	{
		CommonObjectType.COMMON_PLAYER_DATA,
		CommonObjectType.COMMON_PLAYER,
		CommonObjectType.INVENTORY,
		CommonObjectType.ITEM_STACK,
		CommonObjectType.LOCATION,
		CommonObjectType.OBJECT,
		CommonObjectType.PLAYER_DATA,
		CommonObjectType.POTION_EFFECT_COLLECTION,
		CommonObjectType.POTION_EFFECT,
		PluginObjectType.ADMIN,
		PluginObjectType.ATTACKER,
		PluginObjectType.CHECKPOINT_STEP_ENUM,
		PluginObjectType.DEFENDER,
		PluginObjectType.GENERIC_PLUGIN_PLAYER,
		PluginObjectType.HOME_MAP,
		PluginObjectType.PLUGIN_PLAYER,
		PluginObjectType.PLUGIN_PLAYER_DATA,
		PluginObjectType.PHYSICAL_PLAYER,
		PluginObjectType.SPAWN_PLAYER,
		PluginObjectType.SPIRITUAL_PLAYER,
		PluginObjectType.RAID_ANALYZER,
		PluginObjectType.RAID_SPECTATOR,
		PluginObjectType.TAG,
		PluginObjectType.TOWN_PLAYER_DATA,
		PluginObjectType.TOWN_PLAYER,
		PluginObjectType.TUTORIAL_TOWN_PLAYER_DATA,
		PluginObjectType.TUTORIAL_TOWN_PLAYER,
		PluginObjectType.VANILLA_PLAYER_DATA,
		PluginObjectType.VANILLA_PLAYER,
	},
	
	OBJECT_TYPES_V6 = new ObjectType[]
	{
		CommonObjectType.COMMON_PLAYER_DATA,
		CommonObjectType.COMMON_PLAYER,
		CommonObjectType.INVENTORY,
		CommonObjectType.ITEM_STACK,
		CommonObjectType.LOCATION,
		CommonObjectType.OBJECT,
		CommonObjectType.PLAYER_DATA,
		CommonObjectType.POTION_EFFECT_COLLECTION,
		CommonObjectType.POTION_EFFECT,
		CommonObjectType.UUID,
		CommonObjectType.UUID_HASH_SET,
		PluginObjectType.ADMIN,
		PluginObjectType.ATTACKER,
		PluginObjectType.CHECKPOINT_STEP_ENUM,
		PluginObjectType.DEFENDER,
		PluginObjectType.GENERIC_PLUGIN_PLAYER,
		PluginObjectType.HOME_MAP,
		PluginObjectType.PLUGIN_PLAYER,
		PluginObjectType.PLUGIN_PLAYER_DATA,
		PluginObjectType.PHYSICAL_PLAYER,
		PluginObjectType.SPAWN_PLAYER,
		PluginObjectType.SPIRITUAL_PLAYER,
		PluginObjectType.RAID_ANALYZER,
		PluginObjectType.RAID_SPECTATOR,
		PluginObjectType.TAG,
		PluginObjectType.TOWN_PLAYER_DATA,
		PluginObjectType.TOWN_PLAYER,
		PluginObjectType.TUTORIAL_TOWN_PLAYER_DATA,
		PluginObjectType.TUTORIAL_TOWN_PLAYER,
		PluginObjectType.VANILLA_PLAYER_DATA,
		PluginObjectType.VANILLA_PLAYER,
	},
	
	OBJECT_TYPES_V7 = new ObjectType[]
	{
		CommonObjectType.COMMON_PLAYER_DATA,
		CommonObjectType.COMMON_PLAYER,
		CommonObjectType.INVENTORY,
		CommonObjectType.ITEM_STACK,
		CommonObjectType.LOCATION,
		CommonObjectType.OBJECT,
		CommonObjectType.PLAYER_DATA,
		CommonObjectType.POTION_EFFECT_COLLECTION,
		CommonObjectType.POTION_EFFECT,
		CommonObjectType.UUID,
		CommonObjectType.UUID_HASH_SET,
		PluginObjectType.ADMIN,
		PluginObjectType.ATTACKER,
		PluginObjectType.BROADCAST_HASH_SET,
		PluginObjectType.CHECKPOINT_STEP_ENUM,
		PluginObjectType.DEFENDER,
		PluginObjectType.GENERIC_PLUGIN_PLAYER,
		PluginObjectType.HOME_MAP,
		PluginObjectType.PLUGIN_PLAYER,
		PluginObjectType.PLUGIN_PLAYER_DATA,
		PluginObjectType.PHYSICAL_PLAYER,
		PluginObjectType.SPAWN_PLAYER,
		PluginObjectType.SPIRITUAL_PLAYER,
		PluginObjectType.RAID_ANALYZER,
		PluginObjectType.RAID_SPECTATOR,
		PluginObjectType.TAG,
		PluginObjectType.TOWN_PLAYER_DATA,
		PluginObjectType.TOWN_PLAYER,
		PluginObjectType.TUTORIAL_TOWN_PLAYER_DATA,
		PluginObjectType.TUTORIAL_TOWN_PLAYER,
		PluginObjectType.VANILLA_PLAYER_DATA,
		PluginObjectType.VANILLA_PLAYER,
	};
	
	@Getter
	private final Map<UUID, PluginPlayer> memoryPlayers = new HashMap<>();
	
	public PlayerDataFiler(Main plugin)
	{
		super(plugin, VERSION);
	}
	
	public void saveMemoryPlayers()
	{
		final Iterator<PluginPlayer> iterator = memoryPlayers.values().iterator();
		
		while(iterator.hasNext())
		{
			PluginPlayer pluginPlayer = iterator.next();
			
			save(pluginPlayer);
			
			if(!plugin.getPlayerManager().isRegistered(pluginPlayer))
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
			
			try
			(
				FileOutputStream fileOut = new FileOutputStream(tmp);
				BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut);
				DataOutputStream out = new DataOutputStream(bufferedOut)
			)
			{
				Writers.of(plugin, out, writeVersions(out))
						.getWriter(GenericPluginPlayerWriter.class)
						.writeReference(pluginPlayer);
				out.flush();
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
					pluginPlayer.getInterfaceType().getSimpleName()));
		}
		
		return pluginPlayer;
	}
	
	private PluginPlayer logLoadFromDisk(PluginPlayer pluginPlayer, Player player)
	{
		if(pluginPlayer != null)
		{
			plugin.getLogger().info(MessageFormat.format("{0} data loaded from disk: {1}",
					pluginPlayer.getPlayer().getName(),
					pluginPlayer.getInterfaceType().getSimpleName()));
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
		
		try
		(
			FileInputStream fileIn = new FileInputStream(file);
			FileChannel fileChannel = fileIn.getChannel()
		)
		{
			byte[] header = new byte[2];
			fileIn.read(header);
			
			fileChannel.position(0L);
			
			try(BufferedInputStream bufferedIn = new BufferedInputStream(fileIn))
			{
				if(isObjectStream(header))
				{
					try(ObjectInputStream in = new ObjectInputStream(bufferedIn))
					{
						PluginPlayer pluginPlayer = Readers.of(plugin, in, readVersions(in))
								.getReader(GenericPluginPlayerReader.class)
								.readReference(player, plugin);
						plugin.getLogger().info(MessageFormat.format("(ObjectStream) {0} data loaded: {1}", player.getName(), pluginPlayer));
						return pluginPlayer;
					}
				}
				else
				{
					try(DataInputStream in = new DataInputStream(bufferedIn))
					{
						PluginPlayer pluginPlayer = Readers.of(plugin, in, readVersions(in))
								.getReader(GenericPluginPlayerReader.class)
								.readReference(player,plugin);
						plugin.getLogger().info(MessageFormat.format("(DataStream) {0} data loaded: {1}", player.getName(), pluginPlayer));
						return pluginPlayer;
					}
				}
			}
		}
		catch(Exception e)
		{
			plugin.getLogger().log(Level.SEVERE, MessageFormat.format("Failed to load {0} player_data. uuid={1}",
					player.getName(),
					player.getUniqueId()), e);
			return null;
		}
	}
	
	@Override
	protected ObjectType[] getObjectTypes(byte version) throws InvalidVersionException
	{
		return switch(version)
		{
			case 0 -> OBJECT_TYPES_V0;
			case 1 -> OBJECT_TYPES_V1;
			case 2 -> OBJECT_TYPES_V2;
			case 3 -> OBJECT_TYPES_V3;
			case 4 -> OBJECT_TYPES_V4;
			case 5 -> OBJECT_TYPES_V5;
			case 6 -> OBJECT_TYPES_V6;
			case 7 -> OBJECT_TYPES_V7;
			default -> throw new InvalidVersionException(MessageFormat.format(
					"Invalid {0} version: {1}",
					getClass().getSimpleName(),
					version));
		};
	}
}
