package com.eul4.enums;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;

@UtilityClass
public class PluginNamespacedKey
{
	private static final String NAMESPACE = "town";
	
	public static final NamespacedKey ITEM_BUILDER_ORDINAL = new NamespacedKey(NAMESPACE, "item_builder_ordinal");
}
