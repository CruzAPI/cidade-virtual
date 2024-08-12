package com.eul4;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.eul4.command.*;
import com.eul4.common.Common;
import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.common.type.player.CommonWorldType;
import com.eul4.common.util.FileUtil;
import com.eul4.externalizer.filer.BlockDataFiler;
import com.eul4.externalizer.filer.PlayerDataFiler;
import com.eul4.externalizer.filer.TownsFiler;
import com.eul4.i18n.PluginBundleBaseName;
import com.eul4.listener.*;
import com.eul4.listener.container.entity.CancelDropOnDeathListener;
import com.eul4.listener.container.entity.FakeShulkerBulletListener;
import com.eul4.listener.container.entity.FakeVillagerListener;
import com.eul4.listener.hotbar.DefenderSpectatorHotbarListener;
import com.eul4.listener.hotbar.RaidAnalyzerHotbarListener;
import com.eul4.listener.hotbar.RaidSpectatorHotbarListener;
import com.eul4.listener.inventory.*;
import com.eul4.listener.player.*;
import com.eul4.listener.player.tutorial.step.*;
import com.eul4.listener.scoreboard.AnalyzerScoreboardListener;
import com.eul4.listener.scoreboard.TownScoreboardListener;
import com.eul4.listener.structure.ArmoryListener;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.*;
import com.eul4.rule.serializer.*;
import com.eul4.service.*;
import com.eul4.task.SpawnProtectionTask;
import com.eul4.type.PluginWorldType;
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
	private StructureDamageCalculator structureDamageCalculator;
	
	private TownHallRuleSerializer townHallRuleSerializer;
	private LikeGeneratorRuleSerializer likeGeneratorRuleSerializer;
	private DislikeGeneratorRuleSerializer dislikeGeneratorRuleSerializer;
	private LikeDepositRuleSerializer likeDepositRuleSerializer;
	private DislikeDepositRuleSerializer dislikeDepositRuleSerializer;
	private ArmoryRuleSerializer armoryRuleSerializer;
	private CannonRuleSerializer cannonRuleSerializer;
	private TurretRuleSerializer turretRuleSerializer;
	
	private Rule<TownHallAttribute> townHallRule;
	private Rule<LikeGeneratorAttribute> likeGeneratorRule;
	private Rule<DislikeGeneratorAttribute> dislikeGeneratorRule;
	private Rule<LikeDepositAttribute> likeDepositRule;
	private Rule<DislikeDepositAttribute> dislikeDepositRule;
	private Rule<ArmoryAttribute> armoryRule;
	private Rule<CannonAttribute> cannonRule;
	private Rule<TurretAttribute> turretRule;
	
	private BuyStructureCommand buyStructureCommand;
	private RaidCommand	raidCommand;
	
	private BlockDataFiler blockDataFiler;
	private PlayerDataFiler playerDataFiler;
	private TownsFiler townsFiler;
	
	private MacroidService macroidService;
	
	private PluginManager pluginManager;
	
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
		pluginManager = getServer().getPluginManager();
		
		registerResourceBundles();
		
		super.onEnable();
		
		registerWorlds();
		registerServices();
		registerFilers();
		registerRuleSerializers();
		
		registerCommands();
		registerListeners();
		registerPacketInterceptors();
		
		scheduleTasks();
		
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
		var cannonRule = cannonRuleSerializer.load();
		var turretRule = turretRuleSerializer.load();
		
		this.townHallRule = townHallRule;
		this.likeGeneratorRule = likeGeneratorRule;
		this.dislikeGeneratorRule = dislikeGeneratorRule;
		this.likeDepositRule = likeDepositRule;
		this.dislikeDepositRule = dislikeDepositRule;
		this.armoryRule = armoryRule;
		this.cannonRule = cannonRule;
		this.turretRule = turretRule;
		
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
		structureDamageCalculator = new StructureDamageCalculator(this);
		
		macroidService = new MacroidService(this);
	}
	
	private void registerRuleSerializers()
	{
		townHallRuleSerializer = new TownHallRuleSerializer(this);
		likeGeneratorRuleSerializer = new LikeGeneratorRuleSerializer(this);
		dislikeGeneratorRuleSerializer = new DislikeGeneratorRuleSerializer(this);
		likeDepositRuleSerializer = new LikeDepositRuleSerializer(this);
		dislikeDepositRuleSerializer = new DislikeDepositRuleSerializer(this);
		armoryRuleSerializer = new ArmoryRuleSerializer(this);
		cannonRuleSerializer = new CannonRuleSerializer(this);
		turretRuleSerializer = new TurretRuleSerializer(this);
	}
	
	private void registerPacketInterceptors()
	{
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
	}
	
	private void scheduleTasks()
	{
		SpawnProtectionTask spawnProtectionTask = new SpawnProtectionTask(this);
		
		spawnProtectionTask.runTaskTimer(this, 20L, 20L);
	}
	
	private void registerCommands()
	{
		getCommand("admin").setExecutor(new AdminCommand(this));
		getCommand("balance").setExecutor(new BalanceCommand(this));
		getCommand("buystructure").setExecutor(buyStructureCommand = new BuyStructureCommand(this));
		getCommand(DelHomeCommand.COMMAND_NAME).setExecutor(new DelHomeCommand(this));
		getCommand(HomeCommand.COMMAND_NAME).setExecutor(new HomeCommand(this));
		getCommand("macroid").setExecutor(new MacroidCommand(this));
		getCommand("raid").setExecutor(raidCommand = new RaidCommand(this));
		getCommand("rulereload").setExecutor(new ReloadRuleCommand(this));
		getCommand(SetHomeCommand.COMMAND_NAME).setExecutor(new SetHomeCommand(this));
		getCommand(SpawnCommand.COMMAND_NAME).setExecutor(new SpawnCommand(this));
		getCommand(TagCommand.COMMAND_NAME).setExecutor(new TagCommand(this));
		getCommand("test").setExecutor(new TestCommand(this));
		getCommand(TownCommand.COMMAND_NAME).setExecutor(new TownCommand(this));
//		getCommand(TutorialCommand.COMMAND_NAME).setExecutor(new TutorialCommand(this));
	}
	
	private void registerListeners()
	{
		registerContainerListeners();
		registerHotbarListeners();
		registerInventoryListeners();
		registerPlayerListeners();
		registerScoreboardListeners();
		registerStructureListeners();
		
		pluginManager.registerEvents(new AssistantHideListener(this), this);
		pluginManager.registerEvents(new AssistantInteractListener(this), this);
		pluginManager.registerEvents(new AssistantTargetTaskListener(this), this);
		pluginManager.registerEvents(new BlockDataSaveListener(this), this);
		pluginManager.registerEvents(new ChannelingTaskListener(this), this);
		pluginManager.registerEvents(new ChatListener(this), this);
		pluginManager.registerEvents(new ConfirmationGuiListener(this), this);
		pluginManager.registerEvents(new EntityItemMoveListener(this), this);
		pluginManager.registerEvents(new EntityRecyclerListener(this), this);
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
		pluginManager.registerEvents(new MacroidListener(this), this);
		pluginManager.registerEvents(new PlayerLoaderListener(this), this);
		pluginManager.registerEvents(new PlayerManagerListener(this), this);
		pluginManager.registerEvents(new SpawnProtectionListener(this), this);
		pluginManager.registerEvents(new TownHardnessListener(this), this);
		pluginManager.registerEvents(new TownAntiGrieffingListener(this), this);
		pluginManager.registerEvents(new FrozenTownListener(this), this);
	}
	
	private void registerContainerListeners()
	{
		pluginManager.registerEvents(new CancelDropOnDeathListener(this), this);
		pluginManager.registerEvents(new FakeShulkerBulletListener(this), this);
		pluginManager.registerEvents(new FakeVillagerListener(this), this);
	}
	
	private void registerHotbarListeners()
	{
		pluginManager.registerEvents(new RaidAnalyzerHotbarListener(this), this);
		pluginManager.registerEvents(new RaidSpectatorHotbarListener(this), this);
		pluginManager.registerEvents(new DefenderSpectatorHotbarListener(this), this);
	}
	
	private void registerInventoryListeners()
	{
		pluginManager.registerEvents(new ArmoryGuiListener(this), this);
		pluginManager.registerEvents(new ArmoryMenuGuiListener(this), this);
		pluginManager.registerEvents(new ArmoryMyInventoryMenuGuiListener(this), this);
		pluginManager.registerEvents(new ArmorySelectOrStorageItemsGuiListener(this), this);
		pluginManager.registerEvents(new ArmoryWeaponShopGuiListener(this), this);
		pluginManager.registerEvents(new AssistantGuiListener(this), this);
		pluginManager.registerEvents(new TownHallGuiListener(this), this);
	}
	
	private void registerPlayerListeners()
	{
		registerTutorialStepListeners();
		
		pluginManager.registerEvents(new AttackerListener(this), this);
		pluginManager.registerEvents(new DefenderListener(this), this);
		pluginManager.registerEvents(new FighterListener(this), this);
		pluginManager.registerEvents(new InventoryOrganizerPlayerListener(this), this);
		pluginManager.registerEvents(new InvincibleListener(this), this);
		pluginManager.registerEvents(new PluginPlayerListener(this), this);
		pluginManager.registerEvents(new ProtectableListener(this), this);
		pluginManager.registerEvents(new SpectatorListener(this), this);
		pluginManager.registerEvents(new TutorialTownPlayerListener(this), this);
		pluginManager.registerEvents(new VanillaListener(this), this);
	}
	
	private void registerTutorialStepListeners()
	{
		pluginManager.registerEvents(new AssistantStepListener(this), this);
		pluginManager.registerEvents(new ClickToFinishTownHallStepListener(this), this);
		pluginManager.registerEvents(new CollectDislikesStepListener(this), this);
		pluginManager.registerEvents(new CollectLikesStepListener(this), this);
		pluginManager.registerEvents(new UpgradeTownHallStepListener(this), this);
		pluginManager.registerEvents(new WaitFinishTownHallStepListener(this), this);
	}
	
	private void registerScoreboardListeners()
	{
		pluginManager.registerEvents(new AnalyzerScoreboardListener(this), this);
		pluginManager.registerEvents(new TownScoreboardListener(this), this);
	}
	
	private void registerStructureListeners()
	{
		pluginManager.registerEvents(new ArmoryListener(this), this);
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
				Town town = townManager.getTown(uuid);
				town.loadAndPasteTownSchematic();
				town.getStructureMap().values().forEach(Structure::onBuildCorruptedTown);
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
