package com.eul4.enums;

import com.eul4.i18n.PluginRichMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum MysthicType
{
	CONTAINTMENT_PICKAXE
	;
	
	public static MysthicType getMysthicType(ItemStack item)
	{
		if(item == null)
		{
			return null;
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if(meta == null)
		{
			return null;
		}
		
		Integer ordinal = item.getPersistentDataContainer().get(PluginNamespacedKey.MYSTHIC, PersistentDataType.INTEGER);
		
		if(ordinal == null)
		{
			return null;
		}
		
		MysthicType[] values = MysthicType.values();
		
		if(ordinal < values.length)
		{
			return values[ordinal];
		}
		
		return null;
	}
	
	public static ItemStack setMysthic(ItemStack item, MysthicType mysthicType, List<Component> lore)
	{
		if(item == null)
		{
			return null;
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if(meta == null)
		{
			return item;
		}
		
		PersistentDataContainer container = meta.getPersistentDataContainer();
		container.set(PluginNamespacedKey.MYSTHIC, PersistentDataType.INTEGER, mysthicType.ordinal());
		
		lore.add(PluginRichMessage.MYSTHIC_LABEL.translate());
		
		meta.lore(lore);
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	private static boolean isMysthic(ItemStack item)
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
		
		return item.getPersistentDataContainer().has(PluginNamespacedKey.MYSTHIC, PersistentDataType.INTEGER);
	}
}
