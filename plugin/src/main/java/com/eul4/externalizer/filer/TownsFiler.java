package com.eul4.externalizer.filer;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.Writers;
import com.eul4.common.util.FileUtil;
import com.eul4.externalizer.reader.TownMapReader;
import com.eul4.externalizer.writer.TownMapWriter;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.TownMap;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;
import java.util.logging.Level;

public class TownsFiler extends PluginFiler
{
	private static final byte VERSION = 3;
	
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
		PluginObjectType.BOUGHT_TILE_MAP_BY_DEPTH,
		PluginObjectType.CANNON,
		PluginObjectType.DEPOSIT,
		PluginObjectType.DISLIKE_DEPOSIT,
		PluginObjectType.DISLIKE_GENERATOR,
		PluginObjectType.GENERATOR,
		PluginObjectType.GENERIC_STRUCTURE,
		PluginObjectType.LIKE_DEPOSIT,
		PluginObjectType.LIKE_GENERATOR,
		PluginObjectType.STRUCTURE,
		PluginObjectType.STRUCTURE_MAP,
		PluginObjectType.TOWN_BLOCK_MAP,
		PluginObjectType.TOWN_BLOCK,
		PluginObjectType.TOWN_BLOCK_SET,
		PluginObjectType.TOWN_HALL,
		PluginObjectType.TOWN_MAP,
		PluginObjectType.TOWN,
		PluginObjectType.TOWN_TILE_MAP,
		PluginObjectType.TOWN_TILE,
		PluginObjectType.TURRET,
	},
	
	OBJECT_TYPES_V1 = new ObjectType[]
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
		PluginObjectType.BOUGHT_TILE_MAP_BY_DEPTH,
		PluginObjectType.CANNON,
		PluginObjectType.DEPOSIT,
		PluginObjectType.DISLIKE_DEPOSIT,
		PluginObjectType.DISLIKE_GENERATOR,
		PluginObjectType.GENERATOR,
		PluginObjectType.GENERIC_STRUCTURE,
		PluginObjectType.LIKE_DEPOSIT,
		PluginObjectType.LIKE_GENERATOR,
		PluginObjectType.STRUCTURE,
		PluginObjectType.STRUCTURE_MAP,
		PluginObjectType.TOWN_BLOCK_MAP,
		PluginObjectType.TOWN_BLOCK,
		PluginObjectType.TOWN_BLOCK_SET,
		PluginObjectType.TOWN_HALL,
		PluginObjectType.TOWN_MAP,
		PluginObjectType.TOWN,
		PluginObjectType.TOWN_TILE_MAP,
		PluginObjectType.TOWN_TILE,
		PluginObjectType.TURRET,
		PluginObjectType.VECTOR_3,
	},
	
	OBJECT_TYPES_V2 = new ObjectType[]
	{
		CommonObjectType.BLOCK,
		CommonObjectType.BIG_DECIMAL,
		CommonObjectType.CHUNK,
		CommonObjectType.ENTITY,
		CommonObjectType.HOLOGRAM,
		CommonObjectType.INVENTORY,
		CommonObjectType.ITEM_STACK,
		CommonObjectType.LOCATION,
		CommonObjectType.OBJECT,
		CommonObjectType.TRANSLATED_HOLOGRAM_LINE,
		PluginObjectType.ARMORY,
		PluginObjectType.BOUGHT_TILE_MAP_BY_DEPTH,
		PluginObjectType.CANNON,
		PluginObjectType.CAPACITATED_CROWN_HOLDER,
		PluginObjectType.CROWN_DEPOSIT,
		PluginObjectType.DEPOSIT,
		PluginObjectType.DISLIKE_DEPOSIT,
		PluginObjectType.DISLIKE_GENERATOR,
		PluginObjectType.GENERATOR,
		PluginObjectType.GENERIC_STRUCTURE,
		PluginObjectType.LIKE_DEPOSIT,
		PluginObjectType.LIKE_GENERATOR,
		PluginObjectType.PHYSICAL_DEPOSIT,
		PluginObjectType.STRUCTURE,
		PluginObjectType.STRUCTURE_MAP,
		PluginObjectType.TOWN_BLOCK_MAP,
		PluginObjectType.TOWN_BLOCK,
		PluginObjectType.TOWN_BLOCK_SET,
		PluginObjectType.TOWN_HALL,
		PluginObjectType.TOWN_MAP,
		PluginObjectType.TOWN,
		PluginObjectType.TOWN_TILE_MAP,
		PluginObjectType.TOWN_TILE,
		PluginObjectType.TURRET,
		PluginObjectType.VECTOR_3,
	},
	
	OBJECT_TYPES_V3 = new ObjectType[]
	{
		CommonObjectType.BLOCK,
		CommonObjectType.BIG_DECIMAL,
		CommonObjectType.CHUNK,
		CommonObjectType.ENTITY,
		CommonObjectType.HOLOGRAM,
		CommonObjectType.INVENTORY,
		CommonObjectType.ITEM_STACK,
		CommonObjectType.LOCATION,
		CommonObjectType.OBJECT,
		CommonObjectType.TRANSLATED_HOLOGRAM_LINE,
		CommonObjectType.UUID,
		PluginObjectType.ARMORY,
		PluginObjectType.BOUGHT_TILE_MAP_BY_DEPTH,
		PluginObjectType.CANNON,
		PluginObjectType.CAPACITATED_CROWN_HOLDER,
		PluginObjectType.CROWN_DEPOSIT,
		PluginObjectType.DEPOSIT,
		PluginObjectType.DISLIKE_DEPOSIT,
		PluginObjectType.DISLIKE_GENERATOR,
		PluginObjectType.GENERATOR,
		PluginObjectType.GENERIC_STRUCTURE,
		PluginObjectType.LIKE_DEPOSIT,
		PluginObjectType.LIKE_GENERATOR,
		PluginObjectType.PHYSICAL_DEPOSIT,
		PluginObjectType.STRUCTURE,
		PluginObjectType.STRUCTURE_MAP,
		PluginObjectType.TOWN_BLOCK_MAP,
		PluginObjectType.TOWN_BLOCK,
		PluginObjectType.TOWN_BLOCK_SET,
		PluginObjectType.TOWN_HALL,
		PluginObjectType.TOWN_MAP,
		PluginObjectType.TOWN,
		PluginObjectType.TOWN_TILE_MAP,
		PluginObjectType.TOWN_TILE,
		PluginObjectType.TURRET,
		PluginObjectType.VECTOR_3,
	};

	public TownsFiler(Main plugin)
	{
		super(plugin, VERSION);
	}
	
	public void saveTowns()
	{
		final long startTime = System.currentTimeMillis();
		
		File tmp = null;
		
		try
		{
			File file = plugin.getDataFileManager().createTownsFileIfNotExists();
			tmp = new File(file.getParent(), "." + file.getName() + ".tmp");
			
			try
			(
				FileOutputStream fileOut = new FileOutputStream(tmp);
				BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut, BUFFER_SIZE);
				DataOutputStream out = new DataOutputStream(bufferedOut);
			)
			{
				Writers.of(plugin, out, writeVersions(out))
						.getWriter(TownMapWriter.class)
						.writeReferenceNotNull(plugin.getTownManager().getTowns());
				out.flush();
			}
			
			if(tmp.renameTo(file))
			{
				final long finishTime = System.currentTimeMillis();
				final long elapsedTime = finishTime - startTime;
				
				plugin.getLogger().info(MessageFormat.format("{0} towns saved! ({1}ms) {2} length: {3}",
						plugin.getTownManager().getTowns().size(),
						elapsedTime,
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
		
		final long startTime = System.currentTimeMillis();
		
		try
		(
			FileInputStream fileIn = new FileInputStream(file);
			FileChannel fileChannel = fileIn.getChannel()
		)
		{
			byte[] header = new byte[2];
			fileIn.read(header);
			
			fileChannel.position(0L);
			
			try(BufferedInputStream bufferedIn = new BufferedInputStream(fileIn, BUFFER_SIZE))
			{
				if(isObjectStream(header))
				{
					try(ObjectInputStream in = new ObjectInputStream(bufferedIn))
					{
						TownMap towns = Readers.of(plugin, in, readVersions(in))
								.getReader(TownMapReader.class)
								.readReference(plugin);
						plugin.getLogger().info("(ObjectStream) " + towns.size() + " towns loaded!");
						
						return towns;
					}
				}
				else
				{
					try(DataInputStream in = new DataInputStream(bufferedIn))
					{
						TownMap towns = Readers.of(plugin, in, readVersions(in))
								.getReader(TownMapReader.class)
								.readReference(plugin);
						final long finishTime = System.currentTimeMillis();
						final long elapsedTime = finishTime - startTime;
						
						plugin.getLogger().info("(DataStream) " + towns.size() + " towns loaded! (" + elapsedTime + "ms)");
						
						return towns;
					}
				}
			}
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
			default -> throw new InvalidVersionException(MessageFormat.format(
					"Invalid {0} version: {1}",
					getClass().getSimpleName(),
					version));
		};
	}
}
