package com.eul4;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.eul4.command.TestCommand;
import com.eul4.command.TownCommand;
import com.eul4.common.Common;
import com.eul4.common.command.AdminCommand;
import com.eul4.common.command.BuildCommand;
import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.CommonPlayerType;
import com.eul4.common.type.player.PlayerType;
import com.eul4.i18n.PluginBundleBaseName;
import com.eul4.intercepter.SpawnEntityIntercepter;
import com.eul4.listener.EntityRegisterListener;
import com.eul4.listener.TownListener;
import com.eul4.model.player.TownPlayer;
import com.eul4.service.TownManager;
import com.eul4.type.player.PluginCommonPlayerType;
import com.eul4.type.player.PluginPlayerType;
import com.eul4.util.FileUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

@Getter
public class Main extends Common
{
	private World townWorld;
	private World cidadeVirtualWorld;
	private TownManager townManager;
	
	private EntityRegisterListener entityRegisterListener;
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		
		townManager = new TownManager(this);
		
		loadWorlds();
		
		registerResourceBundles();
		registerCommands();
		registerListeners();
		registerPacketIntercepters();
		
		getLogger().info("Plugin enabled.");
	}
	
	private void registerPacketIntercepters()
	{
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		protocolManager.addPacketListener(new SpawnEntityIntercepter(this));
	}
	
	private void registerCommands()
	{
		getCommand("town").setExecutor(new TownCommand(this));
		getCommand("test").setExecutor(new TestCommand(this));
		getCommand("admin").setExecutor(new AdminCommand(this));
		getCommand("build").setExecutor(new BuildCommand(this));
	}
	
	private void registerListeners()
	{
		final PluginManager pluginManager = getServer().getPluginManager();
		
		pluginManager.registerEvents(new TownListener(this), this);
		pluginManager.registerEvents(entityRegisterListener = new EntityRegisterListener(), this);
	}
	
	private void deleteWorld(String worldName)
	{
		FileUtil.deleteDirectory(new File(worldName));
	}
	
	private void loadWorlds()
	{
		deleteWorld("town_world");

		WorldCreator wc;
		
		wc = new WorldCreator("town_world");
		wc.type(WorldType.FLAT);
		wc.environment(World.Environment.NORMAL);
		wc.generator(new ChunkGenerator() {});
		townWorld = wc.createWorld();
		
		wc = new WorldCreator("cidade_virtual");
		wc.type(WorldType.FLAT);
		wc.environment(World.Environment.THE_END);
		wc.generator(new ChunkGenerator() {});
		cidadeVirtualWorld = wc.createWorld();
	}
	
	private void registerResourceBundles()
	{
		for(Locale locale : ResourceBundleHandler.SUPPORTED_LOCALES)
		{
			for(BundleBaseName bundleBaseName : PluginBundleBaseName.values())
			{
				ResourceBundleHandler.registerBundle(ResourceBundle.getBundle(bundleBaseName.getName(), locale));
			}
		}
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
		
		getLogger().info("Plugin disabled.");
	}
	
	@Override
	public PlayerType<TownPlayer> getDefaultPlayerType()
	{
		return PluginPlayerType.TOWN_PLAYER;
	}
	
	@Override
	public CommonPlayerType<? extends CommonPlayer> getDefaultCommonPlayerType()
	{
		return PluginCommonPlayerType.TOWN_PLAYER;
	}
}
