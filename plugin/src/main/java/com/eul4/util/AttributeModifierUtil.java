package com.eul4.util;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER;

@UtilityClass
public final class AttributeModifierUtil
{
	public static AttributeModifier of(double amount, EquipmentSlot equipmentSlot)
	{
		return of(amount, ADD_NUMBER, equipmentSlot);
	}
	
	public static AttributeModifier of(double amount, AttributeModifier.Operation operation, EquipmentSlot equipmentSlot)
	{
		return new AttributeModifier(NamespacedKey.randomKey(), amount, operation, equipmentSlot.getGroup());
	}
	
	public static void set(ItemStack itemStack, Attribute attribute, AttributeModifier attributeModifier)
	{
		ItemMeta meta = itemStack.getItemMeta();
		
		if(meta == null)
		{
			return;
		}
		
		meta.removeAttributeModifier(attribute);
		meta.addAttributeModifier(attribute, attributeModifier);
		
		itemStack.setItemMeta(meta);
	}
}
