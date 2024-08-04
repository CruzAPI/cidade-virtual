package com.eul4.wrapper;

import com.eul4.common.constant.CommonNamespacedKey;
import com.eul4.common.i18n.MessageArgs;
import com.eul4.common.util.ContainerUtil;
import com.eul4.enums.PluginNamespacedKey;
import com.eul4.model.town.Town;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public abstract class EntityItemMove
{
	public enum Type
	{
		ASSISTANT
	}
	
	protected final Town town;
	protected final ItemStack item;
	
	protected EntityItemMove(Town town, ItemStack item)
	{
		this.town = town;
		this.item = item;
		
		ItemMeta meta = item.getItemMeta();
		
		var container = meta.getPersistentDataContainer();
		
		final UUID uuid = ContainerUtil.setRandomUUID(container);
		
		ContainerUtil.setFlag(container, CommonNamespacedKey.CANCEL_MOVE);
		ContainerUtil.setFlag(container, CommonNamespacedKey.CANCEL_SWAP);
		ContainerUtil.setFlag(container, CommonNamespacedKey.CANCEL_INTERACTION);
		ContainerUtil.setFlag(container, PluginNamespacedKey.CANCEL_STRUCTURE_INTERACTION);
		ContainerUtil.setFlag(container, CommonNamespacedKey.REMOVE_ITEM_ON_PLAYER_JOIN);
		ContainerUtil.setFlag(container, CommonNamespacedKey.REMOVE_ITEM_ON_COMMON_PLAYER_REGISTER);
		
		item.setItemMeta(meta);
		town.getEntityItemMoveMap().put(uuid, this);
	}
	
	public ItemStack getItem()
	{
		return item.clone();
	}
	
	public boolean containsSimilar()
	{
		PlayerInventory inventory = town.getPlayer().getInventory();
		
		ItemStack[] contents = inventory.getContents();
		
		for(ItemStack content : contents)
		{
			if(isSimilar(content))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isSimilar(ItemStack item)
	{
		return getType(item) == getType();
	}
	
	private Type getType(ItemStack item)
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
		
		var container = meta.getPersistentDataContainer();
		
		Integer ordinal = container.get(PluginNamespacedKey.ENTITY_MOVE_ITEM_TYPE_ORDINAL, PersistentDataType.INTEGER);
		
		if(ordinal == null)
		{
			return null;
		}
		
		return Type.values()[ordinal];
	}
	
	public void addItemIfNotPresent()
	{
		PlayerInventory inventory = town.getPlayer().getInventory();
		
		if(!containsSimilar() && !inventory.addItem(item).isEmpty())
		{
			town.getPluginPlayer().sendMessage(getInventoryFullMessageArgs());
		}
	}
	
	public abstract MessageArgs getInventoryFullMessageArgs();
	public abstract Type getType();
	
	public abstract void move(Block block);
	
	public void remove()
	{
		PlayerInventory inventory = town.getPlayer().getInventory();
		ItemStack[] contents = inventory.getContents();
		
		for(int i = 0; i < contents.length; i++)
		{
			if(isSimilar(contents[i]))
			{
				inventory.setItem(i, null);
			}
		}
		
		town.getEntityItemMoveMap().remove(item);
	}
}
