package com.eul4.util;

import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.enums.Rarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Optional;

import static com.eul4.enums.PluginNamespacedKey.RARITY;
import static org.bukkit.persistence.PersistentDataType.BYTE;

public class RarityUtil
{
	public static void setRarity(ItemStack item, Rarity rarity)
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
		
		var container = meta.getPersistentDataContainer();
		container.set(RARITY, BYTE, rarity.getId());
		meta.lore(rarity.getStylizedMessage().translateLines(ResourceBundleHandler.DEFAULT_LOCALE));
		item.setItemMeta(meta);
	}
	
	public static Rarity getRarity(ItemStack item)
	{
		return Optional.ofNullable(item)
				.map(ItemStack::getItemMeta)
				.map(ItemMeta::getPersistentDataContainer)
				.map(RarityUtil::getRarityId)
				.map(Rarity::getRarityById)
				.orElse(Rarity.DEFAULT_RARITY);
	}
	
	private static byte getRarityId(PersistentDataContainer container)
	{
		return container.getOrDefault(RARITY, BYTE, (byte) 0);
	}
}
