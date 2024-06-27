package com.eul4.common;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.eul4.common.command.BuildCommand;
import com.eul4.common.event.WorldSaveOrStopEvent;
import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.CommonBundleBaseName;
import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.common.interceptor.HologramTranslatorAdapter;
import com.eul4.common.interceptor.SpawnEntityInterceptor;
import com.eul4.common.listener.*;
import com.eul4.common.service.CommonDataFileManager;
import com.eul4.common.service.PlayerManager;
import com.eul4.common.service.ServerTickCounter;
import com.eul4.common.service.WorldManager;
import com.eul4.common.type.player.CommonWorldType;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

@Getter
public abstract class Common extends JavaPlugin
{
	private HologramTranslatorAdapter hologramTranslatorAdapter;
	
	private EntityRegisterListener entityRegisterListener;
	private SpawnEntityInterceptor spawnEntityInterceptor;
	
	private PlayerManager playerManager;
	private WorldManager worldManager;
	private CommonDataFileManager commonDataFileManager;
	private ServerTickCounter serverTickCounter;
	
	@Override
	public void onEnable()
	{
		loadServices();
		
		registerCommonResourceBundles();
		registerListeners();
		registerPacketAdapters();
		registerCommand();
		
		entityRegisterListener.loadEntities();
		
		getLogger().info("Commons enabled!");
	}
	
	@SneakyThrows
	private void loadServices()
	{
		playerManager = new PlayerManager(this);
		worldManager = new WorldManager(this);
		commonDataFileManager = new CommonDataFileManager(this);
		serverTickCounter = new ServerTickCounter(this);
	}
	
	private void registerCommand()
	{
		getCommand("build").setExecutor(new BuildCommand(this));
	}
	
	private void registerPacketAdapters()
	{
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		protocolManager.addPacketListener(hologramTranslatorAdapter = new HologramTranslatorAdapter(this));
		protocolManager.addPacketListener(spawnEntityInterceptor = new SpawnEntityInterceptor(this));
	}
	
	private void registerCommonResourceBundles()
	{
		for(Locale locale : new Locale[] { new Locale("pt", "BR") })
		{
			for(BundleBaseName bundleBaseName : CommonBundleBaseName.values())
			{
				ResourceBundleHandler.registerBundle(ResourceBundle.getBundle(bundleBaseName.getName(), locale));
			}
		}
	}
	
	private void registerListeners()
	{
		final PluginManager pluginManager = getServer().getPluginManager();
		
		pluginManager.registerEvents(entityRegisterListener = new EntityRegisterListener(this), this);
		pluginManager.registerEvents(new CommonAdminListener(this), this);
		pluginManager.registerEvents(new CommonPlayerListener(this), this);
		pluginManager.registerEvents(new GuiListener(this), this);
		pluginManager.registerEvents(new FixInventoryVisualBugListener(this), this);
		pluginManager.registerEvents(new GuiClickEventCallListener(this), this);
		pluginManager.registerEvents(new CancelItemDropListener(this), this);
		pluginManager.registerEvents(new CancelItemMoveListener(this), this);
		pluginManager.registerEvents(new RemoveOnDropItemListener(this), this);
		pluginManager.registerEvents(new CancelInteractionItemListener(this), this);
		pluginManager.registerEvents(new WorldSaveRecallListener(this), this);
	}
	
	@Override
	public void onDisable()
	{
		for(World world : getServer().getWorlds())
		{
			getServer().getPluginManager().callEvent(new WorldSaveOrStopEvent(world));
		}
		
		serverTickCounter.saveOrLogError();
		getLogger().info("Commons disabled!");
	}
	
	public boolean isQueued(BukkitRunnable bukkitRunnable)
	{
		return Optional.ofNullable(bukkitRunnable)
				.map(BukkitRunnable::getTaskId)
				.filter(getServer().getScheduler()::isQueued)
				.isPresent();
	}
	
	public File getSchematicsFolder()
	{
		return new File("plugins/FastAsyncWorldEdit/schematics");
	}
	
	public long getTotalTick()
	{
		return serverTickCounter.getTotalTick();
	}
	
	public abstract CommonWorldType getMainWorldType();
}