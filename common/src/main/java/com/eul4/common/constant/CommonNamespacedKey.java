package com.eul4.common.constant;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;

@UtilityClass
public class CommonNamespacedKey
{
	private static final String NAMESPACE = "common";
	
	public static final NamespacedKey
	CANCEL_DROP = new NamespacedKey(NAMESPACE, "cancel_drop"),
	CANCEL_MOVE = new NamespacedKey(NAMESPACE, "cancel_move"),
	CANCEL_SPAWN = new NamespacedKey(NAMESPACE, "remove_on_drop"),
	CANCEL_INTERACTION = new NamespacedKey(NAMESPACE, "cancel_interaction"),
	REMOVE_ON_CHUNK_LOAD = new NamespacedKey(NAMESPACE, "remove_on_chunk_load"),
	FAWE_IGNORE = new NamespacedKey(NAMESPACE, "fawe_ignore");
}
