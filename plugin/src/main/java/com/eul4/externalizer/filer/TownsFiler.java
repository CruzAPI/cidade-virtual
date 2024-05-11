package com.eul4.externalizer.filer;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.Writers;
import com.eul4.externalizer.reader.TownMapReader;
import com.eul4.externalizer.writer.TownMapWriter;
import com.eul4.model.town.Town;
import com.eul4.type.player.PluginObjectType;
import com.eul4.util.FileUtil;
import com.google.common.io.ByteStreams;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

@RequiredArgsConstructor
public class TownsFiler
{
	private static final byte VERSION = 0;
	
	private static final ObjectType[] OBJECT_TYPES = new ObjectType[]
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
		PluginObjectType.GENERIC_PLUGIN_PLAYER,
		PluginObjectType.PLUGIN_PLAYER,
		PluginObjectType.RAID_ANALYZER,
		PluginObjectType.TOWN_PLAYER_DATA,
		PluginObjectType.TOWN_PLAYER,
	};

	private final Main plugin;
	
	public void saveTowns()
	{
		File tmp = null;
		
		try
		{
			File file = plugin.getDataFileManager().createTownsFileIfNotExists();
			tmp = new File(file.getParent(), "." + file.getName() + ".tmp");
			
			try(FileOutputStream fileOutputStream = new FileOutputStream(tmp);
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream))
			{
				writeVersions(out);
				Writers.of(out, OBJECT_TYPES)
						.getWriter(TownMapWriter.class)
						.writeReference(plugin.getTownManager().getTowns());
				out.flush();
				fileOutputStream.write(byteArrayOutputStream.toByteArray());
			}
			
			if(tmp.renameTo(file))
			{
				plugin.getLogger().info(MessageFormat.format("{0} towns saved! File length {1}",
						plugin.getTownManager().getTowns().size(),
						file.length()));
			}
			else
			{
				throw new IOException("Failed to replace the old player_data file with the new one.");
			}
		}
		catch(Exception e)
		{
			plugin.getLogger().log(Level.SEVERE, "Failed to save towns!", e);
		}
		finally
		{
			FileUtil.deleteTempFile(tmp, plugin.getLogger());
		}
	}
	
	public Map<UUID, Town> loadTownsFromDisk() throws Exception
	{
		final File file = plugin.getDataFileManager().getTownsFile();
		
		if(!file.exists())
		{
			plugin.getServer().getLogger().warning(file.getName() + " file not found! Loading empty towns...");
			return new HashMap<>();
		}
		
		if(file.length() == 0L)
		{
			plugin.getServer().getLogger().warning(file.getName() + " file is empty! Loading empty towns...");
			return new HashMap<>();
		}
		
		plugin.getLogger().info("towns.dat length: " + file.length());
		
		try(FileInputStream fileInputStream = new FileInputStream(file);
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ByteStreams.toByteArray(fileInputStream));
				ObjectInputStream in = new ObjectInputStream(byteArrayInputStream))
		{
			Map<UUID, Town> towns =  Readers.of(in, readVersions(in))
					.getReader(TownMapReader.class)
					.readReference(plugin);
			plugin.getServer().getLogger().info(towns.size() + " towns loaded!");
			
			return towns;
		}
	}
	
	private Map<ObjectType, Byte> readVersions(ObjectInput in) throws InvalidVersionException, IOException
	{
		byte version = in.readByte();
		
		if(version == 0)
		{
			Map<ObjectType, Byte> versions = new HashMap<>();
			
			versions.put(CommonObjectType.COMMON_PLAYER_DATA, in.readByte());
			versions.put(CommonObjectType.COMMON_PLAYER, in.readByte());
			versions.put(CommonObjectType.INVENTORY, in.readByte());
			versions.put(CommonObjectType.ITEM_STACK, in.readByte());
			versions.put(CommonObjectType.LOCATION, in.readByte());
			versions.put(CommonObjectType.OBJECT, in.readByte());
			versions.put(CommonObjectType.PLAYER_DATA, in.readByte());
			versions.put(CommonObjectType.POTION_EFFECT_COLLECTION, in.readByte());
			versions.put(CommonObjectType.POTION_EFFECT, in.readByte());
			versions.put(PluginObjectType.ADMIN, in.readByte());
			versions.put(PluginObjectType.ATTACKER, in.readByte());
			versions.put(PluginObjectType.GENERIC_PLUGIN_PLAYER, in.readByte());
			versions.put(PluginObjectType.PLUGIN_PLAYER, in.readByte());
			versions.put(PluginObjectType.RAID_ANALYZER, in.readByte());
			versions.put(PluginObjectType.TOWN_PLAYER_DATA, in.readByte());
			versions.put(PluginObjectType.TOWN_PLAYER, in.readByte());
			
			return versions;
		}
		
		throw new InvalidVersionException("Invalid TownsFiler version: " + version);
	}
	
	private void writeVersions(ObjectOutput out) throws IOException
	{
		out.writeByte(VERSION);
		
		for(ObjectType objectType : OBJECT_TYPES)
		{
			out.writeByte(objectType.getVersion());
		}
	}
}
