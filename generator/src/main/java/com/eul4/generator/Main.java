package com.eul4.generator;

import com.eul4.generator.VoidGenerator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Main extends JavaPlugin
{
	private final VoidGenerator voidGenerator = new VoidGenerator();
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
	{
		if("void".equals(id))
		{
			return voidGenerator;
		}
		
		return super.getDefaultWorldGenerator(worldName, id);
	}
}
