package com.eul4.common;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.eul4.common.command.*;
import com.eul4.common.event.WorldSaveOrStopEvent;
import com.eul4.common.externalizer.filer.GroupMapFiler;
import com.eul4.common.externalizer.filer.UserFiler;
import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.CommonBundleBaseName;
import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.common.interceptor.HideEntityFlagInterceptor;
import com.eul4.common.interceptor.HologramTranslatorAdapter;
import com.eul4.common.interceptor.SpawnEntityInterceptor;
import com.eul4.common.listener.*;
import com.eul4.common.listener.container.RemoveItemOnCommonPlayerRegisterListener;
import com.eul4.common.listener.container.RemoveItemOnPlayerJoinListener;
import com.eul4.common.listener.container.RemoveOnChunkLoadListener;
import com.eul4.common.model.console.Console;
import com.eul4.common.model.console.CraftConsole;
import com.eul4.common.service.*;
import com.eul4.common.type.player.CommonWorldType;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Getter
public abstract class Common extends JavaPlugin
{
	private HologramTranslatorAdapter hologramTranslatorAdapter;
	
	private SpawnEntityInterceptor spawnEntityInterceptor;
	
	private ChatCommand chatCommand;
	
	private PlayerManager playerManager;
	private WorldManager worldManager;
	private CommonDataFileManager commonDataFileManager;
	private MessageableService messageableService;
	private PermissionService permissionService;
	private ServerTickCounter serverTickCounter;
	
	private GroupMapFiler groupMapFiler;
	private UserFiler userFiler;
	
	private Set<String> offlineUsernames;
	
	private Console console;
	
	@Override
	public void onEnable()
	{
		try
		{
			console = new CraftConsole(getServer().getConsoleSender());
			
			loadServices();
			
			registerCommonResourceBundles();
			registerListeners();
			registerPacketAdapters();
			registerCommand();
			registerFilers();
			
			groupMapFiler.load();
			
			offlineUsernames = Arrays.stream(getServer().getOfflinePlayers())
					.map(OfflinePlayer::getName)
					.collect(Collectors.toSet());
			
			getLogger().info("Commons enabled!");
		}
		catch(Exception e)
		{
			getLogger().log(Level.SEVERE, "Failed to enable plugin! Server will shut down...", e);
			getServer().shutdown();
		}
	}
	
	private void registerFilers()
	{
		groupMapFiler = new GroupMapFiler(this);
		userFiler = new UserFiler(this);
	}
	
	@SneakyThrows
	private void loadServices()
	{
		playerManager = new PlayerManager(this);
		worldManager = new WorldManager(this);
		commonDataFileManager = new CommonDataFileManager(this);
		messageableService = new MessageableService(this);
		permissionService = new PermissionService(this);
		serverTickCounter = new ServerTickCounter(this);
	}
	
	private void registerCommand()
	{
		getCommand(BroadcastCommand.COMMAND_NAME).setExecutor(new BroadcastCommand(this));
		getCommand("build").setExecutor(new BuildCommand(this));
		getCommand(ChatCommand.COMMAND_NAME).setExecutor(chatCommand = new ChatCommand(this));
		getCommand(ClearChatCommand.COMMAND_NAME).setExecutor(new ClearChatCommand(this));
		getCommand("pex").setExecutor(new PexCommand(this));
		getCommand("scoreboard").setExecutor(new ScoreboardCommand(this));
	}
	
	private void registerPacketAdapters()
	{
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		protocolManager.addPacketListener(hologramTranslatorAdapter = new HologramTranslatorAdapter(this));
		protocolManager.addPacketListener(spawnEntityInterceptor = new SpawnEntityInterceptor(this));
		protocolManager.addPacketListener(new HideEntityFlagInterceptor(this));
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
		
		pluginManager.registerEvents(new RemoveOnChunkLoadListener(this), this);
		
		pluginManager.registerEvents(new CommonAdminListener(this), this);
		pluginManager.registerEvents(new CommonPlayerListener(this), this);
		pluginManager.registerEvents(new GuiListener(this), this);
		pluginManager.registerEvents(new FixInventoryVisualBugListener(this), this);
		pluginManager.registerEvents(new GuiClickEventCallListener(this), this);
		pluginManager.registerEvents(new CancelItemDropListener(this), this);
		pluginManager.registerEvents(new CancelItemMoveListener(this), this);
		pluginManager.registerEvents(new CancelItemSwapListener(this), this);
		pluginManager.registerEvents(new RemoveItemOnCommonPlayerRegisterListener(this), this);
		pluginManager.registerEvents(new RemoveOnDropItemListener(this), this);
		pluginManager.registerEvents(new RemoveItemOnPlayerJoinListener(this), this);
		pluginManager.registerEvents(new CancelInteractionItemListener(this), this);
		pluginManager.registerEvents(new WorldSaveListener(this), this);
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
		groupMapFiler.save();
		userFiler.saveMemoryUsers();
		
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
	
	public OfflinePlayer getOfflinePlayerIfCached(String name)
	{
		OfflinePlayer offlinePlayer = getServer().getOfflinePlayerIfCached(name);
		
		if(offlinePlayer != null)
		{
			offlineUsernames.add(offlinePlayer.getName());
		}
		
		return offlinePlayer;
	}
	
	public abstract CommonWorldType getMainWorldType();
}