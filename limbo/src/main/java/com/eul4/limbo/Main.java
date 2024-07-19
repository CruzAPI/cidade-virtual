package com.eul4.limbo;

import com.eul4.limbo.command.TestCommand;
import com.eul4.limbo.listener.EntitySpawnListener;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Main extends JavaPlugin
{
	private World limboWorld;
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		
		registerWorld();
		
		registerCommands();
		registerListeners();
	}
	
	private void registerWorld()
	{
		limboWorld = WorldCreator.name("limbo")
				.environment(World.Environment.THE_END)
				.generateStructures(false)
				.generator(new ChunkGenerator(){})
				.createWorld();
	}
	
	private void registerListeners()
	{
		getServer().getPluginManager().registerEvents(new EntitySpawnListener(this), this);
	}
	
	private void registerCommands()
	{
		getCommand("test").setExecutor(new TestCommand(this));
	}
}