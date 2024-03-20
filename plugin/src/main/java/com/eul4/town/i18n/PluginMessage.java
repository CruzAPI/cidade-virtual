package com.eul4.town.i18n;

import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.Message;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.ResourceBundle;
import java.util.function.BiFunction;

import static com.eul4.common.i18n.CommonMessage.USAGE;

public class PluginMessage extends Message
{
	public static final PluginMessage
	
	COMMAND_TOWN_USAGE = new PluginMessage("command.town.usage",
	new ComponentBuilder("").color(ChatColor.RED).create()[0],
	(bundle, args) -> new ComponentBuilder("")
	.append(USAGE.translate(bundle.getLocale()).toLegacyText())
	.append(args[0].toString())
	.create());
	
	private PluginMessage(String key)
	{
		super(PluginBundleBaseName.PLUGIN, key);
	}
	
	private PluginMessage(String key, BaseComponent baseComponent)
	{
		super(PluginBundleBaseName.PLUGIN, key, baseComponent);
	}
	
	private PluginMessage(String key,
			BaseComponent baseComponent,
			BiFunction<ResourceBundle, Object[], BaseComponent[]> componentBiFunction)
	{
		super(PluginBundleBaseName.PLUGIN, key, baseComponent, componentBiFunction);
	}
	
	private PluginMessage(BundleBaseName bundleBaseName, String key)
	{
		super(bundleBaseName, key);
	}
	
	private PluginMessage(BundleBaseName bundleBaseName, String key, BaseComponent baseComponent)
	{
		super(bundleBaseName, key, baseComponent);
	}
	
	private PluginMessage(BundleBaseName bundleBaseName,
			String key,
			BaseComponent baseComponent,
			BiFunction<ResourceBundle, Object[], BaseComponent[]> componentBiFunction)
	{
		super(bundleBaseName, key, baseComponent, componentBiFunction);
	}
}
