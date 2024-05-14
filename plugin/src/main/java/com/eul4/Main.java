package com.eul4;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.eul4.command.*;
import com.eul4.common.Common;
import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.externalizer.filer.BlockDataFiler;
import com.eul4.externalizer.filer.PlayerDataFiler;
import com.eul4.externalizer.filer.TownsFiler;
import com.eul4.i18n.PluginBundleBaseName;
import com.eul4.listener.*;
import com.eul4.listener.hotbar.RaidAnalyzerHotbarListener;
import com.eul4.listener.player.RaidAnalyzerListener;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.*;
import com.eul4.rule.serializer.*;
import com.eul4.service.*;
import com.eul4.util.FileUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

@Getter
public class Main extends Common
{
	private World world;
	private World townWorld;
	private World cidadeVirtualWorld;
	private TownManager townManager;
	
	private DataFileManager dataFileManager;
	private StructureUpgradeExecutor structureUpgradeExecutor;
	private PurchaseExecutor purchaseExecutor;
	
	private TownHallRuleSerializer townHallRuleSerializer;
	private LikeGeneratorRuleSerializer likeGeneratorRuleSerializer;
	private DislikeGeneratorRuleSerializer dislikeGeneratorRuleSerializer;
	private LikeDepositRuleSerializer likeDepositRuleSerializer;
	private DislikeDepositRuleSerializer dislikeDepositRuleSerializer;
	
	private Rule<TownHallAttribute> townHallRule;
	private Rule<LikeGeneratorAttribute> likeGeneratorRule;
	private Rule<DislikeGeneratorAttribute> dislikeGeneratorRule;
	private Rule<LikeDepositAttribute> likeDepositRule;
	private Rule<DislikeDepositAttribute> dislikeDepositRule;
	
	private BuyStructureCommand buyStructureCommand;
	private RaidCommand	raidCommand;
	
	private BlockDataFiler blockDataFiler;
	private PlayerDataFiler playerDataFiler;
	private TownsFiler townsFiler;
	
	@Override
	public void onEnable()
	{
		tryEnablePluginOrShutdown();
	}
	
	private void tryEnablePluginOrShutdown()
	{
		try
		{
			enablePlugin();
		}
		catch(Exception e)
		{
			getLogger().log(Level.SEVERE, "Failed to enable plugin! Server will shut down...", e);
			getServer().shutdown();
		}
	}
	
	@SneakyThrows
	private void enablePlugin()
	{
		registerResourceBundles();
		loadWorlds();
		
		super.onEnable();
		
		registerServices();
		registerFilers();
		registerRuleSerializers();
		
		registerCommands();
		registerListeners();
		registerPacketInterceptors();
		
		reloadRules();
		townManager.loadTowns();
		
		getLogger().info("Plugin enabled.");
	}
	
	@SneakyThrows
	public void reloadRules()
	{
		var townHallRule = townHallRuleSerializer.load();
		var likeGeneratorRule = likeGeneratorRuleSerializer.load();
		var dislikeGeneratorRule = dislikeGeneratorRuleSerializer.load();
		var likeDepositRule = likeDepositRuleSerializer.load();
		var dislikeDepositRule = dislikeDepositRuleSerializer.load();
		
		this.townHallRule = townHallRule;
		this.likeGeneratorRule = likeGeneratorRule;
		this.dislikeGeneratorRule = dislikeGeneratorRule;
		this.likeDepositRule = likeDepositRule;
		this.dislikeDepositRule = dislikeDepositRule;
		
		townManager.reloadTowns();
	}
	
	private void registerFilers()
	{
		playerDataFiler = new PlayerDataFiler(this);
		townsFiler = new TownsFiler(this);
	}
	
	@SneakyThrows
	private void registerServices()
	{
		dataFileManager = new DataFileManager(this);
		blockDataFiler = new BlockDataFiler(this);
		townManager = new TownManager(this);
		structureUpgradeExecutor = new StructureUpgradeExecutor(this);
		purchaseExecutor = new PurchaseExecutor(this);
	}
	
	private void registerRuleSerializers()
	{
		townHallRuleSerializer = new TownHallRuleSerializer(this);
		likeGeneratorRuleSerializer = new LikeGeneratorRuleSerializer(this);
		dislikeGeneratorRuleSerializer = new DislikeGeneratorRuleSerializer(this);
		likeDepositRuleSerializer = new LikeDepositRuleSerializer(this);
		dislikeDepositRuleSerializer = new DislikeDepositRuleSerializer(this);
	}
	
	private void registerPacketInterceptors()
	{
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
	}
	
	private void registerCommands()
	{
		getCommand("admin").setExecutor(new AdminCommand(this));
		getCommand("balance").setExecutor(new BalanceCommand(this));
		getCommand("town").setExecutor(new TownCommand(this));
		getCommand("test").setExecutor(new TestCommand(this));
		getCommand("move").setExecutor(new MoveCommand(this));
		getCommand("raid").setExecutor(raidCommand = new RaidCommand(this));
		getCommand("buystructure").setExecutor(buyStructureCommand = new BuyStructureCommand(this));
		getCommand("rulereload").setExecutor(new ReloadRuleCommand(this));
	}
	
	private void registerListeners()
	{
		final PluginManager pluginManager = getServer().getPluginManager();
		
		pluginManager.registerEvents(new RaidAnalyzerHotbarListener(this), this);
		pluginManager.registerEvents(new RaidAnalyzerListener(this), this);
		
		pluginManager.registerEvents(new BlockDataSaveListener(this), this);
		pluginManager.registerEvents(new InventoryUpdateListener(this), this);
		pluginManager.registerEvents(new StructureListener(this), this);
		pluginManager.registerEvents(new StructureGuiListener(this), this);
		pluginManager.registerEvents(new StructureShopGuiListener(this), this);
		pluginManager.registerEvents(new GeneratorGuiListener(this), this);
		pluginManager.registerEvents(new StructureMoveListener(this), this);
		pluginManager.registerEvents(new TownListener(this), this);
		pluginManager.registerEvents(new TownSaveListener(this), this);
		pluginManager.registerEvents(new ItemBuilderListener(this), this);
		pluginManager.registerEvents(new PlayerLoaderListener(this), this);
		pluginManager.registerEvents(new PlayerManagerListener(this), this);
		pluginManager.registerEvents(new ConfirmationGuiListener(this), this);
		pluginManager.registerEvents(new TownHardnessListener(this), this);
		pluginManager.registerEvents(new TownAntiGrieffingListener(this), this);
	}
	
	private void deleteWorld(String worldName)
	{
		FileUtil.deleteDirectory(new File(worldName));
	}
	
	private void loadWorlds()
	{
		WorldCreator wc;
		
		wc = new WorldCreator("world");
		wc.type(WorldType.NORMAL);
		wc.environment(World.Environment.NORMAL);
		world = wc.createWorld();
		
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
		
		townsFiler.saveTowns();
		getServer().getWorlds().forEach(blockDataFiler::saveChunks);
		
		getLogger().info("Plugin disabled.");
	}
	
	public File getSchematicsFolder()
	{
		return new File("plugins/FastAsyncWorldEdit/schematics");
	}
}
