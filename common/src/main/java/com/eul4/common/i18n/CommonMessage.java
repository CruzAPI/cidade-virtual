package com.eul4.common.i18n;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ResourceBundle;
import java.util.function.BiFunction;

public class CommonMessage extends Message
{
	public static final CommonMessage
	
	USAGE = new CommonMessage("usage", Component.empty().color(NamedTextColor.RED));
	
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
