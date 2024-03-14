package com.eul4.i18n;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ResourceBundle;
import java.util.function.BiFunction;

import static com.eul4.i18n.CommonMessage.USAGE;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class PluginMessage extends Message
{
	public static final PluginMessage
	
	COMMAND_TOWN_USAGE = new PluginMessage("command.town.usage", empty().color(RED), (bundle, args) -> new Component[]
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
