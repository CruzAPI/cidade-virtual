package com.eul4.model.player;

import com.eul4.common.wrapper.UUIDUtil;
import com.eul4.enums.PluginNamespacedKey;
import com.eul4.model.town.Town;
import com.eul4.wrapper.TownAttack;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public interface Fighter extends PluginPlayer, SpiritualPlayer
{
	Town getAttackedTown();
	
	void setLastValidLocation(Location location);
	Location getLastValidLocation();
	
	default boolean isOutside()
	{
		Town town = getAttackedTown();
		
		if(town == null)
		{
			return false;
		}
		
		return !town.getBoundingBoxExcludingWalls().contains(getPlayer().getBoundingBox());
	}
	
	default TownAttack getTownAttack()
	{
		return Optional.ofNullable(getAttackedTown()).map(Town::getCurrentAttack).orElse(null);
	}
	
	default void equipBattleInventory()
	{
		PlayerInventory inventory = getPlayer().getInventory();
		
		findTown().flatMap(Town::findArmory)
				.ifPresentOrElse(armory -> inventory.setContents(armory.getBattleInventoryContents()),
						inventory::clear);
		
		ItemStack[] contents = inventory.getContents();
		
		for(int i = 0; i < contents.length; i++)
		{
			ItemStack content = contents[i];
			
			if(content == null || !content.hasItemMeta())
			{
				continue;
			}
			
			ItemMeta meta = content.getItemMeta();
			var container = meta.getPersistentDataContainer();
			container.set(PluginNamespacedKey.BATTLE_INVENTORY_OWNER, PersistentDataType.LONG_ARRAY, UUIDUtil.uuidToLongArray(getUniqueId()));
			content.setItemMeta(meta);
			
			inventory.setItem(i, content);
		}
	}
	
	default void cleanAndSaveBattleInventory()
	{
		Player player = getPlayer();
		PlayerInventory inventory = player.getInventory();
		ItemStack[] contents = inventory.getContents();
		
		for(int i = 0; i < contents.length; i++)
		{
			ItemStack item = contents[i];
			
			if(item == null || !item.hasItemMeta())
			{
				inventory.setItem(i, null);
				continue;
			}
			
			ItemMeta meta = item.getItemMeta();
			var container = meta.getPersistentDataContainer();
			long[] bits = container.get(PluginNamespacedKey.BATTLE_INVENTORY_OWNER, PersistentDataType.LONG_ARRAY);
			
			if(bits == null || !UUIDUtil.fromLongArray(bits).equals(getUniqueId()))
			{
				inventory.setItem(i, null);
				continue;
			}
			
			container.remove(PluginNamespacedKey.BATTLE_INVENTORY_OWNER);
			item.setItemMeta(meta);
			inventory.setItem(i, item);
		}
		
		findTown().flatMap(Town::findArmory).ifPresent(armory -> armory.setBattleInventory(inventory));
	}
}
