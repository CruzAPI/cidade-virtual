package com.eul4.common.constant;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;

@UtilityClass
public class CommonNamespacedKey
{
	private static final String NAMESPACE = "common";
	
	public static final NamespacedKey CANCEL_DROP = new NamespacedKey(NAMESPACE, "cancel_drop");
	public static final NamespacedKey CANCEL_MOVE = new NamespacedKey(NAMESPACE, "cancel_move");
}
