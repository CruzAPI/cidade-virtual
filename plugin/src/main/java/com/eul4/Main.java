package com.eul4;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.eul4.command.*;
import com.eul4.common.Common;
import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.common.type.player.CommonWorldType;
import com.eul4.externalizer.filer.BlockDataFiler;
import com.eul4.externalizer.filer.PlayerDataFiler;
import com.eul4.externalizer.filer.TownsFiler;
import com.eul4.i18n.PluginBundleBaseName;
import com.eul4.listener.*;
import com.eul4.listener.hotbar.DefenderSpectatorHotbarListener;
import com.eul4.listener.hotbar.RaidAnalyzerHotbarListener;
import com.eul4.listener.hotbar.RaidSpectatorHotbarListener;
import com.eul4.listener.inventory.ArmoryGuiListener;
import com.eul4.listener.inventory.ArmoryMenuGuiListener;
import com.eul4.listener.inventory.ArmoryWeaponShopGuiListener;
import com.eul4.listener.player.AttackerListener;
import com.eul4.listener.player.DefenderListener;
import com.eul4.listener.player.InvincibleListener;
import com.eul4.listener.player.SpectatorListener;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.*;
import com.eul4.rule.serializer.*;
import com.eul4.service.DataFileManager;
import com.eul4.service.PurchaseExecutor;
import com.eul4.service.StructureUpgradeExecutor;
import com.eul4.service.TownManager;
import com.eul4.type.PluginWorldType;
import com.eul4.util.FileUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.PluginManager;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;

@Getter
public class Main extends Common
{
	private TownManager townManager;
	
	private DataFileManager dataFileManager;
	private StructureUpgradeExecutor structureUpgradeExecutor;
	private PurchaseExecutor purchaseExecutor;
	
	private TownHallRuleSerializer townHallRuleSerializer;
	private LikeGeneratorRuleSerializer likeGeneratorRuleSerializer;
	private DislikeGeneratorRuleSerializer dislikeGeneratorRuleSerializer;
	private LikeDepositRuleSerializer likeDepositRuleSerializer;
	private DislikeDepositRuleSerializer dislikeDepositRuleSerializer;
	private ArmoryRuleSerializer armoryRuleSerializer;
	
	private Rule<TownHallAttribute> townHallRule;
	private Rule<LikeGeneratorAttribute> likeGeneratorRule;
	private Rule<DislikeGeneratorAttribute> dislikeGeneratorRule;
	private Rule<LikeDepositAttribute> likeDepositRule;
	private Rule<DislikeDepositAttribute> dislikeDepositRule;
	private Rule<ArmoryAttribute> armoryRule;
	
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
		
		super.onEnable();
		
		registerWorlds();
		registerServices();
		registerFilers();
		registerRuleSerializers();
		
		registerCommands();
		registerListeners();
		registerPacketInterceptors();
		
		reloadRules();
		townManager.loadTowns();
		
		pasteCorruptedTowns();
		
		getLogger().info("Plugin enabled.");
	}
	
	private void registerWorlds()
	{
		for(PluginWorldType type : PluginWorldType.values())
		{
			getWorldManager().register(type);
		}
	}
	
	@SneakyThrows
	public void reloadRules()
	{
		var townHallRule = townHallRuleSerializer.load();
		var likeGeneratorRule = likeGeneratorRuleSerializer.load();
		var dislikeGeneratorRule = dislikeGeneratorRuleSerializer.load();
		var likeDepositRule = likeDepositRuleSerializer.load();
		var dislikeDepositRule = dislikeDepositRuleSerializer.load();
		var armoryRule = armoryRuleSerializer.load();
		
		this.townHallRule = townHallRule;
		this.likeGeneratorRule = likeGeneratorRule;
		this.dislikeGeneratorRule = dislikeGeneratorRule;
		this.likeDepositRule = likeDepositRule;
		this.dislikeDepositRule = dislikeDepositRule;
		this.armoryRule = armoryRule;
		
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
		armoryRuleSerializer = new ArmoryRuleSerializer(this);
	}
	
	private void registerPacketInterceptors()
	{
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
	}
	
	private void registerCommands()
	{
		getCommand("admin").setExecutor(new AdminCommand(this));
		getCommand("balance").setExecutor(new BalanceCommand(this));
		getCommand(TownCommand.COMMAND_NAME).setExecutor(new TownCommand(this));
		getCommand(SpawnCommand.COMMAND_NAME).setExecutor(new SpawnCommand(this));
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
		pluginManager.registerEvents(new RaidSpectatorHotbarListener(this), this);
		pluginManager.registerEvents(new DefenderSpectatorHotbarListener(this), this);
		
		pluginManager.registerEvents(new AttackerListener(this), this);
		pluginManager.registerEvents(new DefenderListener(this), this);
		pluginManager.registerEvents(new InvincibleListener(this), this);
		pluginManager.registerEvents(new SpectatorListener(this), this);
		
		pluginManager.registerEvents(new ArmoryGuiListener(this), this);
		pluginManager.registerEvents(new ArmoryMenuGuiListener(this), this);
		pluginManager.registerEvents(new ArmoryWeaponShopGuiListener(this), this);
		
		pluginManager.registerEvents(new BlockDataSaveListener(this), this);
		pluginManager.registerEvents(new InventoryUpdateListener(this), this);
		pluginManager.registerEvents(new StructureListener(this), this);
		pluginManager.registerEvents(new StructureGuiListener(this), this);
		pluginManager.registerEvents(new StructureShopGuiListener(this), this);
		pluginManager.registerEvents(new GeneratorGuiListener(this), this);
		pluginManager.registerEvents(new StructureMoveListener(this), this);
		pluginManager.registerEvents(new TownAttackListener(this), this);
		pluginManager.registerEvents(new TownListener(this), this);
		pluginManager.registerEvents(new TownSaveListener(this), this);
		pluginManager.registerEvents(new ItemBuilderListener(this), this);
		pluginManager.registerEvents(new PlayerLoaderListener(this), this);
		pluginManager.registerEvents(new PlayerManagerListener(this), this);
		pluginManager.registerEvents(new ConfirmationGuiListener(this), this);
		pluginManager.registerEvents(new TownHardnessListener(this), this);
		pluginManager.registerEvents(new TownAntiGrieffingListener(this), this);
		pluginManager.registerEvents(new FrozenTownListener(this), this);
	}
	
	private void deleteWorld(String worldName)
	{
		FileUtil.deleteDirectory(new File(worldName));
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
	
	private void pasteCorruptedTowns()
	{
		File[] files = dataFileManager.getTownSchematicFiles();
		
		if(files == null)
		{
			return;
		}
		
		for(File file : files)
		{
			try
			{
				UUID uuid = UUID.fromString(FileUtils.removeExtension(file.getName()));
				townManager.getTown(uuid).loadAndPasteTownSchematic();
				getLogger().info(MessageFormat.format("Corrupted town {0} restored!", uuid));
			}
			catch(Exception e)
			{
				getLogger().log(Level.WARNING, "Error while trying to paste corrupted town! File: " + file.getName(), e);
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
	
	@Override
	public CommonWorldType getMainWorldType()
	{
		return PluginWorldType.OVER_WORLD;
	}
}
