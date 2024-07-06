package com.eul4.util;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

import static org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER;

@UtilityClass
public final class AttributeModifierUtil
{
	public static AttributeModifier of(double amount, EquipmentSlot equipmentSlot)
	{
		return new AttributeModifier(NamespacedKey.randomKey(), amount, ADD_NUMBER, equipmentSlot.getGroup());
	}
}
