package com.eul4.i18n;

import com.eul4.StructureType;
import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.Message;
import com.eul4.common.wrapper.TimerTranslater;
import com.eul4.enums.Currency;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Generator;
import com.eul4.rule.attribute.*;
import com.eul4.util.MessageUtil;
import com.eul4.util.TickConverter;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

import static com.eul4.common.i18n.CommonMessage.USAGE;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.STRIKETHROUGH;

@Getter
public enum PluginMessage implements Message
{
	LEVEL("level"),
	UPGRADE("upgrade"),
	DURABILITY("durability"),
	PRICE("price"),
	ABBREVIATION_LEVEL("abbreviation.level"),
	STRUCTURE_ARMORY_NAME("structure.armory.name"),
	STRUCTURE_CANNON_NAME("structure.cannon.name"),
	STRUCTURE_TURRET_NAME("structure.turret.name"),
	STRUCTURE_TOWN_HALL_NAME ("structure.town-hall.name"),
	STRUCTURE_LIKE_GENERATOR_NAME("structure.like-generator.name"),
	STRUCTURE_DISLIKE_GENERATOR_NAME("structure.dislike-generator.name"),
	STRUCTURE_LIKE_DEPOSIT_NAME("structure.like-deposit.name"),
	STRUCTURE_DISLIKE_DEPOSIT_NAME("structure.dislike-deposit.name"),
	COLLECT_LIKES("collect-likes", empty().color(GREEN)),
	CLICK_TO_BUY_THIS_TILE("click-to-buy-this-tile", empty().decorate(BOLD)),
	
	TOWN_COMMAND_NAME("town.command.name"),
	SPAWN_COMMAND_NAME("spawn.command.name"),
	COMMAND_BUY_STRUCTURE_ALIASES("command.buy-structure.aliases"),
	
	INVENTORY_ARMORY_MENU_TITLE("inventory.armory-menu.title", empty().color(BLACK).decorate(BOLD)),
	INVENTORY_ARMORY_MENU_SHOP("inventory.armory-menu.shop", empty().color(GREEN)),
	INVENTORY_ARMORY_MENU_MY_INVENTORY("inventory.armory-menu.my-inventory", empty().color(GOLD)),
	
	INVENTORY_ARMORY_WEAPON_SHOP_TITLE("inventory.armory-weapon-shop.title", empty().color(BLACK).decorate(BOLD)),
	
	INVENTORY_GENERATOR_COLLECT("inventory.generator.collect", (bundle, args) ->
	{
		Generator generator = (Generator) args[0];
		
		return new Component[]
		{
			empty().color(WHITE),
			generator.getCurrency().getBaseComponent()
					.append(generator.getCurrency().getPluralWord().translateWord(bundle, String::toUpperCase))
					.append(text(":"))
					.decorate(BOLD),
			text(generator.getBalance()),
			text(generator.getCapacity()),
		};
	}),
	
	INVENTORY_GENERATOR_COLLECT_LORE_DEPOSIT_FULL("inventory.generator.collect.lore.deposit-full", (bundle, args) ->
	{
		Generator generator = (Generator) args[0];
		
		return new Component[]
		{
			empty().color(RED),
			generator.getCurrency().getBaseComponent()
					.append(generator.getCurrency().getPluralWord().translateWord(bundle, String::toUpperCase))
					.decorate(BOLD),
		};
	}),
	
	INVENTORY_GENERATOR_COLLECT_LORE("inventory.generator.collect.lore", (bundle, args) ->
	{
		Generator generator = (Generator) args[0];
		
		return new Component[]
		{
			empty().color(GRAY),
			generator.getCurrency().getBaseComponent()
					.append(text(generator.getPossibleAmountToCollect()))
					.append(text(" "))
					.append(generator.getCurrency().getPluralWord().translateWord(bundle, String::toUpperCase))
					.decorate(BOLD),
		};
	}),
	
