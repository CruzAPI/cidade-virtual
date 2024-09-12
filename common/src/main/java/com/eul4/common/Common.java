package com.eul4.common;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.eul4.common.command.*;
import com.eul4.common.event.WorldSaveOrStopEvent;
import com.eul4.common.exception.PlayerNotFoundException;
import com.eul4.common.exception.UserNotFoundException;
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
import com.eul4.common.util.LoggerUtil;
import com.eul4.common.wrapper.DeafenMessageable;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Getter
public abstract class Common extends JavaPlugin
{
	private HologramTranslatorAdapter hologramTranslatorAdapter;
	
	private SpawnEntityInterceptor spawnEntityInterceptor;
	
	private ChatCommand chatCommand;
	private TellCommand tellCommand;
	
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
	private DeafenMessageable deafenMessageable;
	
	@Override
	public void onEnable()
	{
		try
		{
			console = new CraftConsole(getServer().getConsoleSender());
			deafenMessageable = new DeafenMessageable();
			
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
		registerCommand(new BroadcastCommand(this), BroadcastCommand.NAME_AND_ALIASES);
		registerCommand(new BuildCommand(this), BuildCommand.NAME_AND_ALIASES);
		registerCommand(chatCommand = new ChatCommand(this), ChatCommand.NAME_AND_ALIASES);
		registerCommand(new ClearChatCommand(this), ClearChatCommand.NAME_AND_ALIASES);
		registerCommand(new DisableChatCommand(this), DisableChatCommand.NAME_AND_ALIASES);
		registerCommand(new DisableTellCommand(this), DisableTellCommand.NAME_AND_ALIASES);
		registerCommand(new EnableChatCommand(this), EnableChatCommand.NAME_AND_ALIASES);
		registerCommand(new EnableTellCommand(this), EnableTellCommand.NAME_AND_ALIASES);
		registerCommand(new ExtraInvseeCommand(this), ExtraInvseeCommand.NAME_AND_ALIASES);
		registerCommand(new InvseeCommand(this), InvseeCommand.NAME_AND_ALIASES);
		registerCommand(new MuteCommand(this), MuteCommand.NAME_AND_ALIASES);
		registerCommand(new PexCommand(this), PexCommand.NAME_AND_ALIASES);
		registerCommand(new ReplyCommand(this), ReplyCommand.NAME_AND_ALIASES);
		registerCommand(new ScoreboardCommand(this), ScoreboardCommand.NAME_AND_ALIASES);
		registerCommand(tellCommand = new TellCommand(this), TellCommand.NAME_AND_ALIASES);
		registerCommand(new UnmuteCommand(this), UnmuteCommand.NAME_AND_ALIASES);
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
		pluginManager.registerEvents(new CommonChatListener(this), this);
		pluginManager.registerEvents(new CommonListener(this), this);
		pluginManager.registerEvents(new CommonPlayerListener(this), this);
		pluginManager.registerEvents(new ExtraInvseeCommandListener(this), this);
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
	
	protected void registerCommand(TabExecutor executor, String... args)
	{
		try
		{
			final Field bukkitCommandMap = getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			
			CommandMap commandMap = (CommandMap) bukkitCommandMap.get(getServer());
			
			Class<?> clazz = PluginCommand.class;
			
			Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, Plugin.class);
			constructor.setAccessible(true);
			
			for(String s : args)
			{
				PluginCommand command = (PluginCommand) constructor.newInstance(s, this);
				
				command.register(commandMap);
				command.setExecutor(executor);
				command.setTabCompleter(executor);
				commandMap.register(command.getName(), command);
			}
		}
		catch(Exception ex)
		{
			LoggerUtil.severe(this, ex, "An error occurred while registering commands: {0}", ex.getMessage());
		}
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
	
	public OfflinePlayer getOfflinePlayerIfCachedOrElseThrow(String name) throws UserNotFoundException
	{
		OfflinePlayer offlinePlayer = getOfflinePlayerIfCached(name);
		
		if(offlinePlayer == null)
		{
			throw new UserNotFoundException(name);
		}
		
		return offlinePlayer;
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
	
	public Player getOnlinePlayerOrElseThrow(String name) throws PlayerNotFoundException, UserNotFoundException
	{
		OfflinePlayer offlinePlayer = getOfflinePlayerIfCachedOrElseThrow(name);
		Player player = offlinePlayer.getPlayer();
		
		if(player == null)
		{
			throw new PlayerNotFoundException(offlinePlayer.getName());
		}
		
		return player;
	}
	
	public abstract CommonWorldType getMainWorldType();
}