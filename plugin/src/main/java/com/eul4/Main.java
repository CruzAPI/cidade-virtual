package com.eul4;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.eul4.command.*;
import com.eul4.common.Common;
import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.CommonPlayerType;
import com.eul4.common.type.player.PlayerType;
import com.eul4.i18n.PluginBundleBaseName;
import com.eul4.listener.*;
import com.eul4.model.player.TownPlayer;
import com.eul4.rule.DislikeGeneratorAttribute;
import com.eul4.rule.LikeGeneratorAttribute;
import com.eul4.rule.Rule;
import com.eul4.rule.TownHallAttribute;
import com.eul4.serializer.DislikeGeneratorRuleSerializer;
import com.eul4.serializer.LikeGeneratorRuleSerializer;
import com.eul4.serializer.TownHallRuleSerializer;
import com.eul4.service.*;
import com.eul4.type.player.PluginCommonPlayerType;
import com.eul4.type.player.PluginPlayerType;
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

@Getter
public class Main extends Common
{
	private World townWorld;
	private World cidadeVirtualWorld;
	private TownManager townManager;
	
	private BlockDataSerializer blockDataSerializer;
	private BlockChunkToShortCoordinateSerializer blockChunkToShortCoordinateSerializer;
	private BlockDataMapSerializer blockDataMapSerializer;
	private BlockDataLoader blockDataLoader;
	private DataFileManager dataFileManager;
	private TownSerializer townSerializer;
	private StructureUpgradeExecutor structureUpgradeExecutor;
	private PurchaseExecutor purchaseExecutor;
	
	private TownHallRuleSerializer townHallRuleSerializer;
	private LikeGeneratorRuleSerializer likeGeneratorRuleSerializer;
	private DislikeGeneratorRuleSerializer dislikeGeneratorRuleSerializer;
	
	private Rule<TownHallAttribute> townHallRule;
	private Rule<LikeGeneratorAttribute> likeGeneratorRule;
	private Rule<DislikeGeneratorAttribute> dislikeGeneratorRule;
	
	private BuyStructureCommand buyStructureCommand;
	
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
			e.printStackTrace();
			getServer().shutdown();
		}
	}
	
	private void enablePlugin()
	{
		loadWorlds();
		loadServices();
		loadSerializers();
		reloadRules();
		
		super.onEnable();
		
		townManager.loadTownsOrElse(getServer()::shutdown);
		
		registerResourceBundles();
		registerCommands();
		registerListeners();
		registerPacketInterceptors();
		
		getLogger().info("Plugin enabled.");
	}
	
	@SneakyThrows
	public void reloadRules()
	{
		var townHallRule = townHallRuleSerializer.load();
		var likeGeneratorRule = likeGeneratorRuleSerializer.load();
		var dislikeGeneratorRule = dislikeGeneratorRuleSerializer.load();
		
		this.townHallRule = townHallRule;
		this.likeGeneratorRule = likeGeneratorRule;
		this.dislikeGeneratorRule = dislikeGeneratorRule;
		
		townManager.reloadTowns();
	}
	
	@SneakyThrows
	private void loadServices()
	{
		blockDataSerializer = new BlockDataSerializer();
		blockChunkToShortCoordinateSerializer = new BlockChunkToShortCoordinateSerializer();
		blockDataMapSerializer = new BlockDataMapSerializer(this);
		dataFileManager = new DataFileManager(this);
		blockDataLoader = new BlockDataLoader(this);
		townManager = new TownManager(this);
		townSerializer = new TownSerializer(this);
		structureUpgradeExecutor = new StructureUpgradeExecutor(this);
		purchaseExecutor = new PurchaseExecutor(this);
	}
	
	private void loadSerializers()
	{
		townHallRuleSerializer = new TownHallRuleSerializer(this);
		likeGeneratorRuleSerializer = new LikeGeneratorRuleSerializer(this);
		dislikeGeneratorRuleSerializer = new DislikeGeneratorRuleSerializer(this);
	}
	
	
	private void registerPacketInterceptors()
	{
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
	}
	
	private void registerCommands()
	{
		getCommand("balance").setExecutor(new BalanceCommand(this));
		getCommand("town").setExecutor(new TownCommand(this));
		getCommand("test").setExecutor(new TestCommand(this));
		getCommand("move").setExecutor(new MoveCommand(this));
		getCommand("buystructure").setExecutor(buyStructureCommand = new BuyStructureCommand(this));
		getCommand("rulereload").setExecutor(new ReloadRuleCommand(this));
	}
	
	private void registerListeners()
	{
		final PluginManager pluginManager = getServer().getPluginManager();
		
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
		pluginManager.registerEvents(new ConfirmationGuiListener(this), this);
	}
	
	private void deleteWorld(String worldName)
	{
		FileUtil.deleteDirectory(new File(worldName));
	}
	
	private void loadWorlds()
	{
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
	
	public File getSchematicsFolder()
	{
		return new File("plugins/FastAsyncWorldEdit/schematics");
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
