package com.eul4.i18n;

import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.Message;
import net.kyori.adventure.text.Component;

import java.util.ResourceBundle;
import java.util.function.BiFunction;

import static com.eul4.common.i18n.CommonMessage.USAGE;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public class PluginMessage extends Message
{
	public static final PluginMessage
	
	LEVEL = new PluginMessage("level"),
	
	COLLECT_LIKES = new PluginMessage("collect-likes", empty().color(GREEN)),
	
	STRUCTURE_TOWN_HALL_NAME = new PluginMessage("structure.town-hall.name"),
	
	STRUCTURE_LIKE_GENERATOR_NAME = new PluginMessage("structure.like-generator.name"),
	
	STRUCTURE_DISLIKE_GENERATOR_NAME = new PluginMessage("structure.dislike-generator.name"),
	
	STRUCTURE_GENERATOR_TITLE = new PluginMessage("structure.generator.title", empty().color(GREEN).decorate(BOLD),
	(bundle, args) -> new Component[]
	{
		((Message) args[0]).translateWord(bundle.getLocale(), String::toUpperCase),
		LEVEL.translateWord(bundle.getLocale(), String::toUpperCase),
		text((int) args[1]),
		text((int) args[2]),
		text((int) args[3]),
	}),
	
	STRUCTURE_TITLE = new PluginMessage("structure.title", empty(),
	(bundle, args) -> new Component[]
	{
		((Message) args[0]).translateWord(bundle.getLocale(), String::toUpperCase),
		LEVEL.translateWord(bundle.getLocale(), String::toUpperCase),
		text((int) args[1]),
	}),
	
	CLICK_TO_BUY_THIS_TILE = new PluginMessage("click-to-buy-this-tile", empty().decorate(BOLD)),
	
	HOLOGRAM_LIKE_FARM_LINE1 = new PluginMessage("hologram.like-farm.line1", empty().color(GREEN).decorate(BOLD),
	(bundle, args) -> new Component[]
	{
		text((int) args[0]),
	}),
	
	HOLOGRAM_LIKE_FARM_LINE2 = new PluginMessage("hologram.like-farm.line2", empty().decorate(BOLD),
	(bundle, args) -> new Component[]
	{
		text((int) args[0]),
		text((int) args[1]),
	}),
	
	COMMAND_TOWN_USAGE = new PluginMessage("command.town.usage", empty().color(RED),
	(bundle, args) -> new Component[]
	{
		USAGE.translate(bundle.getLocale()),
		text(args[0].toString()),
	});
	
	private PluginMessage(String key)
	{
		super(PluginBundleBaseName.PLUGIN, key);
	}
	
	private PluginMessage(String key, Component baseComponent)
	{
		super(PluginBundleBaseName.PLUGIN, key, baseComponent);
	}
	
	private PluginMessage(String key,
			Component baseComponent,
			BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		super(PluginBundleBaseName.PLUGIN, key, baseComponent, componentBiFunction);
	}
	
	private PluginMessage(BundleBaseName bundleBaseName, String key)
	{
		super(bundleBaseName, key);
	}
	
	private PluginMessage(BundleBaseName bundleBaseName, String key, Component baseComponent)
	{
		super(bundleBaseName, key, baseComponent);
	}
	
	private PluginMessage(BundleBaseName bundleBaseName,
			String key,
			Component baseComponent,
			BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		super(bundleBaseName, key, baseComponent, componentBiFunction);
	}
}
