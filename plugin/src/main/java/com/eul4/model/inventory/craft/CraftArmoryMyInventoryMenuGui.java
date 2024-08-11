package com.eul4.model.inventory.craft;

import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.ArmoryMyInventoryMenuGui;
import com.eul4.model.town.structure.Armory;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftArmoryMyInventoryMenuGui extends CraftGui implements ArmoryMyInventoryMenuGui
{
	private final ItemStack organizeBattleInventory;
	private final ItemStack transferBlocks;
	private final ItemStack selectOrStorageItems;
	
	@Getter
	private final Armory armory;
	
	public CraftArmoryMyInventoryMenuGui(CommonPlayer commonPlayer, Armory armory)
	{
		//TODO change title
		super(commonPlayer, () -> commonPlayer.createInventory(InventoryType.HOPPER, PluginMessage.INVENTORY_ARMORY_MENU_TITLE));
		
		this.armory = armory;
		
		ItemMeta meta;
		
		ItemStack glass = new ItemStack(Material.GLASS_PANE);
		meta = glass.getItemMeta();
		meta.displayName(Component.empty());
		glass.setItemMeta(meta);
		
		organizeBattleInventory = new ItemStack(Material.BOOKSHELF);
		meta = organizeBattleInventory.getItemMeta();
		meta.displayName(PluginMessage.INVENTORY_ARMORY_MY_INVENTORY_MENU_ARRANGE.translateOne(commonPlayer));
		meta.lore(PluginMessage.INVENTORY_ARMORY_MY_INVENTORY_MENU_ARRANGE_LORE.translateLore(commonPlayer));
		organizeBattleInventory.setItemMeta(meta);
		
		//TODO
		transferBlocks = new ItemStack(Material.HOPPER	);
		meta = transferBlocks.getItemMeta();
//todo		meta.displayName(PluginMessage.INVENTORY_ARMORY_MENU_MY_INVENTORY.translate(commonPlayer));
		transferBlocks.setItemMeta(meta);
		
		selectOrStorageItems = new ItemStack(Material.BARREL);
		meta = selectOrStorageItems.getItemMeta();
		meta.displayName(PluginMessage.INVENTORY_ARMORY_MY_INVENTORY_MENU_SELECTOR.translateOne(commonPlayer));
		meta.lore(PluginMessage.INVENTORY_ARMORY_MY_INVENTORY_MENU_SELECTOR_LORE.translateLore(commonPlayer));
		selectOrStorageItems.setItemMeta(meta);
		
		inventory.setContents(new ItemStack[]
		{
//			organizeBattleInventory, glass, transferBlocks, glass, selectOrStorageItems
			glass, organizeBattleInventory, glass, selectOrStorageItems, glass
		});
	}
	
	@Override
	public void updateTitle()
	{
	
	}
	
	@Override
	public ItemStack getOrganizeBattleInventoryIcon()
	{
		return organizeBattleInventory.clone();
	}
	
	@Override
	public ItemStack getTransferBlocksIcon()
	{
		return transferBlocks.clone();
	}
	
	@Override
	public ItemStack getSelectOrStorageItemsIcon()
	{
		return selectOrStorageItems.clone();
	}
}
