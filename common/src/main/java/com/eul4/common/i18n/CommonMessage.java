package com.eul4.common.i18n;


import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.ResourceBundle;
import java.util.function.BiFunction;

public class CommonMessage extends Message
{
	public static final CommonMessage
	
	USAGE = new CommonMessage("usage", new ComponentBuilder().color(ChatColor.RED).build());
	
	private CommonMessage(String key)
	{
		super(CommonBundleBaseName.COMMON, key);
	}
	
	private CommonMessage(String key, BaseComponent baseComponent)
	{
		super(CommonBundleBaseName.COMMON, key, baseComponent);
	}
	
	private CommonMessage(String key,
			BaseComponent baseComponent,
			BiFunction<ResourceBundle, Object[], BaseComponent[]> componentBiFunction)
	{
		super(CommonBundleBaseName.COMMON, key, baseComponent, componentBiFunction);
	}
	
	private CommonMessage(BundleBaseName bundleBaseName, String key)
	{
		super(bundleBaseName, key);
	}
	
	private CommonMessage(BundleBaseName bundleBaseName, String key, BaseComponent baseComponent)
	{
		super(bundleBaseName, key, baseComponent);
	}
	
	private CommonMessage(BundleBaseName bundleBaseName,
			String key,
			BaseComponent baseComponent,
			BiFunction<ResourceBundle, Object[], BaseComponent[]> componentBiFunction)
	{
		super(bundleBaseName, key, baseComponent, componentBiFunction);
	}
}
