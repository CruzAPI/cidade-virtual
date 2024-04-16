package com.eul4.service;

import com.eul4.Main;
import com.google.common.io.ByteStreams;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class BlockDataLoader
{
	private final Main plugin;
	
	private final Map<Chunk, Map<Block, BlockData>> loadedChunks = new HashMap<>();
	
	public BlockDataLoader(Main plugin)
	{
		this.plugin = plugin;
	}
	
	public boolean hasBlockData(Block block)
	{
		return loadChunk(block).containsKey(block);
	}
	
	public BlockData loadBlockData(Block block)
	{
		return loadChunk(block.getChunk()).get(block);
	}
	
	public BlockData loadBlockDataOrDefault(Block block)
	{
		return loadChunk(block.getChunk()).computeIfAbsent(block, x -> new BlockData());
	}
	
	public void saveBlockData(Block block, BlockData blockData)
	{
		loadChunk(block).put(block, blockData);
	}
	
	public Optional<BlockData> debugHasBlockDataInMemory(Block block)
	{
		return Optional.ofNullable(loadedChunks.get(block.getChunk())).map(x -> x.get(block));
	}
	
	public BlockData removeBlockData(Block block)
	{
		return loadChunk(block).remove(block);
	}
	
	private Map<Block, BlockData> loadChunk(Block block)
	{
		return loadChunk(block.getChunk());
	}
	
	public void saveChunks(World world)
	{
		final Iterator<Map.Entry<Chunk, Map<Block, BlockData>>> iterator = loadedChunks.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			final Map.Entry<Chunk, Map<Block, BlockData>> entry = iterator.next();
			
			final Chunk chunk = entry.getKey();
			final Map<Block, BlockData> blockDataMap = entry.getValue();
			
			final boolean isChunkLoaded = chunk.isLoaded();
			
			if(chunk.getWorld() != world)
			{
				continue;
			}
			
			try
			{
				saveBlockDataMap(chunk, blockDataMap);
				
				if(!isChunkLoaded)
				{
					iterator.remove();
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	private void saveBlockDataMap(Chunk chunk, Map<Block, BlockData> blockDataMap) throws IOException
	{
		try(FileOutputStream fileOutputStream = new FileOutputStream(plugin.getDataFileManager().createBlockDataFileIfNotExists(chunk));
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(byteArrayOutputStream))
		{
			plugin.getBlockDataMapExternalizer().write(blockDataMap, out);
			out.flush();
			fileOutputStream.write(byteArrayOutputStream.toByteArray());
		}
	}
	
	private Map<Block, BlockData> loadChunk(Chunk chunk)
	{
		return loadedChunks.computeIfAbsent(chunk, this::loadChunkInDisk);
	}
	
	private Map<Block, BlockData> loadChunkInDisk(Chunk chunk)
	{
		File file = plugin.getDataFileManager().getBlockDataFile(chunk);
		
		if(!file.exists() || file.length() == 0L)
		{
			plugin.getLogger().warning("Loading empty chunk x=" + chunk.getX() + " z=" + chunk.getZ());
			return new HashMap<>();
		}
		
		try(FileInputStream fileIn = new FileInputStream(file);
				ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
				DataInputStream in = new DataInputStream(streamIn))
		{
			return plugin.getBlockDataMapExternalizer().read(chunk, in);
		}
		catch(IOException ex)
		{
			plugin.getLogger().warning("Error trying to read chunk data. Loading empty chunk x=" + chunk.getX() + " z=" + chunk.getZ());
			ex.printStackTrace();
			return new HashMap<>();
		}
	}
}
