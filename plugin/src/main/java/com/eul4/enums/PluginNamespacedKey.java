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
	FAKE_SHULKER_BULLET = new NamespacedKey(NAMESPACE, "fake_shulker_bullet"),
	STRUCTURE_ITEM_MOVE_UUID = new NamespacedKey(NAMESPACE, "structure_item_move_uuid"),
	TOWN_UUID = new NamespacedKey(NAMESPACE, "town_uuid"),
	ENTITY_MOVE_ITEM_TYPE_ORDINAL = new NamespacedKey(NAMESPACE, "entity_move_item_type_ordinal"),
	CANCEL_STRUCTURE_INTERACTION = new NamespacedKey(NAMESPACE, "cancel_structure_interaction"),
	RARITY = new NamespacedKey(NAMESPACE, "rarity");
}
