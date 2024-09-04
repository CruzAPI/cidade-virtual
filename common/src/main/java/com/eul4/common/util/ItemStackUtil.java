package com.eul4.common.util;

import lombok.experimental.UtilityClass;
import net.minecraft.util.Mth;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import java.util.Collections;
import java.util.Map;

@UtilityClass
public class ItemStackUtil
{
	public static boolean isTool(ItemStack item)
	{
		return item != null && Tag.ITEMS_BREAKS_DECORATED_POTS.isTagged(item.getType());
	}
	
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
	
	public static void setDamage(ItemStack item, int damage)
	{
		if(item == null)
		{
			return;
		}
		
		if(!(item.getItemMeta() instanceof Damageable damageable))
		{
			return;
		}
		
		damageable.setDamage(Mth.clamp(damage, 0, getMaxDamage(item)));
		item.setItemMeta(damageable);
	}
	
	public static void clearEnchantments(ItemStack item)
	{
		if(item == null)
		{
			return;
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if(meta == null)
		{
			return;
		}
		
		meta.removeEnchantments();
		
		if(meta instanceof EnchantmentStorageMeta storageMeta)
		{
			for(Enchantment enchantment : storageMeta.getStoredEnchants().keySet())
			{
				storageMeta.removeStoredEnchant(enchantment);
			}
		}
		
		item.setItemMeta(meta);
	}
	
	public static int getMaxDamage(ItemStack item)
	{
		if(item == null)
		{
			return 0;
		}
		
		if(!(item.getItemMeta() instanceof Damageable damageable))
		{
			return 0;
		}
		
		return damageable.hasMaxDamage() ? damageable.getMaxDamage() : item.getType().getMaxDurability();
	}
	
	public static boolean hasEnchantments(ItemStack item)
	{
		return !getEnchantments(item).isEmpty();
	}
	
	public static Map<Enchantment, Integer> getEnchantments(ItemStack item)
	{
		if(item == null)
		{
			return Collections.emptyMap();
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if(meta == null)
		{
			return Collections.emptyMap();
		}
		
		if(meta instanceof EnchantmentStorageMeta enchantmentStorageMeta)
		{
			return enchantmentStorageMeta.getStoredEnchants();
		}
		
		return meta.getEnchants();
	}
	
	public static boolean addEnchant(ItemStack item, Enchantment ench, int level, boolean ignoreLevelRestriction)
	{
		if(item == null)
		{
			return false;
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if(meta == null)
		{
			return false;
		}
		
		boolean changed;
		
		if(item.getItemMeta() instanceof EnchantmentStorageMeta)
		{
			EnchantmentStorageMeta storage = ((EnchantmentStorageMeta) meta);
			changed = storage.addStoredEnchant(ench, level, ignoreLevelRestriction);
		}
		else
		{
			changed = meta.addEnchant(ench, level, ignoreLevelRestriction);
		}
		
		item.setItemMeta(meta);
		return changed;
	}
	
	public static int getRepairCost(ItemStack item)
	{
		return item != null && item.getItemMeta() instanceof Repairable secondRepairable
				? secondRepairable.getRepairCost()
				: 0;
	}
}
