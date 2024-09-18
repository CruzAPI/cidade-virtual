package com.eul4.i18n;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.command.HomeCommand;
import com.eul4.command.SetHomeCommand;
import com.eul4.command.TagCommand;
import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.Message;
import com.eul4.common.util.CommonMessageUtil;
import com.eul4.common.util.CommonWordUtil;
import com.eul4.common.wrapper.TimerTranslator;
import com.eul4.enums.Currency;
import com.eul4.enums.Rarity;
import com.eul4.model.player.SetHomePerformer;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Generator;
import com.eul4.rule.attribute.*;
import com.eul4.util.TickConverter;
import com.eul4.wrapper.Tag;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static com.eul4.common.i18n.CommonMessage.*;
import static com.eul4.common.util.CommonMessageUtil.*;
import static java.util.Collections.singletonList;
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
	COST("cost"),
	CROWN("crown"),
	CROWNS("crowns"),
	COMMON("common"),
	RARE("rare"),
	LEGENDARY("legendary"),
	RARITY("rarity"),
	
	ENCHANTMENT_STABILITY("enchantment.stability"),
	
	TAG_OWNER("tag.owner", empty().color(DARK_RED)),
	TAG_ADMIN("tag.admin", empty().color(RED)),
	TAG_VIP("tag.vip", empty().color(GREEN)),
	TAG_MAYOR("tag.mayor", empty().color(YELLOW)),
	TAG_ALPHA("tag.alpha", empty().color(DARK_PURPLE)),
	TAG_DEPUTY_MAYOR("tag.deputy-mayor", empty().color(YELLOW)),
	TAG_TOWNEE("tag.townee", empty().color(GRAY)),
	TAG_INDIGENT("tag.indigent", empty().color(DARK_GRAY)),
	
	RARITY_COMMON((locale, args) -> singletonList
	(
		COMMON.translate(locale, String::toUpperCase).style(Rarity.COMMON.getStyle())
	)),
	
	RARITY_RARE((locale, args) -> singletonList
	(
		RARE.translate(locale, String::toUpperCase).style(Rarity.RARE.getStyle())
	)),
	
	RARITY_LEGENDARY((locale, args) -> singletonList
	(
		LEGENDARY.translate(locale, String::toUpperCase).style(Rarity.LEGENDARY.getStyle())
	)),
	
	CROWN_DEPOSITS_INSUFFICIENT_CAPACITY("crown-deposits-insufficient-capacity", empty().color(RED)),
	CROWN_DEPOSIT_INSUFFICIENT_CAPACITY("crown-deposit-insufficient-capacity", empty().color(RED)),
	EXCEPTION_OPERATION("exception.operation", empty().color(RED)),
	
	CONTAINTMENT_PICKAXE_CAN_BREAK_ONLY_SPAWNERS("containtment-pickaxe.can-break-only-spawners", empty().color(RED)),
	INCOMPATIBLE_RARITY("incompatible-rarity", empty().color(RED)),
	ENDERCHEST_DISABLED_YOU_CAN_ONLY_PICKUP("enderchest-disabled.you-can-only-pickup", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		Component.translatable(Material.ENDER_CHEST.translationKey())
	}),
	
	COMMON_INCOMPATIBILITY_$TRANSLATABLE("common.container-incompatibility", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		((TranslatableComponent) args[0])
	}),
	RARE_INCOMPATIBILITY_$TRANSLATABLE("rare.container-incompatibility", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		((TranslatableComponent) args[0])
	}),
	LEGENDARY_INCOMPATIBILITY_$TRANSLATABLE("legendary.container-incompatibility", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		((TranslatableComponent) args[0])
	}),
	
	COMMON_PLACEMENT_INCOMPATIBILITY_$ITEM_$AGAINST("common.placement-incompatibility", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		((TranslatableComponent) args[0]),
		((TranslatableComponent) args[1])
	}),
	
	RARE_PLACEMENT_INCOMPATIBILITY_$ITEM_$AGAINST("rare.placement-incompatibility", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		((TranslatableComponent) args[0]),
		((TranslatableComponent) args[1])
	}),
	
	LEGENDARY_PLACEMENT_INCOMPATIBILITY_$ITEM_$AGAINST("legendary.placement-incompatibility", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		((TranslatableComponent) args[0]),
		((TranslatableComponent) args[1])
	}),
	
	BOLD_DECORATED_$CURRENCY_$VALUE((locale, args) ->
	{
		Currency currency = (Currency) args[0];
		Number value = (Number) args[1];
		Message word =  value.doubleValue() == 1.0D ? currency.getSingularWord() : currency.getPluralWord();
		DecimalFormat decimalFormat = currency.getDecimalFormat(locale);
		
		Component component = decimalToComponent(value, decimalFormat)
				.appendSpace()
				.append(word.translate(locale, String::toUpperCase))
				.style(currency.getStyle())
				.decorate(BOLD);
		
		return Collections.singletonList(component);
	}),
	
	FIREWORK_INCOMPATIBLE_RARITY("firework-incompatible-rarity", empty().color(RED)),
	ARROW_RARITY_HIGHER_THAN_BOW("arrow-rarity-higher-than-bow", empty().color(RED)),
	CONTAINTMENT_PICKAXE_DISPLAY_NAME("containtment-pickaxe.display-name", empty().color(YELLOW).decorate(BOLD)),
	
	WORLD_BLOCK_RARITY_RESTRICTION("world-block-rarity-restriction", empty().color(RED)),
	
	ANVIL_INSUFFICIENT_XP("anvil.insufficient-xp", empty().color(RED).decorate(BOLD)),
	
	ABBREVIATION_LEVEL("abbreviation.level"),
	STRUCTURE_ARMORY_NAME("structure.armory.name"),
	STRUCTURE_CANNON_NAME("structure.cannon.name"),
	STRUCTURE_TURRET_NAME("structure.turret.name"),
	STRUCTURE_TOWN_HALL_NAME ("structure.town-hall.name"),
	STRUCTURE_LIKE_GENERATOR_NAME("structure.like-generator.name"),
	STRUCTURE_DISLIKE_GENERATOR_NAME("structure.dislike-generator.name"),
	STRUCTURE_CROWN_DEPOSIT_NAME("structure.crown-deposit.name"),
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
			generator.getCurrency().getPluralWord().translate(bundle.getLocale(), String::toUpperCase)
					.append(text(":"))
					.style(generator.getCurrency().getStyle())
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
			generator.getCurrency().getPluralWord().translate(bundle, String::toUpperCase)
					.style(generator.getCurrency().getStyle())
					.decorate(BOLD),
		};
	}),
	
	INVENTORY_GENERATOR_COLLECT_LORE("inventory.generator.collect.lore", (bundle, args) ->
	{
		Generator generator = (Generator) args[0];
		
		return new Component[]
		{
			empty().color(GRAY),
			BOLD_DECORATED_$CURRENCY_$VALUE.translate(bundle, generator.getCurrency(), generator.getPossibleAmountToCollect()),
		};
	}),
	
	GENERATOR_COLLECT("generator.collect", (bundle, args) ->
	{
		Generator generator = (Generator) args[0];
		int amountCollected = (int) args[1];
		
		return new Component[]
		{
			empty().color(GRAY),
			BOLD_DECORATED_$CURRENCY_$VALUE.translate(bundle, generator.getCurrency(), amountCollected),
		};
	}),
	
	GENERATOR_COLLECT_EMPTY("generator.collect.empty", (bundle, args) ->
	{
		Generator generator = (Generator) args[0];
		
		return new Component[]
		{
			empty().color(GRAY),
			generator.getCurrency().getPluralWord().translate(bundle, String::toUpperCase)
					.style(generator.getCurrency().getStyle())
					.decorate(BOLD),
		};
	}),
	
	GENERATOR_COLLECT_DEPOSIT_FULLED("generator.collect.deposit-fulled", (bundle, args) ->
	{
		Generator generator = (Generator) args[0];
		
		return new Component[]
		{
			empty().color(GRAY),
			generator.getCurrency().getPluralWord().translate(bundle, String::toUpperCase)
					.style(generator.getCurrency().getStyle())
					.decorate(BOLD),
		};
	}),
	
	GENERATOR_COLLECT_DEPOSIT_FULL("generator.collect.deposit-full", (bundle, args) ->
	{
		Generator generator = (Generator) args[0];
		
		return new Component[]
		{
			empty().color(GRAY),
			generator.getCurrency().getPluralWord().translate(bundle, String::toUpperCase)
					.style(generator.getCurrency().getStyle())
					.decorate(BOLD),
		};
	}),
	
	STRUCTURE_GENERATOR_TITLE("structure.generator.title",
	(bundle, args) -> new Component[]
	{
		empty().color((TextColor) args[0]),
		((Message) args[1]).translate(bundle.getLocale()),
		ABBREVIATION_LEVEL.translate(bundle.getLocale(), String::toUpperCase),
		text((int) args[2]),
	}),
	
	STRUCTURE_TITLE("structure.title",
	(bundle, args) -> new Component[]
	{
		empty(),
		((Message) args[0]).translate(bundle.getLocale()),
		ABBREVIATION_LEVEL.translate(bundle.getLocale(), String::toUpperCase),
		text((int) args[1]),
	}),
	
	COMPONENT_STRUCTURE_TITLE("structure.title",
	(bundle, args) -> new Component[]
	{
		(Component) args[0],
		((Message) args[1]).translate(bundle.getLocale()),
		ABBREVIATION_LEVEL.translate(bundle.getLocale(), String::toUpperCase),
		text((int) args[2]),
	}),
	
	STRUCTURE_HOLOGRAM_TITLE("structure.hologram.title",
	(bundle, args) -> new Component[]
	{
		empty().decorate(BOLD),
		((StructureType) args[0]).getNameMessage().translate(bundle, String::toUpperCase).color(((StructureType) args[0]).getColor()),
		LEVEL.translate(bundle, String::toUpperCase),
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
	
	COMMAND_SPAWN_USAGE((locale, args) -> singletonList(
			USAGE.translate(locale, WordUtils::capitalize)
					.append(text(": /" + args[0]))
					.color(RED))),
	
	STRUCTURE_READY_IN("structure.ready-in",
	(bundle, args) -> new Component[]
	{
		empty(),
		TimerTranslator.translate((int) args[0], bundle.getLocale()),
	}),
	
	CLICK_TO_FINISH_BUILD("click-to-finish-build"),
	
	STRUCTURE_BUILD_FINISHED("structure.build-finished", empty().color(GREEN)),
	STRUCTURE_SCHEMATIC_NOT_FOUND("structure.schematic-not-found", empty().color(RED)),
	STRUCTURE_FAILED_CANCEL_MOVE("structure.failed-cancel-move", empty().color(RED)),
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
	
	LIKE("like"),
	DISLIKE("dislike"),
	
	LIKES("likes"),
	DISLIKES("dislikes"),
	
	CONFIRM_OPERATION("confirm-operation"),
	
	STRUCTURE_CONSTRUCTOR("structure-constructor", (bundle, args) -> new Component[]
	{
		empty(),
		((Message) args[0]).translate(bundle).color(LIGHT_PURPLE),
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
			TimerTranslator.translate(buildTicks, bundle),
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
			TimerTranslator.translate(buildTicks, bundle),
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
			TimerTranslator.translate(buildTicks, bundle),
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
			TimerTranslator.translate(nextLevelAttributes.getTotalBuildTicks(), bundle),
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
			TimerTranslator.translate(buildTicks, bundle),
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
			TimerTranslator.translate(buildTicks, bundle),
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
			TimerTranslator.translate(buildTicks, bundle),
		};
	}),
	
	STRUCTURE_CROWN_DEPOSIT_UPGRADE_PREVIEW_LORE("structure.crown-deposit.upgrade-preview-lore", (bundle, args) ->
	{
		CrownDepositAttribute currentLevelAttributes = (CrownDepositAttribute) args[0];
		CrownDepositAttribute nextLevelAttributes = (CrownDepositAttribute) args[1];
		
		Currency currency = currentLevelAttributes.getCurrency();
		DecimalFormat decimalFormat = currency.getDecimalFormat(bundle.getLocale());
		
		int buildTicks = nextLevelAttributes.getTotalBuildTicks();
		
		return new Component[]
		{
			empty().color(GRAY),
			text(decimalFormat.format(currentLevelAttributes.getCapacity())),
			text(decimalFormat.format(nextLevelAttributes.getCapacity())),
			TimerTranslator.translate(buildTicks, bundle),
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
			TimerTranslator.translate(buildTicks, bundle),
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
		
		return new Component[]
		{
			empty().color(WHITE).decorate(BOLD),
			BOLD_DECORATED_$CURRENCY_$VALUE.translate(bundle, currency, args[0])
		};
	}),
	
	THIS_BLOCK_WILL_EXCEED_HARDNESS_LIMIT("this-block-will-exceed-hardness-limit", empty().color(RED)),
	
	TITLE_SEARCHING("title.searching", empty().color(GRAY)),
	TITLE_OWNER_TOWN("title.owner-town", (bundle, args) -> new Component[]
	{
		empty().color(GRAY),
		CommonMessageUtil.getOfflinePlayerDisplayName((OfflinePlayer) args[0]),
	}),
	
	SUBTITLE_SECONDS_TO_ANALYZE("subtitle.seconds-to-analyze", (bundle, args) -> new Component[]
	{
		empty(),
		text((int) args[0]),
	}),
	
	ANALYZING_TOWN("analyzing-town", (bundle, args) -> new Component[]
	{
		empty(),
		CommonMessageUtil.getOfflinePlayerDisplayName((OfflinePlayer) args[0]),
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
	
	STRUCTURE_CROWN_DEPOSIT_BALANCE("structure.dislike-deposit.balance", (bundle, args) -> new Component[]
	{
		empty().color(WHITE).decorate(BOLD),
		decimalToComponent(args[0], Currency.CROWN.getDecimalFormat(bundle.getLocale())),
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
	
	COMMAND_WORLD_UNKNOWN_WORLD("command.world.unknown-world", Component.empty().color(RED)),
	COMMAND_WORLD_$LABEL("command.world.info", (bundle, args) -> new Component[]
	{
		empty().color(GRAY),
		argToComponent(args[0], bundle.getLocale())
	}),
	
	COMMAND_WORLD_USAGE_$ALIASES((locale, args) -> Collections.singletonList
	(
		text("/")
			.append(argToComponent(args[0]))
			.color(RED)
	)),
	
	COMMAND_WORLD_USE_$ALIASES((locale, args) -> Collections.singletonList
	(
		USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
			.appendNewline()
			.append(COMMAND_WORLD_USAGE_$ALIASES.translate(locale, args[0]))
			.color(RED)
	)),
	
	COMMAND_RAID_ALREADY_IN_WORLD("command.raid.already-in-world", Component.empty().color(RED)),
	COMMAND_NEWBIE_ALREADY_IN_WORLD("command.newbie.already-in-world", Component.empty().color(RED)),
	
	COMMAND_PRICE_USAGE_$ALIASES((locale, args) -> Collections.singletonList
	(
		text("/")
			.append(argToComponent(args[0]))
			.color(RED)
	)),
	
	COMMAND_PRICE_USE_$ALIASES((locale, args) -> Collections.singletonList
	(
		USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
			.appendNewline()
			.append(COMMAND_PRICE_USAGE_$ALIASES.translate(locale, args[0]))
			.color(RED)
	)),
	
	
	COMMAND_SELL_USAGE_$ALIASES((locale, args) -> Collections.singletonList
	(
		text("/")
			.append(argToComponent(args[0]))
			.color(RED)
	)),
	
	COMMAND_SELL_USE_$ALIASES((locale, args) -> Collections.singletonList
	(
		USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
			.appendNewline()
			.append(COMMAND_SELL_USAGE_$ALIASES.translate(locale, args[0]))
			.color(RED)
	)),
	
	COMMAND_RAID_USAGE_$ALIASES((locale, args) -> Collections.singletonList
	(
		text("/")
			.append(argToComponent(args[0]))
			.color(RED)
	)),
	
	COMMAND_RAID_USE_$ALIASES((locale, args) -> Collections.singletonList
	(
		USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
			.appendNewline()
			.append(COMMAND_RAID_USAGE_$ALIASES.translate(locale, args[0]))
			.color(RED)
	)),
	
	COMMAND_MUTEBROADCAST_USAGE_$ALIASES((locale, args) -> Collections.singletonList
	(
		text("/")
			.append(argToComponent(args[0]))
			.appendSpace()
			.append(usageRequiredArg(BROADCAST.translate(locale)))
			.color(RED)
	)),
	
	COMMAND_MUTEBROADCAST_USE_$ALIASES((locale, args) -> Collections.singletonList
	(
		USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
			.appendNewline()
			.append(COMMAND_MUTEBROADCAST_USAGE_$ALIASES.translate(locale, args[0]))
			.color(RED)
	)),
	
	COMMAND_NEWBIE_USAGE_$ALIASES((locale, args) -> Collections.singletonList
	(
		text("/")
			.append(argToComponent(args[0]))
			.color(RED)
	)),
	
	COMMAND_NEWBIE_USE_$ALIASES((locale, args) -> Collections.singletonList
	(
		USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
			.appendNewline()
			.append(COMMAND_NEWBIE_USAGE_$ALIASES.translate(locale, args[0]))
			.color(RED)
	)),

	COMMAND_BALANCE_YOUR_RESOURCES("command.balance.your-resources", Component.empty().color(WHITE).decorate(BOLD)),
	
	COMMAND_BALANCE_TRY_TOWN_COMMAND("command.balance.try-town-command", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		TOWN_COMMAND_NAME.translate(bundle),
	}),
	
	COMMAND_BALANCE_FOOTER("command.balance.footer", Component.empty().color(GRAY).decorate(STRIKETHROUGH)),
	
	COMMAND_MUTEBROADCAST_BROADCAST_MUTED("command.mutebroadcast.broadcast-muted", empty().color(GREEN)),
	
	COMMAND_MUTEBROADCAST_BROADCAST_ALREADY_MUTED("command.mutebroadcast.broadcast-already-muted", empty().color(RED)),
	
	COMMAND_MUTEBROADCAST_BROADCAST_NOT_FOUND("command.mutebroadcast.broadcast-not-found", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		argToComponent(args[0]),
	}),
	
	COMMAND_BALANCE("command.balance", (bundle, args) ->
	{
		Town town = (Town) args[0];
		
		return new Component[]
		{
			empty().color(WHITE),
			text(" ]   ").color(GRAY).decorate(STRIKETHROUGH),
			COMMAND_BALANCE_YOUR_RESOURCES.translate(bundle),
			text("   [ ").color(GRAY).decorate(STRIKETHROUGH),
			LIKES.translate(bundle, String::toUpperCase).color(GREEN).decorate(BOLD).append(text(":")),
			text(town.getLikes()),
			text(town.getLikeCapacity()),
			DISLIKES.translate(bundle, String::toUpperCase).color(RED).decorate(BOLD).append(text(":")),
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
		COMMAND_BUY_STRUCTURE_ALIASES.translate(bundle, string -> "/" + string).color(WHITE),
	}),
	
	COMMAND_TOWN_FAILED_TO_CREATE_TOWN("command.town.failed-to-create-town", empty().color(RED)),
	
	TILE_BOUGHT("tile-bought", empty().color(GREEN)),
	
	PURCHASE_INVALID_PRICE("purchase-invalid-price", empty().color(RED)),
	
	INVENTORY_STRUCTURE_GUI_MOVE_STRUCTURE("inventory.structure-gui.move-structure", empty().color(GRAY)),
	
	SPAWN_PROTECTION_ENABLED("spawn-protection.enabled", empty().color(GREEN)),
	SPAWN_PROTECTION_WARN("spawn-protection.warn", empty().color(YELLOW)),
	SPAWN_PROTECTION_DISABLED("spawn-protection.disabled", empty().color(RED)),
	
	CAN_NOT_CREATE_PORTAL_HERE("can-not-create-portal-here", empty().color(RED)),
	
	TOWN_HINT_BACK_TO_SPAWN("town.hint-back-to-spawn", (bundle, args) -> new Component[]
	{
		empty().color(GRAY),
		SPAWN_COMMAND_NAME.translate(bundle, command -> "/" + command).color(WHITE),
	}),
	
	STRUCTURE_ITEM_MOVE("structure.item-move", (bundle, args) ->
	{
		StructureType structureType = (StructureType) args[0];
		
		return new Component[]
		{
			empty().color(AQUA),
			structureType.getNameMessage().translate(bundle).color(structureType.getColor()),
		};
	}),
	
	STRUCTURE_ITEM_MOVE_LORE("structure.item-move.lore", empty().color(GRAY)),
	
	STRUCTURE_MOVE_INVENTORY_FULL("structure.move-inventory-full", empty().color(RED)),
	
	TOWN_VIRTUAL_ASSISTANT("town.virtual-assistant", empty().color(WHITE).decorate(BOLD)),
	
	INVENTORY_ASSISTANT_TITLE("inventory.assistant.title"),
	INVENTORY_ASSISTANT_MOVE_ASSISTANT("inventory.assistant.move-assistant"),
	INVENTORY_ASSISTANT_STRUCTURE_SHOP("inventory.assistant.structure-shop"),
	INVENTORY_ASSISTANT_BACK_TO_SPAWN("inventory.assistant.back-to-spawn"),
	
	INVENTORY_TOWN_HALL_ASSISTANT_MENU("inventory.town-hall.assistant-menu"),
	
	MOVE_ASSISTANT_ITEM("move-assistant.item"),
	MOVE_ASSISTANT_ITEM_LORE("move-assistant.item.lore", empty().color(GRAY)),
	MOVE_ASSISTANT_INVENTORY_FULL("move-assistant.inventory-full", empty().color(RED)),
	MOVE_ASSISTANT_CAN_NOT_MOVE_HERE("move-assistant.can-not-move-here", empty().color(RED)),
	
	STRUCTURE_LIKE_GENERATOR_SHOP_PREVIEW_ATTRIBUTES("structure.like-generator.shop-preview-attributes", (bundle, args) ->
	{
		LikeGeneratorAttribute attribute = (LikeGeneratorAttribute) StructureType.LIKE_GENERATOR.getRule((Main) args[0]).getAttributeOrDefault(1);
		
		return new Component[]
		{
			empty().color(GRAY),
			text(attribute.getCapacity()),
			text(TickConverter.generationPerHour(attribute.getDelay())),
			TimerTranslator.translate(attribute.getTotalBuildTicks(), bundle),
		};
	}),
	
	STRUCTURE_DISLIKE_GENERATOR_SHOP_PREVIEW_ATTRIBUTES("structure.dislike-generator.shop-preview-attributes", (bundle, args) ->
	{
		DislikeGeneratorAttribute attribute = (DislikeGeneratorAttribute) StructureType.DISLIKE_GENERATOR.getRule((Main) args[0]).getAttributeOrDefault(1);
		
		return new Component[]
		{
			empty().color(GRAY),
			text(attribute.getCapacity()),
			text(TickConverter.generationPerHour(attribute.getDelay())),
			TimerTranslator.translate(attribute.getTotalBuildTicks(), bundle),
		};
	}),
	
	STRUCTURE_LIKE_DEPOSIT_SHOP_PREVIEW_ATTRIBUTES("structure.like-deposit.shop-preview-attributes", (bundle, args) ->
	{
		LikeDepositAttribute attribute = (LikeDepositAttribute) StructureType.LIKE_DEPOSIT.getRule((Main) args[0]).getAttributeOrDefault(1);
		
		return new Component[]
		{
			empty().color(GRAY),
			text(attribute.getCapacity()),
			TimerTranslator.translate(attribute.getTotalBuildTicks(), bundle),
		};
	}),
	
	STRUCTURE_DISLIKE_DEPOSIT_SHOP_PREVIEW_ATTRIBUTES("structure.dislike-deposit.shop-preview-attributes", (bundle, args) ->
	{
		DislikeDepositAttribute attribute = (DislikeDepositAttribute) StructureType.DISLIKE_DEPOSIT.getRule((Main) args[0]).getAttributeOrDefault(1);
		
		return new Component[]
		{
			empty().color(GRAY),
			text(attribute.getCapacity()),
			TimerTranslator.translate(attribute.getTotalBuildTicks(), bundle),
		};
	}),
	
	STRUCTURE_CROWN_DEPOSIT_SHOP_PREVIEW_ATTRIBUTES("structure.crown-deposit.shop-preview-attributes", (bundle, args) ->
	{
		CrownDepositAttribute attribute = (CrownDepositAttribute) StructureType.CROWN_DEPOSIT.getRule((Main) args[0]).getAttributeOrDefault(1);
		
		return new Component[]
		{
			empty().color(GRAY),
			decimalToComponent(attribute.getCapacity(), attribute.getCurrency().getDecimalFormat(bundle.getLocale())),
			TimerTranslator.translate(attribute.getTotalBuildTicks(), bundle),
		};
	}),
	
	STRUCTURE_ARMORY_SHOP_PREVIEW_ATTRIBUTES("structure.armory.shop-preview-attributes", (bundle, args) ->
	{
		ArmoryAttribute attribute = (ArmoryAttribute) StructureType.ARMORY.getRule((Main) args[0]).getAttributeOrDefault(1);
		
		return new Component[]
		{
			empty().color(GRAY),
			TimerTranslator.translate(attribute.getTotalBuildTicks(), bundle),
		};
	}),
	
	STRUCTURE_TURRET_SHOP_PREVIEW_ATTRIBUTES("structure.turret.shop-preview-attributes", (bundle, args) ->
	{
		TurretAttribute attribute = (TurretAttribute) StructureType.TURRET.getRule((Main) args[0]).getAttributeOrDefault(1);
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(bundle.getLocale());
		DecimalFormat decimalFormat = new DecimalFormat("0.0", symbols);
		
		return new Component[]
		{
			empty().color(GRAY),
			text(decimalFormat.format(attribute.getAttackDamage())),
			text(decimalFormat.format(attribute.getAttackSpeedPerSecond())),
			text(decimalFormat.format(attribute.getMissileSpeedPerSecond())),
			text(decimalFormat.format(attribute.getRange())),
			TimerTranslator.translate(attribute.getTotalBuildTicks(), bundle),
		};
	}),
	
	STRUCTURE_CANNON_SHOP_PREVIEW_ATTRIBUTES("structure.cannon.shop-preview-attributes", (bundle, args) ->
	{
		CannonAttribute attribute = (CannonAttribute) StructureType.CANNON.getRule((Main) args[0]).getAttributeOrDefault(1);
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(bundle.getLocale());
		DecimalFormat decimalFormat = new DecimalFormat("0.0", symbols);
		//TODO....
		return new Component[]
		{
			empty().color(GRAY),
		};
	}),
	
	STRUCTURE_LIKE_GENERATOR_SHOP_LORE("structure.like-generator.shop-lore", empty().color(GRAY)),
	STRUCTURE_DISLIKE_GENERATOR_SHOP_LORE("structure.dislike-generator.shop-lore", empty().color(GRAY)),
	STRUCTURE_LIKE_DEPOSIT_SHOP_LORE("structure.like-deposit.shop-lore", empty().color(GRAY)),
	STRUCTURE_DISLIKE_DEPOSIT_SHOP_LORE("structure.dislike-deposit.shop-lore", empty().color(GRAY)),
	STRUCTURE_ARMORY_SHOP_LORE("structure.armory.shop-lore", empty().color(GRAY)),
	STRUCTURE_CANNON_SHOP_LORE("structure.cannon.shop-lore", empty().color(GRAY)),
	STRUCTURE_TURRET_SHOP_LORE("structure.turret.shop-lore", empty().color(GRAY)),
	
	INVENTORY_STRUCTURE_SHOP_GUI_STRUCTURE_DISPLAY_NAME((locale, args) ->
	{
		StructureType structureType = (StructureType) args[0];
		Town town = (Town) args[1];
		
		final int current = town.countStructures(structureType);
		final int max = town.getTownHall().getStructureLimitMap().getOrDefault(structureType, 0);
		
		NamedTextColor color = current >= max ? RED : GRAY;
		
		return singletonList(structureType.getNameMessage().translate(locale)
				.append(text(" "))
				.append(text("(" + current + "/" + max + ")").color(color)));
	}),
	
	COMMAND_PRICE_UNIT_PRICE_$MATERIAL_$PRICE("command.price.unit-price", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		argToComponent(args[0], "*", "*").color(YELLOW),
		decimalToComponent(args[1], "0.###", bundle.getLocale()).color(YELLOW),
	}),
	
	COMMAND_SELL_NEED_HOLD_ITEM("command.sell.need-hold-item", empty().color(RED)),
	COMMAND_SELL_ITEM_NOT_FOR_SALE("command.sell.item-not-for-sale", empty().color(RED)),
	COMMAND_SELL_SOLD_$MATERIAL_$AMOUNT_$VALUE("command.sell.sold", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		argToComponent(args[0], "*", "*").color(YELLOW),
		argToComponent(args[1]).color(YELLOW),
		decimalToComponent(args[2], "0.###", bundle.getLocale()).color(YELLOW),
	}),
	
	COMMAND_SETHOME_MAX_HOME_REACHED("command.sethome.max-home-reached", empty().color(RED)),
	COMMAND_SETHOME_NEED_TO_BE_IN_VANILLA("command.sethome.need-to-be-in-vanilla", empty().color(RED)),
	COMMAND_SETHOME_NEED_TO_BE_AWAY_$RADIUS("command.sethome.need-to-be-away-blocks-from-spawn", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		argToComponent(args[0])
	}),
	COMMAND_SETHOME_HOME_NAME_MUST_NOT_BE_BLANK("command.sethome.home-name-must-not-be-blank", empty().color(RED)),
	COMMAND_SETHOME_HOME_NAME_MAX_LENGTH("command.sethome.home-name-max-length", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text(SetHomePerformer.HOME_NAME_MAX_LENGTH)
	}),
	COMMAND_SETHOME_HOME_NAME_LETTERS_AND_NUMBERS("command.sethome.home-name-letters-and-numbers", empty().color(RED)),
	COMMAND_SETHOME_HOME_NAME_KEY_WORD("command.sethome.home-name-key-word", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text((String) args[0])
	}),
	COMMAND_SETHOME_HOME_ALREADY_SET("command.sethome.home-already-set", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text((String) args[0])
	}),
	COMMAND_SETHOME_USAGE("command.sethome.usage", empty().color(RED)),
	COMMAND_SETHOME_HOME_SET("command.sethome.home-set", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text("/" + HomeCommand.COMMAND_NAME + " " + args[0]).color(WHITE)
	}),
	COMMAND_SETHOME_CONFIRMATION_LORE("command.sethome.confirmation-lore", empty().color(GRAY)),
	
	COMMAND_DELHOME_CAN_NOT_DELETE_RESPAWN("command.delhome.can-not-delete-respawn-point", empty().color(RED)),
	COMMAND_DELHOME_USAGE("command.delhome.usage", empty().color(RED)),
	COMMAND_DELHOME_HOME_DELETED("command.delhome.home-deleted", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text("\"" + args[0] + "\"")
	}),
	
	COMMAND_HOME_TELEPORTED_TO_HOME("command.home.teleported-to-home", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		text("\"" + args[0] + "\"")
	}),
	COMMAND_HOME_HOME_NOT_FOUND("command.home.home-not-found", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text("\"" + args[0] + "\"")
	}),
	COMMAND_HOME_NO_HOME_FOUNDS("command.home.no-home-founds", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text("/" + SetHomeCommand.COMMAND_NAME)
	}),
	COMMAND_HOME_USAGE("command.home.usage", empty().color(RED)),
	
	COMMAND_HOME_YOUR_HOMES_HOVER("command.home.your-homes.hover", (bundle, args) -> new Component[]
	{
		empty().color(GRAY),
		text((String) args[0]),
	}),
	
	COMMAND_HOME_EMPTY_HOMES("command.home.empty-homes", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text("\"/" + SetHomeCommand.COMMAND_NAME + " <home>\""),
	}),
	
	COMMAND_HOME_RESPAWN_POINT_NOT_FOUND("command.home.respawn-point-not-found", empty().color(RED)),
	COMMAND_HOME_YOUR_HOMES("command.home.your-homes", (bundle, args) ->
	{
		Set<String> homeNames = (Set<String>) args[0];
		
		Component yourHomes = Component.empty().color(YELLOW);
		Iterator<String> iterator = homeNames.iterator();
		
		while(iterator.hasNext())
		{
			final String homeName = iterator.next();
			final String runCommand = "/" + HomeCommand.COMMAND_NAME + " " + homeName;
			final ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, runCommand);
			final HoverEventSource<Component> hoverEvent = COMMAND_HOME_YOUR_HOMES_HOVER.translate(bundle.getLocale(), homeName).asHoverEvent(UnaryOperator.identity());
			final Component homeNameComponent = text(homeName).clickEvent(clickEvent).hoverEvent(hoverEvent);
			
			yourHomes = yourHomes.append(homeNameComponent);
			
			if(iterator.hasNext())
			{
				yourHomes = yourHomes.append(text(", ").color(GREEN));
			}
		}
		
		return new Component[]
		{
			empty().color(GREEN),
			yourHomes,
		};
	}),
	
	CHANNELER_CHANNELING_CANCELLED("channeler.channeling-cancelled", empty().color(RED)),
	CHANNELER_CANCEL_REASON_SNEAK("channeler.cancel-reason.sneak", empty().color(RED)),
	CHANNELER_CANCEL_REASON_MOVE("channeler.cancel-reason.move", empty().color(RED)),
	CHANNELER_CANCEL_REASON_TAKE_DAMAGE("channeler.cancel-reason.take-damage", empty().color(RED)),
	CHANNELER_CANCEL_REASON_DEAL_DAMAGE("channeler.cancel-reason.deal-damage", empty().color(RED)),
	CHANNELER_CANCEL_REASON_DEAD("channeler.cancel-reason.dead", empty().color(RED)),
	CHANNELER_CANCEL_REASON_CHANGE_MODE("channeler.cancel-reason.change-mode", empty().color(RED)),
	CHANNELER_CANCEL_REASON_INTERACT("channeler.cancel-reason.interact", empty().color(RED)),
	CHANNELER_CANCEL_REASON_DROP_ITEM("channeler.cancel-reason.drop-item", empty().color(RED)),
	CHANNELER_CANCEL_REASON_PICKUP_ITEM("channeler.cancel-reason.pickup-item", empty().color(RED)),
	CHANNELER_CANCEL_REASON_OPEN_INVENTORY("channeler.cancel-reason.open-inventory", empty().color(RED)),
	CHANNELER_CANCEL_REASON_CHANNELING("channeler.cancel-reason.channeling", empty().color(RED)),
	CHANNELER_CANCEL_REASON_VEHICLE("channeler.cancel-reason.vehicle", empty().color(RED)),
	CHANNELER_CANCEL_REASON_PASSENGERS("channeler.cancel-reason.passengers", empty().color(RED)),
	CHANNELER_INITIALIZING_CHANNELING("channeler.initializing-channeling", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		TimerTranslator.translate(((Long) args[0]).intValue(), bundle)
	}),
	
	COMMAND_TAG_YOU_DO_NOT_HAVE_ANY_TAGS("command.tag.you-do-not-have-any-tags", empty().color(RED)),
	COMMAND_TAG_SELECT_TAG_HOVER("command.tag.select-tag.hover", empty().color(WHITE)),
	COMMAND_TAG_YOUR_TAGS("command.tag.your-tags", (bundle, args) ->
	{
		TreeSet<Tag> tags = (TreeSet<Tag>) args[0];
		
		Component yourTags = empty().color(WHITE);
		Iterator<Tag> iterator = tags.iterator();
		
		while(iterator.hasNext())
		{
			final Tag tag = iterator.next();
			final String runCommand = "/" + TagCommand.COMMAND_NAME + " " + tag.getName();
			final ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, runCommand);
			final HoverEvent<Component> hoverEvent = COMMAND_TAG_SELECT_TAG_HOVER.translate(bundle.getLocale()).asHoverEvent();
			final Component tagComponent = tag.getMessage().translate(bundle.getLocale()).clickEvent(clickEvent).hoverEvent(hoverEvent);
			
			yourTags = yourTags.append(tagComponent);
			
			if(iterator.hasNext())
			{
				yourTags = yourTags.append(text(", "));
			}
		}
		
		return new Component[]
		{
			empty().color(GREEN),
			yourTags,
		};
	}),
	COMMAND_TAG_TAG_NOT_FOUND("command.tag.tag-not-found", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text("\"" + args[0] + "\""),
	}),
	COMMAND_TAG_YOU_DO_NOT_HAVE_THIS_TAG("command.tag.you-do-not-have-this-tag", empty().color(RED)),
	COMMAND_TAG_TAG_SET("command.tag.tag-set", (bundle, args) -> new Component[]
	{
		empty().color(GREEN),
		((Tag) args[0]).getMessage().translate(bundle.getLocale()),
	}),
	
	COMMAND_TAG_SUB_COMMAND_CLEAR("command.tag.sub-command.clear"),
	COMMAND_TAG_SUB_COMMAND_HIDE("command.tag.sub-command.hide"),
	COMMAND_TAG_SUB_COMMAND_SHOW("command.tag.sub-command.show"),
	COMMAND_TAG_TAG_CLEARED("command.tag.tag-cleared", empty().color(GREEN)),
	COMMAND_TAG_NO_TAG_TO_CLEAR("command.tag.no-tag-to-clear", empty().color(RED)),
	COMMAND_TAG_HIDDEN("command.tag.tag-hidden", empty().color(GREEN)),
	COMMAND_TAG_SHOWN("command.tag.tag-shown", empty().color(GREEN)),
	COMMAND_TAG_ALREADY_HIDDEN("command.tag.tag-already-hidden", empty().color(RED)),
	COMMAND_TAG_ALREADY_SHOWN("command.tag.tag-already-shown", empty().color(RED)),
	
	COMMAND_TAG_USAGE((locale, args) ->
	{
		List<Component> components = new ArrayList<>();
		
		TextColor baseColor = RED;
		
		components.add(USAGE.translate(locale, WordUtils::capitalize).append(text(":")).color(baseColor));
		components.add(text("/" + args[0] + " [tag]").color(baseColor));
		components.add(text("/" + args[0] + " ").append(COMMAND_TAG_SUB_COMMAND_CLEAR.translate(locale)).color(baseColor));
		components.add(text("/" + args[0] + " ").append(COMMAND_TAG_SUB_COMMAND_HIDE.translate(locale)).color(baseColor));
		components.add(text("/" + args[0] + " ").append(COMMAND_TAG_SUB_COMMAND_SHOW.translate(locale)).color(baseColor));
		
		return components;
	}),
	
	PLAYER_RAID_SPECTATOR_CAN_NOT_RESPAWN_ANYMORE("player.raid-spectator.can-not-respawn-anymore", empty().color(RED)),
	
	COMMAND_COOLDOWN_$TICKS("command-cooldown", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		TimerTranslator.translate((long) args[0], bundle),
	}),
	
	COMMAND_TOGGLE_COMBAT_USAGE_$ALIASES((locale, args) -> Collections.singletonList
	(
		text("/")
		.append(argToComponent(args[0]))
		.color(RED)
	)),
	
	COMMAND_TOGGLE_COMBAT_USE_$ALIASES((locale, args) -> Collections.singletonList
	(
		USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
		.appendNewline()
		.append(COMMAND_TOGGLE_COMBAT_USAGE_$ALIASES.translate(locale, args[0]))
		.color(RED)
	)),
	
	COMMAND_SET_RARITY_RARITY_SET("command.set-rarity.rarity-set", (bundle, args) -> new Component[]
	{
		empty().color(GRAY),
		((Rarity) args[0]).getStylizedMessage().translate(bundle),
	}),
	COMMAND_SET_RARITY_RARITY_NOT_FOUND("command.set-rarity.rarity-not-found", empty().color(RED)),
	COMMAND_SET_RARITY_USAGE_$ALIASES((locale, args) -> singletonList
	(
		text("/")
		.append(argToComponent(args[0]))
		.appendSpace()
		.append(usageRequiredArg(RARITY.translate(locale)))
	)),
	
	COMMAND_SET_RARITY_USE_$ALIASES((locale, args) -> singletonList
	(
		USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
		.appendNewline()
		.append(COMMAND_SET_RARITY_USAGE_$ALIASES.translate(locale, args[0]))
	)),
	
	COMMAND_TRACK_USAGE_$ALIASES((locale, args) -> singletonList
	(
		text("/")
		.append(argToComponent(args[0]))
		.appendSpace()
		.append(usageOptionalArg(PLAYER.translate(locale)))
	)),
	
	COMMAND_TRACK_USE_$ALIASES((locale, args) -> singletonList
	(
		USAGE.translate(locale, CommonWordUtil::capitalizeAndConcatColon)
		.appendNewline()
		.append(COMMAND_TRACK_USAGE_$ALIASES.translate(locale, args[0]))
	)),
	
	COMMAND_TRACK_YOURSELF("command.track.yourself", empty().color(RED)),
	
	COMMAND_TRACK_TARGET_NOT_IN_RAID("command.track.target-not-in-raid", empty().color(RED)),
	
	COMMAND_AVAILABLE_ONLY_IN_RAID("command-available-only-in-raid", empty().color(RED)),
	
	COMMAND_TRACK_INVALID("command.track.invalid", (bundle, args) -> new Component[]
	{
		empty().color(RED),
		text("https://www.mcbrawl.com/wiki/tracking-raid/")
				.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://www.mcbrawl.com/wiki/tracking-raid/"))
				.color(GRAY)
	}),
	
	COMMAND_MUTEBROADCAST_CLICK_TO_MUTE("command.mutebroadcast.click-to-mute", empty().color(RED)),
	
	AUTO_$BROADCAST((locale, args) ->
	{
		BroadcastRichMessage broadcast = (BroadcastRichMessage) args[0];
		
		HoverEvent<Component> muteBroadcastHover = COMMAND_MUTEBROADCAST_CLICK_TO_MUTE.translate(locale).asHoverEvent();
		ClickEvent muteBroadcastClick = ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/mutebroadcast " + broadcast.name());
		
		Component muteBroadcast = Component.text("✕")
				.color(RED)
				.hoverEvent(muteBroadcastHover)
				.clickEvent(muteBroadcastClick);
		
		return singletonList
		(
				text("➟ ").color(GRAY)
						.append(usageOptionalArg(muteBroadcast).color(DARK_GRAY))
						.appendSpace()
						.append(broadcast.translate(locale))
		);
	}),
	;
	
	private final String key;
	private final BundleBaseName bundleBaseName;
	private final BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction;
	private final BiFunction<Locale, Object[], List<Component>> untranslatableComponentBiFunction;
	
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
		
		this.untranslatableComponentBiFunction = null;
	}
	
	PluginMessage(BiFunction<Locale, Object[], List<Component>> untranslatableComponentBiFunction)
	{
		this.untranslatableComponentBiFunction = untranslatableComponentBiFunction;
		
		this.bundleBaseName = null;
		this.key = null;
		this.componentBiFunction = null;
	}
}
