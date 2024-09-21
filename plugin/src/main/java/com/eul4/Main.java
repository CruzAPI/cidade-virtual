package com.eul4;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.eul4.command.*;
import com.eul4.common.Common;
import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.common.type.player.CommonWorldType;
import com.eul4.common.util.FileUtil;
import com.eul4.enums.Rarity;
import com.eul4.externalizer.filer.BlockDataFiler;
import com.eul4.externalizer.filer.PlayerDataFiler;
import com.eul4.externalizer.filer.TownsFiler;
import com.eul4.i18n.PluginBundleBaseName;
import com.eul4.interceptor.HeartParticleInterceptor;
import com.eul4.interceptor.HideEnchantInterceptor;
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
import com.eul4.listener.world.CommonLevelListener;
import com.eul4.listener.world.RaidLevelListener;
import com.eul4.listener.world.VanillaLevelListener;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.*;
import com.eul4.rule.serializer.*;
import com.eul4.service.*;
import com.eul4.task.RarityBossBarTask;
import com.eul4.task.SpawnProtectionTask;
import com.eul4.type.PluginWorldType;
import com.eul4.util.RarityUtil;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;
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
	private AttackCommand attackCommand;
	private ToggleCombatCommand	toggleCombatCommand;
	
	private ItemDamageAttributeListener itemDamageAttributeListener;
	
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
		registerFurnaceRecipes();
		
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
		protocolManager.addPacketListener(new HeartParticleInterceptor(this));
		protocolManager.addPacketListener(new HideEnchantInterceptor(this));
	}
	
	private void scheduleTasks()
	{
		SpawnProtectionTask spawnProtectionTask = new SpawnProtectionTask(this);
		
		spawnProtectionTask.runTaskTimer(this, 20L, 20L);
		new RarityBossBarTask(this).runTaskTimer(this, 0L, 1L);
	}
	
	private void registerCommands()
	{
		LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
		manager.registerEventHandler(LifecycleEvents.COMMANDS, event ->
		{
			final Commands commands = event.registrar();
			commands.register("brig", new BrigCommand());
		});
		
		registerCommand(new AdminCommand(this), AdminCommand.NAME_AND_ALIASES);
		
		registerCommand(new AdminCommand(this), AdminCommand.NAME_AND_ALIASES);
		registerCommand(new BalanceCommand(this), BalanceCommand.NAME_AND_ALIASES);
		registerCommand(new DebugCommand(this), DebugCommand.NAME_AND_ALIASES);
		registerCommand(buyStructureCommand = new BuyStructureCommand(this), BuyStructureCommand.NAME_AND_ALIASES);
		registerCommand(new DelHomeCommand(this), DelHomeCommand.NAME_AND_ALIASES);
		registerCommand(new EnchantCommand(this), EnchantCommand.NAME_AND_ALIASES);
		registerCommand(new HomeCommand(this), HomeCommand.NAME_AND_ALIASES);
		registerCommand(new MacroidCommand(this), MacroidCommand.NAME_AND_ALIASES);
		registerCommand(new NewbieCommand(this), NewbieCommand.NAME_AND_ALIASES);
		registerCommand(new RaidCommand(this), RaidCommand.NAME_AND_ALIASES);
		registerCommand(attackCommand = new AttackCommand(this), AttackCommand.NAME_AND_ALIASES);
		registerCommand(new ReloadRuleCommand(this), ReloadRuleCommand.NAME_AND_ALIASES);
		registerCommand(new SetHomeCommand(this), SetHomeCommand.NAME_AND_ALIASES);
		registerCommand(new SetRarityCommand(this), SetRarityCommand.NAME_AND_ALIASES);
		registerCommand(new SpawnCommand(this), SpawnCommand.NAME_AND_ALIASES);
		registerCommand(new TagCommand(this), TagCommand.NAME_AND_ALIASES);
		registerCommand(new TestCommand(this), TestCommand.NAME_AND_ALIASES);
		registerCommand(toggleCombatCommand = new ToggleCombatCommand(this), ToggleCombatCommand.NAME_AND_ALIASES);
		registerCommand(new TownCommand(this), TownCommand.NAME_AND_ALIASES);
		registerCommand(new TutorialCommand(this), TutorialCommand.NAME_AND_ALIASES);
		registerCommand(new WorldCommand(this), WorldCommand.NAME_AND_ALIASES);
	}
	
	private void registerFurnaceRecipes()
	{
		Iterator<Recipe> recipeIterator = getServer().recipeIterator();
		List<Recipe> recipesToAdd = new ArrayList<>();
		
		while(recipeIterator.hasNext())
		{
			Recipe recipe = recipeIterator.next();
			
			if(recipe instanceof FurnaceRecipe furnaceRecipe)
			{
				List<ItemStack> choices;
				
				if(furnaceRecipe.getInputChoice() instanceof RecipeChoice.MaterialChoice materialChoice)
				{
					choices = materialChoice.getChoices().stream().map(ItemStack::of).toList();
				}
				else if(furnaceRecipe.getInputChoice() instanceof RecipeChoice.ExactChoice exactChoice)
				{
					choices = exactChoice.getChoices();
				}
				else
				{
					continue;
				}
				
				for(Rarity rarity : Rarity.values())
				{
					choices.forEach(item -> RarityUtil.setRarity(item, rarity));
					RecipeChoice recipeChoice = new RecipeChoice.ExactChoice(choices);
					ItemStack result = RarityUtil.setRarity(furnaceRecipe.getResult(), rarity);
					
					recipesToAdd.add(new FurnaceRecipe
					(
						new NamespacedKey(this, rarity.name().toLowerCase(Locale.ROOT) + "_" + furnaceRecipe.key().value()),
						result,
						recipeChoice,
						furnaceRecipe.getExperience() * rarity.getScalarMultiplier(10.0F),
						furnaceRecipe.getCookingTime() * rarity.getScalarMultiplier(5)
					));
				}
			}
		}
		
		recipesToAdd.forEach(getServer()::addRecipe);
	}
	
	private void registerListeners()
	{
		registerContainerListeners();
		registerHotbarListeners();
		registerInventoryListeners();
		registerPlayerListeners();
		registerScoreboardListeners();
		registerStructureListeners();
		registerWorldListeners();
		
		pluginManager.registerEvents(new AnvilRarityListener(this), this);
		pluginManager.registerEvents(new AssistantHideListener(this), this);
		pluginManager.registerEvents(new AssistantInteractListener(this), this);
		pluginManager.registerEvents(new AssistantTargetTaskListener(this), this);
		pluginManager.registerEvents(new BlockDataSaveListener(this), this);
		pluginManager.registerEvents(new BlockRarityListener(this), this);
		pluginManager.registerEvents(new BowRarityListener(this), this);
		pluginManager.registerEvents(new BucketRarityListener(this), this);
		pluginManager.registerEvents(new ChannelingTaskListener(this), this);
		pluginManager.registerEvents(new ChatListener(this), this);
		pluginManager.registerEvents(new ConfirmationGuiListener(this), this);
		pluginManager.registerEvents(new ContainerRarityListener(this), this);
		pluginManager.registerEvents(new ContaintmentPickaxeListener(this), this);
		pluginManager.registerEvents(new CraftRarityListener(this), this);
		pluginManager.registerEvents(new DebugListener(this), this);
		pluginManager.registerEvents(new DecoratedPotRarityListener(this), this);
		pluginManager.registerEvents(new DefaultItemStackRarityListener(this), this);
		pluginManager.registerEvents(new ElytraRarityListener(this), this);
		pluginManager.registerEvents(new EnchantmentListener(this), this);
		pluginManager.registerEvents(new EntityItemMoveListener(this), this);
		pluginManager.registerEvents(new EntityRarityListener(this), this);
		pluginManager.registerEvents(new EntityRecyclerListener(this), this);
		pluginManager.registerEvents(new FishingRarityListener(this), this);
		pluginManager.registerEvents(new FluidRarityListener(this), this);
		pluginManager.registerEvents(new FrozenTownListener(this), this);
		pluginManager.registerEvents(new FurnaceRarityListener(this), this);
		pluginManager.registerEvents(new InventoryUpdateListener(this), this);
		pluginManager.registerEvents(new StructureListener(this), this);
		pluginManager.registerEvents(new StructureGuiListener(this), this);
		pluginManager.registerEvents(new StructureShopGuiListener(this), this);
		pluginManager.registerEvents(new TestListener(this), this);
		pluginManager.registerEvents(new GeneratorGuiListener(this), this);
		pluginManager.registerEvents(new HangRarityListener(this), this);
		pluginManager.registerEvents(new StructureMoveListener(this), this);
		pluginManager.registerEvents(new StructureRarityListener(this), this);
		pluginManager.registerEvents(new TownAttackListener(this), this);
		pluginManager.registerEvents(new TownListener(this), this);
		pluginManager.registerEvents(new TownSaveListener(this), this);
		pluginManager.registerEvents(new VillagerRarityListener(this), this);
		pluginManager.registerEvents(new ItemBuilderListener(this), this);
		pluginManager.registerEvents(itemDamageAttributeListener = new ItemDamageAttributeListener(this), this);
		pluginManager.registerEvents(new MacroidListener(this), this);
		pluginManager.registerEvents(new OreMinedAlertListener(this), this);
		pluginManager.registerEvents(new PlayerAttackSpeedListener(this), this);
		pluginManager.registerEvents(new PlayerLoaderListener(this), this);
		pluginManager.registerEvents(new PlayerManagerListener(this), this);
		pluginManager.registerEvents(new SmithingRarityListener(this), this);
		pluginManager.registerEvents(new SpawnProtectionListener(this), this);
		pluginManager.registerEvents(new TownHardnessListener(this), this);
		pluginManager.registerEvents(new TownAntiGrieffingListener(this), this);
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
	
	private void registerWorldListeners()
	{
		pluginManager.registerEvents(new CommonLevelListener(this), this);
		pluginManager.registerEvents(new RaidLevelListener(this), this);
		pluginManager.registerEvents(new VanillaLevelListener(this), this);
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
		playerDataFiler.saveMemoryPlayers();
		getServer().getWorlds().forEach(blockDataFiler::saveChunks);
		
		getLogger().info("Plugin disabled.");
	}
	
	@Override
	public CommonWorldType getMainWorldType()
	{
		return PluginWorldType.RAID_WORLD;
	}
}
