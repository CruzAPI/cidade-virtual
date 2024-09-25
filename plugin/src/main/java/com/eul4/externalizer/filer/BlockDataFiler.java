package com.eul4.externalizer.filer;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.Writers;
import com.eul4.event.BlockDataLoadEvent;
import com.eul4.externalizer.reader.BlockDataMapReader;
import com.eul4.externalizer.writer.BlockDataMapWriter;
import com.eul4.service.BlockData;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.BlockDataMap;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;

public class BlockDataFiler extends PluginFiler
{
	private static final byte VERSION = 1;
	
	private static final ObjectType[] OBJECT_TYPES_V0 = new ObjectType[]
	{
		CommonObjectType.OBJECT,
		PluginObjectType.BLOCK_DATA_MAP,
		PluginObjectType.BLOCK_DATA,
		PluginObjectType.SHORT_COORDINATE_BLOCK_CHUNK,
	},
	
	OBJECT_TYPES_V1 = new ObjectType[]
	{
		CommonObjectType.OBJECT,
		PluginObjectType.BLOCK_DATA_MAP,
		PluginObjectType.BLOCK_DATA,
		PluginObjectType.POINT_4_BIT,
		PluginObjectType.SHORT_COORDINATE_BLOCK_CHUNK,
	};
	
	private final Map<World, Map<Chunk, BlockDataMap>> worldChunks = new HashMap<>();
	
	public BlockDataFiler(Main plugin)
	{
		super(plugin, VERSION);
	}
	
	private Map<Chunk, BlockDataMap> getLoadedChunks(Chunk chunk)
	{
		return getLoadedChunks(chunk.getWorld());
	}
	
	private Map<Chunk, BlockDataMap> getLoadedChunks(Block block)
	{
		return getLoadedChunks(block.getWorld());
	}
	
	private Map<Chunk, BlockDataMap> getLoadedChunks(World world)
	{
		return worldChunks.computeIfAbsent(world, (key) -> new HashMap<>());
	}
	
	public boolean hasBlockData(Block block)
	{
		return loadChunk(block).containsKey(block);
	}
	
	public BlockData loadBlockData(Block block)
	{
		return loadChunk(block.getChunk()).get(block);
	}
	
	public void setBlockData(Block block, BlockData blockData)
	{
		setBlockData(block, blockData, BlockDataLoadEvent.Cause.OTHER);
	}
	public void setBlockData(Block block, BlockData blockData, BlockDataLoadEvent.Cause cause)
	{
		BlockDataMap chunkData = loadChunk(block);
		new BlockDataLoadEvent(block, blockData, cause).callEvent();
		chunkData.put(block, blockData);
	}
	
	public BlockData loadBlockDataOrDefault(Block block)
	{
		return loadBlockDataOrDefault(block, BlockDataLoadEvent.Cause.OTHER);
	}
	
	public BlockData loadBlockDataOrDefault(Block block, BlockDataLoadEvent.Cause cause)
	{
		return loadBlockDataOrDefault(block, BlockData::new, cause);
	}
	
	public BlockData loadBlockDataOrDefault(Block block, Supplier<BlockData> blockDataSupplier)
	{
		return loadBlockDataOrDefault(block, blockDataSupplier, BlockDataLoadEvent.Cause.OTHER);
	}
	
	public BlockData loadBlockDataOrDefault(Block block, Supplier<BlockData> blockDataSupplier, BlockDataLoadEvent.Cause cause)
	{
		BlockDataMap chunkData = loadChunk(block);
		
		BlockData blockData = chunkData.get(block);
		
		if(blockData != null)
		{
			return blockData;
		}
		
		blockData = blockDataSupplier.get();
		BlockDataLoadEvent event = new BlockDataLoadEvent(block, blockData, cause);
		event.callEvent();
		chunkData.put(block, event.getBlockData());
		return event.getBlockData();
	}
	
