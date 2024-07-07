package com.eul4.externalizer.filer;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.Writers;
import com.eul4.externalizer.reader.TownMapReader;
import com.eul4.externalizer.writer.TownMapWriter;
import com.eul4.type.player.PluginObjectType;
import com.eul4.util.FileUtil;
import com.eul4.wrapper.TownMap;
import com.google.common.io.ByteStreams;

import java.io.*;
import java.text.MessageFormat;
import java.util.logging.Level;

public class TownsFiler extends Filer
{
	private static final byte VERSION = 0;
	
	private static final ObjectType[] OBJECT_TYPES_V0 = new ObjectType[]
	{
		CommonObjectType.BLOCK,
		CommonObjectType.CHUNK,
		CommonObjectType.ENTITY,
		CommonObjectType.HOLOGRAM,
		CommonObjectType.INVENTORY,
		CommonObjectType.ITEM_STACK,
		CommonObjectType.LOCATION,
		CommonObjectType.OBJECT,
		CommonObjectType.TRANSLATED_HOLOGRAM_LINE,
		PluginObjectType.ARMORY,
		PluginObjectType.CANNON,
		PluginObjectType.DEPOSIT,
		PluginObjectType.DISLIKE_DEPOSIT,
		PluginObjectType.DISLIKE_GENERATOR,
		PluginObjectType.GENERATOR,
		PluginObjectType.GENERIC_STRUCTURE,
		PluginObjectType.LIKE_DEPOSIT,
		PluginObjectType.LIKE_GENERATOR,
		PluginObjectType.STRUCTURE,
		PluginObjectType.STRUCTURE_SET,
		PluginObjectType.TOWN_BLOCK_MAP,
		PluginObjectType.TOWN_BLOCK,
		PluginObjectType.TOWN_BLOCK_SET,
		PluginObjectType.TOWN_HALL,
		PluginObjectType.TOWN_MAP,
		PluginObjectType.TOWN,
		PluginObjectType.TOWN_TILE_MAP,
		PluginObjectType.TOWN_TILE,
		PluginObjectType.TURRET,
	};

	public TownsFiler(Main plugin)
	{
		super(plugin, VERSION);
	}
	
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
				Writers.of(plugin, out, writeVersions(out))
						.getWriter(TownMapWriter.class)
						.writeReferenceNotNull(plugin.getTownManager().getTowns());
				out.flush();
				fileOutputStream.write(byteArrayOutputStream.toByteArray());
			}
			
			if(tmp.renameTo(file))
			{
				plugin.getLogger().info(MessageFormat.format("{0} towns saved! {1} length: {2}",
						plugin.getTownManager().getTowns().size(),
						file.getName(),
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
	
	public TownMap loadTownsFromDisk() throws Exception
	{
		final File file = plugin.getDataFileManager().getTownsFile();
		
		if(!file.exists())
		{
			plugin.getLogger().warning(file.getName() + " file not found! Loading empty towns...");
			return new TownMap();
		}
		
		if(file.length() == 0L)
		{
			plugin.getLogger().warning(file.getName() + " file is empty! Loading empty towns...");
			return new TownMap();
		}
		
		plugin.getLogger().info("towns.dat length: " + file.length());
		
		try(FileInputStream fileInputStream = new FileInputStream(file);
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ByteStreams.toByteArray(fileInputStream));
				ObjectInputStream in = new ObjectInputStream(byteArrayInputStream))
		{
			TownMap towns = Readers.of(plugin, in, readVersions(in))
					.getReader(TownMapReader.class)
					.readReference(plugin);
			plugin.getLogger().info(towns.size() + " towns loaded!");
			
			return towns;
		}
	}
	
	@Override
	protected ObjectType[] getObjectTypes(byte version) throws InvalidVersionException
	{
		return switch(version)
		{
			case 0 -> OBJECT_TYPES_V0;
			default -> throw new InvalidVersionException(MessageFormat.format(
					"Invalid {0} version: {1}",
					getClass().getSimpleName(),
					version));
		};
	}
}
