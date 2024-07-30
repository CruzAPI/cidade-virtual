package com.eul4.wrapper;

import com.eul4.Main;
import com.eul4.common.util.ContainerUtil;
import com.eul4.enums.PluginNamespacedKey;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class StructureItemMove
{
	public static Optional<Structure> findStructure(ItemStack item, Main plugin)
	{
		return Optional.ofNullable(item)
				.map(ItemStack::getItemMeta)
				.map(ItemMeta::getPersistentDataContainer)
				.map(container -> fromContainer(container, plugin));
	}
	
	public static Structure getStructure(ItemStack item, Main plugin)
	{
		return findStructure(item, plugin).orElse(null);
	}
	
	private static Structure fromContainer(PersistentDataContainer container, Main plugin)
	{
		UUID townUniqueId = ContainerUtil.getUUID(container, PluginNamespacedKey.TOWN_UUID);
		UUID structureUniqueId = ContainerUtil.getUUID(container, PluginNamespacedKey.STRUCTURE_ITEM_MOVE_UUID);
		
		if(townUniqueId == null || structureUniqueId == null)
		{
			return null;
		}
		
		Town town = plugin.getTownManager().getTown(townUniqueId);
		
		return town.getStructureByUniqueId(structureUniqueId);
	}
}
