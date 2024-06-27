package com.eul4.enums;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;

@UtilityClass
public class PluginNamespacedKey
{
	private static final String NAMESPACE = "town";
	
	public static final NamespacedKey ITEM_BUILDER_ORDINAL = new NamespacedKey(NAMESPACE, "item_builder_ordinal");
	public static final NamespacedKey MACROID_WAND_UUID = new NamespacedKey(NAMESPACE, "macroid_wand_uuid");
	public static final NamespacedKey WEAPON_ITEM_ATTRIBUTE = new NamespacedKey(NAMESPACE, "weapon_item_attribute");
	public static final NamespacedKey BATTLE_INVENTORY_OWNER = new NamespacedKey(NAMESPACE, "battle_inventory_owner");
}
