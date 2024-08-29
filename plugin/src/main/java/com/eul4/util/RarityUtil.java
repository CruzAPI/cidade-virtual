package com.eul4.util;

import com.eul4.Main;
import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.enums.Rarity;
import com.eul4.service.BlockData;
import lombok.experimental.UtilityClass;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Optional;

import static com.eul4.enums.PluginNamespacedKey.RARITY;
import static org.bukkit.persistence.PersistentDataType.BYTE;

@UtilityClass
public class RarityUtil
{
	public static ItemStack setRarity(ItemStack item, Rarity rarity)
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
		
		var container = meta.getPersistentDataContainer();
		container.set(RARITY, BYTE, rarity.getId());
		meta.lore(rarity.getStylizedMessage().translateLines(ResourceBundleHandler.DEFAULT_LOCALE));
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static Rarity getRarity(Main plugin, Block block)
	{
		return Optional.of(plugin.getBlockDataFiler())
				.map(blockDataFiler -> blockDataFiler.loadBlockData(block))
				.map(BlockData::getRarity)
				.orElse(Rarity.DEFAULT_RARITY);
	}
	
	public static Optional<Rarity> findRarity(ItemStack item)
	{
		return Optional.ofNullable(item)
				.map(ItemStack::getItemMeta)
				.map(ItemMeta::getPersistentDataContainer)
				.map(RarityUtil::getRarityId)
				.map(Rarity::getRarityById);
	}
	
	public static boolean hasRarity(ItemStack item)
	{
		return findRarity(item).isPresent();
	}
	
	public static Rarity getRarityOrNull(ItemStack item)
	{
		return findRarity(item).orElse(null);
	}
	
	public static Rarity getRarity(ItemStack item)
	{
		return findRarity(item).orElse(Rarity.DEFAULT_RARITY);
	}
	
	public static Rarity getRarity(Entity entity)
	{
		return getRarity(entity.getPersistentDataContainer());
	}
	
	public static void setRarity(Entity entity, Rarity rarity)
	{
		entity.getPersistentDataContainer().set(RARITY, BYTE, rarity.getId());
	}
	
	private static Rarity getRarity(PersistentDataContainer container)
	{
		return Rarity.getRarityById(getRarityId(container));
	}
	
	private static byte getRarityId(PersistentDataContainer container)
	{
		return container.getOrDefault(RARITY, BYTE, (byte) 0);
	}
}
