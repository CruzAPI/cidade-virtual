package com.eul4.common.i18n;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.ResourceBundle;
import java.util.function.BiFunction;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class CommonMessage extends Message
{
	public static final CommonMessage
	
	ADMINISTRATOR = new CommonMessage("administrator"),
	PLAYER = new CommonMessage("player"),
	GAME_MODE_CHANGED = new CommonMessage("player-mode-changed", empty().color(GREEN),
	(bundle, args) -> new Component[]
	{
		((CommonMessage) args[1]).translateWord(bundle.getLocale(), String::toUpperCase).color((TextColor) args[0]),
	}),
	USAGE = new CommonMessage("usage", empty().color(RED));
	
	private CommonMessage(String key)
	{
		super(CommonBundleBaseName.COMMON, key);
	}
	
	private CommonMessage(String key, Component baseComponent)
	{
		super(CommonBundleBaseName.COMMON, key, baseComponent);
	}
	
	private CommonMessage(String key,
			Component baseComponent,
			BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		super(CommonBundleBaseName.COMMON, key, baseComponent, componentBiFunction);
	}
	
	private CommonMessage(BundleBaseName bundleBaseName, String key)
	{
		super(bundleBaseName, key);
	}
	
	private CommonMessage(BundleBaseName bundleBaseName, String key, Component baseComponent)
	{
		super(bundleBaseName, key, baseComponent);
	}
	
	private CommonMessage(BundleBaseName bundleBaseName,
			String key,
			Component baseComponent,
			BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		super(bundleBaseName, key, baseComponent, componentBiFunction);
	}
}
