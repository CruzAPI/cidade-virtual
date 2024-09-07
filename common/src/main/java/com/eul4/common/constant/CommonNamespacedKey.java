package com.eul4.common.constant;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;

@UtilityClass
public class CommonNamespacedKey
{
	private static final String NAMESPACE = "common";
	
	public static final NamespacedKey
	CANCEL_DROP = new NamespacedKey(NAMESPACE, "cancel_drop"),
	CANCEL_SWAP = new NamespacedKey(NAMESPACE, "cancel_swap"),
	CANCEL_MOVE = new NamespacedKey(NAMESPACE, "cancel_move"),
	HIDE_ENTITY = new NamespacedKey(NAMESPACE, "hide_entity"),
	CANCEL_SPAWN = new NamespacedKey(NAMESPACE, "remove_on_drop"),
	CANCEL_INTERACTION = new NamespacedKey(NAMESPACE, "cancel_interaction"),
	REMOVE_ON_CHUNK_LOAD = new NamespacedKey(NAMESPACE, "remove_on_chunk_load"),
	REMOVE_ITEM_ON_PLAYER_JOIN = new NamespacedKey(NAMESPACE, "remove_item_on_player_join"),
	REMOVE_ITEM_ON_COMMON_PLAYER_REGISTER = new NamespacedKey(NAMESPACE, "remove_item_on_common_player_register"),
	FAWE_IGNORE = new NamespacedKey(NAMESPACE, "fawe_ignore"),
	ITEM_UUID = new NamespacedKey(NAMESPACE, "item_uuid"),
	UUID = new NamespacedKey(NAMESPACE, "uuid");
}
