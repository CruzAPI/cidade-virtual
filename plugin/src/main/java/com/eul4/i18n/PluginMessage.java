package com.eul4.i18n;

import com.eul4.StructureType;
import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.Message;
import com.eul4.common.wrapper.TimerTranslater;
import com.eul4.enums.Currency;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.ResourceBundle;
import java.util.function.BiFunction;

import static com.eul4.common.i18n.CommonMessage.USAGE;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

@Getter
public enum PluginMessage implements Message
{
	LEVEL("level"),
	ABBREVIATION_LEVEL("abbreviation.level"),
	STRUCTURE_TOWN_HALL_NAME ("structure.town-hall.name"),
	STRUCTURE_LIKE_GENERATOR_NAME("structure.like-generator.name"),
	STRUCTURE_DISLIKE_GENERATOR_NAME("structure.dislike-generator.name"),
	COLLECT_LIKES("collect-likes", empty().color(GREEN)),
	CLICK_TO_BUY_THIS_TILE("click-to-buy-this-tile", empty().decorate(BOLD)),
	
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
	
	STRUCTURE_HOLOGRAM_TITLE("structure.hologram.title",
	(bundle, args) -> new Component[]
	{
		empty().decorate(BOLD),
		((StructureType<?, ?>) args[0]).getNameMessage().translateWord(bundle, String::toUpperCase).color(((StructureType<?, ?>) args[0]).getColor()),
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
	
	STRUCTURE_NOT_FOR_SALE("structure-not-for-sale", empty().color(RED)),
	STRUCTURE_SHOP_TITLE("inventory.structure-shop.title", empty().color(BLACK)),
	
	YOU_CAN_NOT_CONSTRUCT_OUTSIDE_YOUR_TOWN("command.buy-structure.can-not-construct-outside", empty().color(RED)),
	
	DECORATED_VALUE_CURRENCY("decorated-value-currency", (bundle, args) -> new Component[]
	{
		(Component) args[0],
		text((int) args[1]),
		((Currency) args[2]).getPluralWord().translateWord(bundle),
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
		((StructureType<?, ?>) args[0]).getNameMessage().translate(bundle),
		text((int) args[1]),
		text((int) args[2]),
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
