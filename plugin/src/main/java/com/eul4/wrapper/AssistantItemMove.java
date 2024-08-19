package com.eul4.wrapper;

import com.eul4.common.i18n.MessageArgs;
import com.eul4.exception.CannotConstructException;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.Town;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.eul4.enums.PluginNamespacedKey.ENTITY_MOVE_ITEM_TYPE_ORDINAL;
import static org.bukkit.persistence.PersistentDataType.INTEGER;

public class AssistantItemMove extends EntityItemMove
{
	public AssistantItemMove(Town town)
	{
		super(town, ItemStack.of(Material.VILLAGER_SPAWN_EGG));
		
		ItemMeta meta = item.getItemMeta();
		
		meta.displayName(PluginMessage.MOVE_ASSISTANT_ITEM.translate(town.getPluginPlayer()));
		meta.lore(PluginMessage.MOVE_ASSISTANT_ITEM_LORE.translateLines(town.getPluginPlayer()));
		
		var container = meta.getPersistentDataContainer();
		
		container.set(ENTITY_MOVE_ITEM_TYPE_ORDINAL, INTEGER, getType().ordinal());
		
		item.setItemMeta(meta);
	}
	
	@Override
	public MessageArgs getInventoryFullMessageArgs()
	{
		return PluginMessage.MOVE_ASSISTANT_INVENTORY_FULL.withArgs();
	}
	
	@Override
	public Type getType()
	{
		return Type.ASSISTANT;
	}
	
	@Override
	public void move(Block block)
	{
		try
		{
			town.spawnAssistant(block);
			remove();
		}
		catch(CannotConstructException e)
		{
			town.getPluginPlayer().sendMessage(PluginMessage.MOVE_ASSISTANT_CAN_NOT_MOVE_HERE);
		}
	}
}
