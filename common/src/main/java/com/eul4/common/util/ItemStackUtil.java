package com.eul4.common.util;

import lombok.experimental.UtilityClass;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@UtilityClass
public class ItemStackUtil
{
	public static boolean hasAttackSpeed(ItemStack itemStack)
	{
		if(itemStack == null)
		{
			return false;
		}
		
		ItemMeta meta = itemStack.getItemMeta();
		
		if(meta == null)
		{
			return false;
		}
		
		return meta.getAttributeModifiers(Attribute.GENERIC_ATTACK_SPEED) != null;
	}
}
