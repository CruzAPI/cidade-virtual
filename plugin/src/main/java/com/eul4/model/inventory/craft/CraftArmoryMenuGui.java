package com.eul4.model.inventory.craft;

import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.ArmoryMenuGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftArmoryMenuGui extends CraftGui implements ArmoryMenuGui
{
	private final ItemStack shop;
	private final ItemStack myInventory;
	
	public CraftArmoryMenuGui(CommonPlayer commonPlayer)
	{
		super(commonPlayer, () -> commonPlayer.createInventory(InventoryType.HOPPER, PluginMessage.INVENTORY_ARMORY_MENU_TITLE));
		
		ItemMeta meta;
		
		ItemStack glass = new ItemStack(Material.GLASS_PANE);
		meta = glass.getItemMeta();
		meta.displayName(Component.empty());
		glass.setItemMeta(meta);
		
		shop = new ItemStack(Material.EMERALD);
		meta = shop.getItemMeta();
		meta.displayName(PluginMessage.INVENTORY_ARMORY_MENU_SHOP.translate(commonPlayer));
		shop.setItemMeta(meta);
		
		myInventory = new ItemStack(Material.CHEST);
		meta = myInventory.getItemMeta();
		meta.displayName(PluginMessage.INVENTORY_ARMORY_MENU_MY_INVENTORY.translate(commonPlayer));
		myInventory.setItemMeta(meta);
		
		inventory.setContents(new ItemStack[]
		{
			glass, shop, glass, myInventory, glass
		});
	}
	
	@Override
	public void updateTitle()
	{
	
	}
	
	@Override
	public ItemStack getShopIcon()
	{
		return shop.clone();
	}
	
	@Override
	public ItemStack getMyInventoryIcon()
	{
		return myInventory.clone();
	}
}