	public BlockData removeBlockData(Block block)
	{
		return loadChunk(block).remove(block);
	}
	
	private BlockDataMap loadChunk(Block block)
	{
		return loadChunk(block.getChunk());
	}
	
	public void saveChunks(World world)
	{
		final Iterator<Map.Entry<Chunk, BlockDataMap>> iterator = getLoadedChunks(world).entrySet().iterator();
		
		while(iterator.hasNext())
		{
			final Map.Entry<Chunk, BlockDataMap> entry = iterator.next();
			
			final Chunk chunk = entry.getKey();
			final BlockDataMap blockDataMap = entry.getValue();
			
			final boolean isChunkLoaded = chunk.isLoaded();
			
			try
			{
				File file = plugin.getDataFileManager().createBlockDataFileIfNotExists(chunk);
				File tmp = new File(file.getParent(), "." + file.getName() + ".tmp");
				
				try
				(
					FileOutputStream fileOut = new FileOutputStream(tmp);
					BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut);
					DataOutputStream out = new DataOutputStream(bufferedOut);
				)
				{
					Writers
							.of(plugin, out, writeVersions(out))
							.getWriter(BlockDataMapWriter.class)
							.writeReference(blockDataMap);
					out.flush();
				}
				
				if(!tmp.renameTo(file))
				{
					throw new IOException("Failed to replace the old player_data file with the new one.");
				}
				
				if(!isChunkLoaded)
				{
					iterator.remove();
				}
			}
			catch(Exception ex)
			{
				plugin.getLogger().log(Level.SEVERE, MessageFormat.format(
						"Failed to save chunk data! w={0} x={1} z={2}",
						chunk.getWorld().getName(),
						chunk.getX(),
						chunk.getZ()), ex);
			}
		}
	}
	
	private BlockDataMap loadChunk(Chunk chunk)
	{
		return getLoadedChunks(chunk).computeIfAbsent(chunk, this::loadBlockDataMapFromDisk);
	}
	
	private BlockDataMap loadBlockDataMapFromDisk(Chunk chunk)
	{
		File file = plugin.getDataFileManager().getBlockDataFile(chunk);
		
		if(!file.exists())
		{
			plugin.getLogger().warning(MessageFormat.format(
					"File {0} not found. Loading empty chunk data w={1} x={2} z={3}",
					file.getName(),
					chunk.getWorld().getName(),
					chunk.getX(),
					chunk.getZ()));
			return new BlockDataMap(chunk);
		}
		
		if(file.length() == 0L)
		{
			plugin.getLogger().warning(MessageFormat.format(
					"File {0} is empty. Loading empty chunk data w={1} x={2} z={3}",
					file.getName(),
					chunk.getWorld().getName(),
					chunk.getX(),
					chunk.getZ()));
			return new BlockDataMap(chunk);
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
						return Readers
								.of(plugin, in, readVersions(in))
								.getReader(BlockDataMapReader.class)
								.readReference(chunk);
					}
				}
				else
				{
					try(DataInputStream in = new DataInputStream(bufferedIn))
					{
						return Readers
								.of(plugin, in, readVersions(in))
								.getReader(BlockDataMapReader.class)
								.readReference(chunk);
					}
				}
			}
		}
		catch(Exception ex)
		{
			plugin.getLogger().log(Level.SEVERE, MessageFormat.format(
					"Error trying to read chunk data. Loading empty chunk x={0} z={1}",
					chunk.getX(),
					chunk.getZ()), ex);
			return new BlockDataMap(chunk);
		}
	}
	
	@Override
	protected ObjectType[] getObjectTypes(byte version) throws InvalidVersionException
	{
		return switch(version)
		{
			case 0 -> OBJECT_TYPES_V0;
			case 1 -> OBJECT_TYPES_V1;
			default -> throw new InvalidVersionException(MessageFormat.format(
					"Invalid {0} version: {1}",
					getClass().getSimpleName(),
					version));
		};
	}
}
