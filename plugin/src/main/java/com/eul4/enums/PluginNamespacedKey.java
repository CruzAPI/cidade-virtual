package com.eul4.enums;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;

@UtilityClass
public class PluginNamespacedKey
{
	private static final String NAMESPACE = "town";
	
	public static final NamespacedKey
	
	ITEM_BUILDER_ORDINAL = new NamespacedKey(NAMESPACE, "item_builder_ordinal"),
	MACROID_WAND_UUID = new NamespacedKey(NAMESPACE, "macroid_wand_uuid"),
	WEAPON_ITEM_ATTRIBUTE = new NamespacedKey(NAMESPACE, "weapon_item_attribute"),
	BATTLE_INVENTORY_OWNER = new NamespacedKey(NAMESPACE, "battle_inventory_owner"),
	FAKE_VILLAGER = new NamespacedKey(NAMESPACE, "fake_villager"),
	FAKE_SHULKER_BULLET = new NamespacedKey(NAMESPACE, "fake_shulker_bullet");
}