	GENERATOR_COLLECT("generator.collect", (bundle, args) ->
	{
		Generator generator = (Generator) args[0];
		int amountCollected = (int) args[1];
		
		return new Component[]
		{
			empty().color(GRAY),
			generator.getCurrency().getBaseComponent()
					.append(text(amountCollected))
					.append(text(" "))
					.append(generator.getCurrency().getPluralWord().translateWord(bundle, String::toUpperCase))
					.decorate(BOLD),
		};
	}),
	
	GENERATOR_COLLECT_EMPTY("generator.collect.empty", (bundle, args) ->
	{
		Generator generator = (Generator) args[0];
		
		return new Component[]
		{
			empty().color(GRAY),
			generator.getCurrency().getBaseComponent()
					.append(generator.getCurrency().getPluralWord().translateWord(bundle, String::toUpperCase))
					.decorate(BOLD),
		};
	}),
	
	GENERATOR_COLLECT_DEPOSIT_FULLED("generator.collect.deposit-fulled", (bundle, args) ->
	{
		Generator generator = (Generator) args[0];
		
		return new Component[]
		{
			empty().color(GRAY),
			generator.getCurrency().getBaseComponent()
					.append(generator.getCurrency().getPluralWord().translateWord(bundle, String::toUpperCase))
					.decorate(BOLD),
		};
	}),
	
	GENERATOR_COLLECT_DEPOSIT_FULL("generator.collect.deposit-full", (bundle, args) ->
	{
		Generator generator = (Generator) args[0];
		
		return new Component[]
		{
			empty().color(GRAY),
			generator.getCurrency().getBaseComponent()
					.append(generator.getCurrency().getPluralWord().translateWord(bundle, String::toUpperCase))
					.decorate(BOLD),
		};
	}),
	
	STRUCTURE_GENERATOR_TITLE("structure.generator.title",
	(bundle, args) -> new Component[]
	{
		empty().color((TextColor) args[0]),
		((Message) args[1]).translateWord(bundle.getLocale()),
		ABBREVIATION_LEVEL.translateWord(bundle.getLocale(), String::toUpperCase),
		text((int) args[2]),
	}),
	
	STRUCTURE_TITLE("structure.title",
	(bundle, args) -> new Component[]
	{
		empty(),
		((Message) args[0]).translateWord(bundle.getLocale()),
		ABBREVIATION_LEVEL.translateWord(bundle.getLocale(), String::toUpperCase),
		text((int) args[1]),
	}),
	
	COMPONENT_STRUCTURE_TITLE("structure.title",
	(bundle, args) -> new Component[]
	{
		(Component) args[0],
		((Message) args[1]).translateWord(bundle.getLocale()),
		ABBREVIATION_LEVEL.translateWord(bundle.getLocale(), String::toUpperCase),
		text((int) args[2]),
	}),
	
	STRUCTURE_HOLOGRAM_TITLE("structure.hologram.title",
	(bundle, args) -> new Component[]
	{
		empty().decorate(BOLD),
		((StructureType) args[0]).getNameMessage().translateWord(bundle, String::toUpperCase).color(((StructureType) args[0]).getColor()),
		LEVEL.translateWord(bundle, String::toUpperCase),
		text((int) args[1]),
	}),
	
	HOLOGRAM_LIKE_FARM_LINE2("hologram.like-farm.line2",
	(bundle, args) -> new Component[]
	{
		empty().decorate(BOLD),
		text((int) args[0]),
		text((int) args[1]),
	}),
	
	COMMAND_TOWN_USAGE("command.town.usage",
	(bundle, args) -> new Component[]
	{
		empty().color(RED),
		USAGE.translate(bundle.getLocale()),
		text(args[0].toString()),
	}),
	
	COMMAND_SPAWN_USAGE("command.spawn.usage",
	(bundle, args) -> new Component[]
	{
		empty().color(RED),
		USAGE.translate(bundle.getLocale()),
		text(args[0].toString()),
	}),
	
