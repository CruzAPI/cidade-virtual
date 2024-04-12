package com.eul4.service;

import com.eul4.StructureType;
import com.eul4.common.Common;
import lombok.RequiredArgsConstructor;
import org.bukkit.Chunk;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public class DataFileManager
{
	private final Common plugin;
	
	public File getBlockDataFile(Chunk chunk)
	{
		return new File(plugin.getDataFolder() + "/block_data/" + chunk.getWorld().getName(),
				"c." + chunk.getX() + "." + chunk.getZ() + ".dat");
	}
	
	public File createBlockDataFileIfNotExists(Chunk chunk) throws IOException
	{
		return createFileIfNotExists(getBlockDataFile(chunk));
	}
	
	public File getTownsFile()
	{
		return new File(plugin.getDataFolder(), "towns.dat");
	}
	
	public File createTownsFileIfNotExists() throws IOException
	{
		return createFileIfNotExists(getTownsFile());
	}
	
	public File getLikeGeneratorRuleFile()
	{
		return new File(plugin.getDataFolder(), "like_generator_rule.yml");
	}
	
	public File getDislikeGeneratorRuleFile()
	{
		return new File(plugin.getDataFolder(), "dislike_generator_rule.yml");
	}
	
	public File getTownHallRuleFile()
	{
		return new File(plugin.getDataFolder(), "town_hall_rule.yml");
	}
	
	public File getStructurePricesFile()
	{
		return new File(plugin.getDataFolder(), "structure_prices.dat");
	}
	
	public File createStructurePricesFileIfNotExists() throws IOException
	{
		return createFileIfNotExists(getStructurePricesFile());
	}
	
	private File createFileIfNotExists(File file) throws IOException
	{
		if(!file.exists())
		{
			file.toPath().getParent().toFile().mkdirs();
			file.createNewFile();
		}
		
		return file;
	}
	
	public File getRuleFile(StructureType<?, ?> structureType)
	{
		String fileName = structureType.name().toLowerCase() + "_rule.yml";
		return new File(plugin.getDataFolder(), fileName);
	}
}
