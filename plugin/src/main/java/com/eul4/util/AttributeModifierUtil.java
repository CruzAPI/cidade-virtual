package com.eul4.util;

import lombok.experimental.UtilityClass;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;

@UtilityClass
public final class AttributeModifierUtil
{
	public static AttributeModifier of(double amount, EquipmentSlot equipmentSlot)
	{
		return new AttributeModifier(UUID.randomUUID(), "", amount, AttributeModifier.Operation.ADD_NUMBER, equipmentSlot);
	}
}