	STRUCTURE_READY_IN("structure.ready-in",
	(bundle, args) -> new Component[]
	{
		empty(),
		TimerTranslater.translate((int) args[0], bundle.getLocale()),
	}),
	
	CLICK_TO_FINISH_BUILD("click-to-finish-build"),
	
	STRUCTURE_BUILD_FINISHED("structure.build-finished", empty().color(GREEN)),
	STRUCTURE_SCHEMATIC_NOT_FOUND("structure.schematic-not-found", empty().color(RED)),
	STRUCTURE_CAN_NOT_CONSTRUCT_HERE("structure.can-not-construct-here", empty().color(RED)),
	STRUCTURE_ALREADY_BUILT("structure.already-build", empty().color(RED)),
	STRUCTURE_NOT_READY_YET("structure.not-ready-yet", empty().color(RED)),
	
	STRUCTURE("structure"),
	VALUE("value"),
	CURRENCY("currency"),
	
	COMMAND_SETPRICE_USAGE("command.setprice.usage", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text(args[0].toString()),
		STRUCTURE.translate(bundle.getLocale()),
		LEVEL.translate(bundle.getLocale()),
		CURRENCY.translate(bundle.getLocale()),
		VALUE.translate(bundle.getLocale()),
	}),
	
	COMMAND_SETPRICE_VALUE_SET("command.setprice.value-set", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text(((StructureType) args[0]).name()),
		text((int) args[1]),
		text((double) args[2]),
		text(((Currency) args[3]).name()),
	}),
	
	STRUCTURE_TYPE_NOT_FOUND("structure-type-not-found", empty().color(RED)),
	LEVEL_MUST_BE_AN_INT("level-must-be-an-int", empty().color(RED)),
	CURRENCY_NOT_FOUND("currency-not-found", empty().color(RED)),
	VALUE_MUST_BE_A_DOUBLE("value-must-be-a-double", empty().color(RED)),
	FAILED_TO_SAVE_STRUCTURE_PRICES_FILE("failed-to-save-structure-prices-file", empty().color(RED)),
	
	MISSING_LIKES("missing-likes", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text((int) args[0]),
	}),
	
	MISSING_DISLIKES("missing-dislikes", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text((int) args[0]),
	}),
	
	MISSING_RESOURCES("missing-resources", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text((int) args[0]),
		translatable(((Material) args[1]).translationKey()),
	}),
	
	STRUCTURE_NOT_FOR_SALE("structure-not-for-sale", empty().color(RED)),
	STRUCTURE_SHOP_TITLE("inventory.structure-shop.title", empty().color(BLACK)),
	
	YOU_CAN_NOT_CONSTRUCT_OUTSIDE_YOUR_TOWN("command.buy-structure.can-not-construct-outside", empty().color(RED)),
	
	DECORATED_VALUE_CURRENCY("decorated-value-currency", (bundle, args) -> new Component[]
	{
		((Currency) args[0]).getBaseComponent(),
		text((int) args[1]),
		((Currency) args[0]).getPluralWord().translateWord(bundle),
	}),
	
	BOLD_DECORATED_VALUE_CURRENCY("bold-decorated-value-currency", (bundle, args) -> new Component[]
	{
		((Currency) args[0]).getBaseComponent().decorate(BOLD),
		text((int) args[1]),
		((Currency) args[0]).getPluralWord().translateWord(bundle, String::toUpperCase),
	}),
	
	LIKES("likes"),
	DISLIKES("dislikes"),
	
	CONFIRM_OPERATION("confirm-operation"),
	
	STRUCTURE_CONSTRUCTOR("structure-constructor", (bundle, args) -> new Component[]
	{
		empty(),
		((Message) args[0]).translateWord(bundle).color(LIGHT_PURPLE),
	}),
	
	STRUCTURE_CONSTRUCTOR_LORE("structure-constructor-lore", empty().color(GRAY)),
	
	STRUCTURE_MAX_UPGRADE_REACHED("structure-max-upgrade-reached", empty().color(RED)),
	STRUCTURE_UPGRADED_TO_LEVEL("structure-upgraded-to-level", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text((int) args[0]),
	}),
	
	CAN_ONLY_UPGRADE_WHEN_BUILT("can-only-upgrade-when-built", empty().color(RED)),
	
	REQUIRES_TOWN_HALL_LEVEL("requires-town-hall-level", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text((int) args[0]),
	}),
	
	STRUCTURE_LIMIT_REACHED("structure-limit-reached", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		((StructureType) args[0]).getNameMessage().translate(bundle),
		text((int) args[1]),
		text((int) args[2]),
	}),
	
	STRUCTURE_TOWN_HALL_UPGRADE_PREVIEW_LORE("structure.town-hall.upgrade-preview-lore", (bundle, args) ->
	{
		TownHallAttribute currentLevelAttributes = (TownHallAttribute) args[0];
		TownHallAttribute nextLevelAttributes = (TownHallAttribute) args[1];
		
		int buildTicks = nextLevelAttributes.getTotalBuildTicks();
		
		return new Component[]
		{
			empty().color(GRAY),
			text(currentLevelAttributes.getLikeCapacity()),
			text(nextLevelAttributes.getLikeCapacity()),
			text(currentLevelAttributes.getDislikeCapacity()),
			text(nextLevelAttributes.getDislikeCapacity()),
			TimerTranslater.translate(buildTicks, bundle),
		};
	}),
	
	STRUCTURE_ARMORY_UPGRADE_PREVIEW_LORE("structure.armory.upgrade-preview-lore", (bundle, args) ->
	{
		ArmoryAttribute currentLevelAttributes = (ArmoryAttribute) args[0];
		ArmoryAttribute nextLevelAttributes = (ArmoryAttribute) args[1];
		
		int buildTicks = nextLevelAttributes.getTotalBuildTicks();
		
		return new Component[]
		{
			empty().color(GRAY),
			//TODO ...
			TimerTranslater.translate(buildTicks, bundle),
		};
	}),
	
	STRUCTURE_CANNON_UPGRADE_PREVIEW_LORE("structure.cannon.upgrade-preview-lore", (bundle, args) ->
	{
		CannonAttribute currentLevelAttributes = (CannonAttribute) args[0];
		CannonAttribute nextLevelAttributes = (CannonAttribute) args[1];
		
		int buildTicks = nextLevelAttributes.getTotalBuildTicks();
		
		return new Component[]
		{
			empty().color(GRAY),
			//TODO ...
			TimerTranslater.translate(buildTicks, bundle),
		};
	}),
	
	STRUCTURE_TURRET_UPGRADE_PREVIEW_LORE("structure.turret.upgrade-preview-lore", (bundle, args) ->
	{
		TurretAttribute currentLevelAttributes = (TurretAttribute) args[0];
		TurretAttribute nextLevelAttributes = (TurretAttribute) args[1];
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(bundle.getLocale());
		DecimalFormat decimalFormat = new DecimalFormat("0.0", symbols);
		
		return new Component[]
		{
			empty().color(GRAY),
			text(decimalFormat.format(currentLevelAttributes.getAttackDamage())),
			text(decimalFormat.format(nextLevelAttributes.getAttackDamage())),
			text(decimalFormat.format(currentLevelAttributes.getAttackSpeedPerSecond())),
			text(decimalFormat.format(nextLevelAttributes.getAttackSpeedPerSecond())),
			text(decimalFormat.format(currentLevelAttributes.getMissileSpeedPerSecond())),
			text(decimalFormat.format(nextLevelAttributes.getMissileSpeedPerSecond())),
			text(decimalFormat.format(currentLevelAttributes.getRange())),
			text(decimalFormat.format(nextLevelAttributes.getRange())),
			TimerTranslater.translate(nextLevelAttributes.getTotalBuildTicks(), bundle),
		};
	}),
	
	STRUCTURE_LIKE_GENERATOR_UPGRADE_PREVIEW_LORE("structure.like-generator.upgrade-preview-lore", (bundle, args) ->
	{
		LikeGeneratorAttribute currentLevelAttributes = (LikeGeneratorAttribute) args[0];
		LikeGeneratorAttribute nextLevelAttributes = (LikeGeneratorAttribute) args[1];
		
		int buildTicks = nextLevelAttributes.getTotalBuildTicks();
		
		return new Component[]
		{
			empty().color(GRAY),
			text(currentLevelAttributes.getCapacity()),
			text(nextLevelAttributes.getCapacity()),
			text(TickConverter.generationPerHour(currentLevelAttributes.getDelay())),
			text(TickConverter.generationPerHour(nextLevelAttributes.getDelay())),
			TimerTranslater.translate(buildTicks, bundle),
		};
	}),
	
	STRUCTURE_DISLIKE_GENERATOR_UPGRADE_PREVIEW_LORE("structure.dislike-generator.upgrade-preview-lore", (bundle, args) ->
	{
		DislikeGeneratorAttribute currentLevelAttributes = (DislikeGeneratorAttribute) args[0];
		DislikeGeneratorAttribute nextLevelAttributes = (DislikeGeneratorAttribute) args[1];
		
		int buildTicks = nextLevelAttributes.getTotalBuildTicks();
		
		return new Component[]
		{
			empty().color(GRAY),
			text(currentLevelAttributes.getCapacity()),
			text(nextLevelAttributes.getCapacity()),
			text(TickConverter.generationPerHour(currentLevelAttributes.getDelay())),
			text(TickConverter.generationPerHour(nextLevelAttributes.getDelay())),
			TimerTranslater.translate(buildTicks, bundle),
		};
	}),
	
	STRUCTURE_LIKE_DEPOSIT_UPGRADE_PREVIEW_LORE("structure.like-deposit.upgrade-preview-lore", (bundle, args) ->
	{
		LikeDepositAttribute currentLevelAttributes = (LikeDepositAttribute) args[0];
		LikeDepositAttribute nextLevelAttributes = (LikeDepositAttribute) args[1];
		
		int buildTicks = nextLevelAttributes.getTotalBuildTicks();
		
		return new Component[]
		{
			empty().color(GRAY),
			text(currentLevelAttributes.getCapacity()),
			text(nextLevelAttributes.getCapacity()),
			TimerTranslater.translate(buildTicks, bundle),
		};
	}),
	
	STRUCTURE_DISLIKE_DEPOSIT_UPGRADE_PREVIEW_LORE("structure.dislike-deposit.upgrade-preview-lore", (bundle, args) ->
	{
		DislikeDepositAttribute currentLevelAttributes = (DislikeDepositAttribute) args[0];
		DislikeDepositAttribute nextLevelAttributes = (DislikeDepositAttribute) args[1];
		
		int buildTicks = nextLevelAttributes.getTotalBuildTicks();
		
		return new Component[]
		{
			empty().color(GRAY),
			text(currentLevelAttributes.getCapacity()),
			text(nextLevelAttributes.getCapacity()),
			TimerTranslater.translate(buildTicks, bundle),
		};
	}),
	
	UPGRADE_LOCKED("upgrade-locked.requires-town-hall-level", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text((int) args[0]),
	}),
	
	STRUCTURE_DEPOSIT_CAPACITY_HOLOGRAM("structure.deposit-capacity-hologram", (bundle, args) ->
	{
		Currency currency = ((Currency) args[1]);
		
		Component baseComponent = currency.getBaseComponent();
		
		return new Component[]
		{
			empty().color(WHITE).decorate(BOLD),
			baseComponent.append(text((int) args[0])),
			baseComponent.append(currency.getPluralWord().translateWord(bundle, String::toUpperCase))
		};
	}),
	
	THIS_BLOCK_WILL_EXCEED_HARDNESS_LIMIT("this-block-will-exceed-hardness-limit", empty().color(RED)),
	
	TITLE_SEARCHING("title.searching", empty().color(GRAY)),
	TITLE_OWNER_TOWN("title.owner-town", (bundle, args) -> new Component[]
	{
		empty().color(GRAY),
		MessageUtil.getOfflinePlayerDisplayName((OfflinePlayer) args[0]),
	}),
	
	SUBTITLE_SECONDS_TO_ANALYZE("subtitle.seconds-to-analyze", (bundle, args) -> new Component[]
	{
		empty(),
		text((int) args[0]),
	}),
	
	ANALYZING_TOWN("analyzing-town", (bundle, args) -> new Component[]
	{
		empty(),
		MessageUtil.getOfflinePlayerDisplayName((OfflinePlayer) args[0]),
	}),
	
	NO_TOWNS_FOUND("no-towns-found", empty().color(RED)),
	
	RAID_ANALYZER_HOTBAR_ATTACK("raid-analyzer.hotbar.attack", empty().color(WHITE)),
	
	RAID_ANALYZER_HOTBAR_REROLL("raid-analyzer.hotbar.reroll", (bundle, args) -> new Component[]
	{
		empty().color(GOLD),
	}),
	
	RAID_ANALYZER_HOTBAR_CANCEL("raid-analyzer.hotbar.cancel", empty().color(RED)),
	
	RAID_SPECTATOR_HOTBAR_DEFEND("raid-spectator.hotbar.defend", empty().color(GRAY)),
	
	RAID_SPECTATOR_HOTBAR_VANILLA("raid-spectator.hotbar.vanilla", empty().color(RED)),
	
	DEFENDER_SPECTATOR_HOTBAR_QUIT("defender-spectator.hotbar.quit", empty().color(RED)),
	
	TOWN_ATTACK_ALERT("town-attack-alert", (bundle, args) -> new Component[]
	{
		empty().color(GOLD),
		((Component) args[0]),
	}),
	
	TYPE_TOWN_TO_DEFEND("type-town-to-defend", (bundle, args) -> new Component[]
	{
		empty().color(GOLD),
		text((String) args[0]).color(AQUA),
	}),
	
	ATTACK_IS_OVER("attack-is-over", empty().color(GOLD)),
	
	CAN_NOT_LEAVE_ADMIN_WITH_NO_TOWN("command.admin.can-not-leave-without-town", empty().color(RED)),
	
	TITLE_RESPAWNED("title.respawned", empty().color(GREEN)),
	SUBTITLE_RESPAWNED("subtitle.respawned", empty().color(GRAY)),
	TITLE_DEFENDER_MODE("title.defender-mode", empty().color(GREEN)),
	
	TITLE_RESPAWNING_IN("title.respawning-in", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text((int) args[0]),
	}),
	
	STRUCTURE_HEALTH_POINTS("structure.health-points", (bundle, args) ->
	{
		DecimalFormat decimalFormat = new DecimalFormat("0.#", new DecimalFormatSymbols(bundle.getLocale()));
		
		return new Component[]
		{
			empty().color(WHITE).decorate(BOLD),
			text(decimalFormat.format(args[0])),
			text(decimalFormat.format(args[1])),
		};
	}),
	
	STRUCTURE_LIKE_GENERATOR_BALANCE("structure.like-generator.balance", (bundle, args) -> new Component[]
	{
		empty().color(WHITE).decorate(BOLD),
		text((int) args[0]),
	}),
	
	STRUCTURE_DISLIKE_GENERATOR_BALANCE("structure.dislike-generator.balance", (bundle, args) -> new Component[]
	{
		empty().color(WHITE).decorate(BOLD),
		text((int) args[0]),
	}),
	
	STRUCTURE_LIKE_DEPOSIT_BALANCE("structure.like-deposit.balance", (bundle, args) -> new Component[]
	{
		empty().color(WHITE).decorate(BOLD),
		text((int) args[0]),
	}),
	
	STRUCTURE_DISLIKE_DEPOSIT_BALANCE("structure.dislike-deposit.balance", (bundle, args) -> new Component[]
	{
		empty().color(WHITE).decorate(BOLD),
		text((int) args[0]),
	}),
	
	STRUCTURE_TOWN_HALL_VIRTUAL_LIKES("structure.town-hall.virtual-likes", (bundle, args) -> new Component[]
	{
		empty().color(GREEN).decorate(BOLD),
		text((int) args[0]),
	}),
	
	STRUCTURE_TOWN_HALL_VIRTUAL_DISLIKES("structure.town-hall.virtual-dislikes", (bundle, args) -> new Component[]
	{
		empty().color(RED).decorate(BOLD),
		text((int) args[0]),
	}),

	TOWN_UNDER_ATTACK("town-under-attack", (bundle, args) -> new Component[]
	{
		empty().color(RED).decorate(BOLD),
		(Component) args[0],
	}),

	YOU_ATTACKING_TOW("you-attacking-town", (bundle, args) -> new Component[]
	{
		empty().color(GREEN).decorate(BOLD),
		text((String) args[0]),
	}),
	
	UNLOCKS_AT_LEVEL("unlocks-at-level", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text((int) args[0]),
	}),
	
	INVENTORY_ARMORY_MY_INVENTORY_MENU_ARRANGE("inventory-armory-my-inventory-menu.arrange", Component.empty().color(YELLOW)),
	INVENTORY_ARMORY_MY_INVENTORY_MENU_ARRANGE_LORE("inventory-armory-my-inventory-menu.arrange.lore", Component.empty().color(GRAY)),
	
	INVENTORY_ARMORY_MY_INVENTORY_MENU_SELECTOR("inventory-armory-my-inventory-menu.selector", Component.empty().color(GOLD)),
	INVENTORY_ARMORY_MY_INVENTORY_MENU_SELECTOR_LORE("inventory-armory-my-inventory-menu.selector.lore", Component.empty().color(GRAY)),
	
	COMMAND_BALANCE_YOUR_RESOURCES("command.balance.your-resources", Component.empty().color(WHITE).decorate(BOLD)),
	
	COMMAND_BALANCE_TRY_TOWN_COMMAND("command.balance.try-town-command", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		TOWN_COMMAND_NAME.translateWord(bundle),
	}),
	
	COMMAND_BALANCE_FOOTER("command.balance.footer", Component.empty().color(GRAY).decorate(STRIKETHROUGH)),
	
	COMMAND_BALANCE("command.balance", (bundle, args) ->
	{
		Town town = (Town) args[0];
		
		return new Component[]
		{
			empty().color(WHITE),
			text(" ]   ").color(GRAY).decorate(STRIKETHROUGH),
			COMMAND_BALANCE_YOUR_RESOURCES.translate(bundle),
			text("   [ ").color(GRAY).decorate(STRIKETHROUGH),
			LIKES.translateWord(bundle, String::toUpperCase).color(GREEN).decorate(BOLD).append(text(":")),
			text(town.getLikes()),
			text(town.getLikeCapacity()),
			DISLIKES.translateWord(bundle, String::toUpperCase).color(RED).decorate(BOLD).append(text(":")),
			text(town.getDislikes()),
			text(town.getDislikeCapacity()),
			COMMAND_BALANCE_FOOTER.translate(bundle),
		};
	}),
	
	INVENTORY_ARMORY_STORAGE_TITLE("inventory-armory-storage.title"),
	
	TITLE_OPEN_YOUR_INVENTORY("title.open-your-inventory", empty().color(WHITE)),
	SUBTITLE_OPEN_YOUR_INVENTORY("subtitle.open-your-inventory", empty().color(GRAY)),
	
	TITLE_CREATING_TOWN("title.creating-town", empty().color(WHITE)),
	SUBTITLE_CREATING_TOWN("subtitle.creating-town", empty().color(GRAY)),
	
	TITLE_TOWN_WELCOME("title.town.welcome", (bundle, args) -> new Component[]
	{
		empty().color(GOLD),
		(Component) args[0],
	}),
	
	YOU_RECEIVED_YOUR_VANILLA_INVENTORY_BACK("you-received-your-vanilla-inventory-back"),
	
	BATTLE_INVENTORY_UPDATED("battle-inventory-updated", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		YOU_RECEIVED_YOUR_VANILLA_INVENTORY_BACK.translate(bundle),
	}),
	
	BATTLE_INVENTORY_UNCHANGED("battle-inventory-unchanged", (bundle, args) -> new Component[]
	{
		empty().color(YELLOW),
		YOU_RECEIVED_YOUR_VANILLA_INVENTORY_BACK.translate(bundle),
	}),
	
	STORAGE_AND_BATTLE_INVENTORY_UPDATED("storage-and-battle-inventory-updated", empty().color(GREEN)),
	EMPTY_STORAGE_AND_BATTLE_INVENTORY("empty-storage-and-battle-inventory", empty().color(RED)),
	ORGANIZE_EMPTY_BATTLE_INVENTORY("organize-empty-battle-inventory", empty().color(RED)),
	FAILED_TO_OPEN_STORAGE_AND_BATTLE_INVENTORY("failed-to-open-storage-and-battle-inventory", empty().color(RED)),
	
	WEAPON_PURCHASE_FAILED_STORAGE_FULL("weapon-purchase-failed-storage-full", empty().color(RED)),
	
	WEAPON_PURCHASE("weapon-purchase", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text("*").color(YELLOW)
				.append((Component) args[0])
				.append(text("*"))
	}),
	
	TOWN_WELCOME("town.welcome", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		(Component) args[0],
	}),
	
	TOWN_HINT_BUY_STRUCTURE_COMMAND("town.hint.buy-structure-command", (bundle, args) -> new Component[]
	{
		empty().color(GRAY),
		COMMAND_BUY_STRUCTURE_ALIASES.translateWord(bundle, string -> "/" + string).color(WHITE),
	}),
	
	COMMAND_TOWN_FAILED_TO_CREATE_TOWN("command.town.failed-to-create-town", empty().color(RED)),
	
	TILE_BOUGHT("tile-bought", empty().color(GREEN)),
	
	PURCHASE_INVALID_PRICE("purchase-invalid-price", empty().color(RED)),
	
	INVENTORY_STRUCTURE_GUI_MOVE_STRUCTURE("inventory.structure-gui.move-structure", empty().color(GRAY)),
	
	SPAWN_PROTECTION_ENABLED("spawn-protection.enabled", empty().color(GREEN)),
	
	SPAWN_PROTECTION_DISABLED("spawn-protection.disabled", empty().color(RED)),
	
	CAN_NOT_CREATE_PORTAL_HERE("can-not-create-portal-here", empty().color(RED)),
	
	TOWN_HINT_BACK_TO_SPAWN("town.hint-back-to-spawn", (bundle, args) -> new Component[]
	{
		empty().color(GRAY),
		SPAWN_COMMAND_NAME.translate(bundle, command -> "/" + command).color(WHITE),
	});
	
	private final String key;
	private final BundleBaseName bundleBaseName;
	private final BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction;
	
	PluginMessage(String key)
	{
		this(key, empty());
	}
	
	PluginMessage(String key, Component baseComponent)
	{
		this(key, (bundle, args) -> new Component[] { baseComponent });
	}
	
	PluginMessage(String key, BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		this(PluginBundleBaseName.PLUGIN, key, componentBiFunction);
	}
	
	PluginMessage(BundleBaseName bundleBaseName,
			String key,
			BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		this.bundleBaseName = bundleBaseName;
		this.key = key;
		this.componentBiFunction = componentBiFunction;
	}
}
