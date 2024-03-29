package com.eul4.service;

import com.eul4.Main;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class BlockDataLoader
{
	private final Main plugin;
	private final Function<Chunk, Map<Block, BlockData>> loadChunkFunction;
	
	private final Map<Chunk, Map<Block, BlockData>> loadedChunks = new HashMap<>();
	
	public BlockDataLoader(Main plugin)
	{
		this.plugin = plugin;
		this.loadChunkFunction = chunk ->
		{
			File file = plugin.getDataFileManager().getBlockDataFile(chunk);
			
			if(!file.exists() || file.length() == 0L)
			{
				return new HashMap<>();
			}
			
			try(FileInputStream fileIn = new FileInputStream(file);
					ByteArrayInputStream streamIn = new ByteArrayInputStream(ByteStreams.toByteArray(fileIn));
					ObjectInputStream in = new ObjectInputStream(streamIn))
			{
				return plugin.getBlockDataMapSerializer()
						.deserialize((Map<Short, byte[]>) in.readObject(), chunk);
			}
			catch(ClassNotFoundException | IOException ex)
			{
				ex.printStackTrace();
			}
			
			return new HashMap<>();
		};
	}
	
	public BlockData loadBlockData(Block block)
	{
		return loadChunk(block.getChunk()).get(block);
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
			final Map<Block, BlockData> blockDatas = entry.getValue();
			
			final boolean isChunkLoaded = chunk.isLoaded();
			
			if(chunk.getWorld() != world)
			{
				continue;
			}
			
			final File file;
			
			try
			{
				file = plugin.getDataFileManager().createBlockDataFileIfNotExists(chunk);
			}
			catch(IOException e)
			{
				e.printStackTrace();
				continue;
			}
			
			try(FileOutputStream fileOutputStream = new FileOutputStream(file);
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream))
			{
				objectOutputStream.writeObject(plugin.getBlockDataMapSerializer().serialize(blockDatas));
				fileOutputStream.write(byteArrayOutputStream.toByteArray());
				
				if(!isChunkLoaded)
				{
					iterator.remove();
					Bukkit.broadcastMessage("chunk removed! x: " + chunk.getX() + " z: " + chunk.getZ());
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	private Map<Block, BlockData> loadChunk(Chunk chunk)
	{
		return loadedChunks.computeIfAbsent(chunk, loadChunkFunction);
	}
}
