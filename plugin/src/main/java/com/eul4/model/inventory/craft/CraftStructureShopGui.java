package com.eul4.model.inventory.craft;

import com.eul4.Main;
import com.eul4.Price;
import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.ItemBuilder;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.StructureShopGui;
import com.eul4.model.player.PluginPlayer;
import com.eul4.util.MessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CraftStructureShopGui extends CraftGui implements StructureShopGui
{
	private final Map<ItemStack, ItemBuilder> itemStructureMap = new LinkedHashMap<>();
	
	private ItemStack likeGenerator;
	private ItemStack dislikeGenerator;
	private ItemStack likeDeposit;
	private ItemStack dislikeDeposit;
	private ItemStack armory;
	private ItemStack cannon;
	private ItemStack turret;
	
	public CraftStructureShopGui(CommonPlayer commonPlayer)
	{
		super(commonPlayer, commonPlayer.createInventory(9 * 6, PluginMessage.STRUCTURE_SHOP_TITLE));
		
		likeGenerator = new ItemStack(Material.LIME_STAINED_GLASS);
		setDisplayNameAndPriceInLore(likeGenerator, ItemBuilder.LIKE_GENERATOR);
		
		dislikeGenerator = new ItemStack(Material.RED_STAINED_GLASS);
		setDisplayNameAndPriceInLore(dislikeGenerator, ItemBuilder.DISLIKE_GENERATOR);
		
		likeDeposit = new ItemStack(Material.LIME_CONCRETE);
		setDisplayNameAndPriceInLore(likeDeposit, ItemBuilder.LIKE_DEPOSIT);
		
		dislikeDeposit = new ItemStack(Material.RED_CONCRETE);
		setDisplayNameAndPriceInLore(dislikeDeposit, ItemBuilder.DISLIKE_DEPOSIT);
		
		armory = new ItemStack(Material.IRON_BLOCK); //TODO: duplicated Material in ItemBuilder?? Probably one for icon and the other for be placed in ground.
		setDisplayNameAndPriceInLore(armory, ItemBuilder.ARMORY);
		
//		cannon = new ItemStack(Material.DISPENSER);
//		setDisplayNameAndPriceInLore(cannon, ItemBuilder.CANNON);
		
		turret = new ItemStack(Material.SMOOTH_STONE);
		setDisplayNameAndPriceInLore(turret, ItemBuilder.TURRET);
		
		itemStructureMap.keySet().forEach(getInventory()::addItem);
	}
	
	private void setDisplayNameAndPriceInLore(ItemStack item, ItemBuilder itemBuilder)
	{
		ItemMeta meta = item.getItemMeta();
		meta.displayName(PluginMessage.INVENTORY_STRUCTURE_SHOP_GUI_STRUCTURE_DISPLAY_NAME
				.translate(commonPlayer, itemBuilder.getStructureType(), ((PluginPlayer) commonPlayer).getTown()));
		
		Price price = itemBuilder.getStructureType().getRule((Main) commonPlayer.getPlugin())
				.getAttribute(1).getPrice();
		
		List<Component> lore = new ArrayList<>();
		
		if(price != null)
		{
			lore.addAll(MessageUtil.getPriceLore(price, commonPlayer));
			lore.add(Component.empty());
			lore.addAll(itemBuilder.getShopLoreMessage().translateLore(commonPlayer));
			lore.add(Component.empty());
			lore.addAll(itemBuilder.getShopPreviewAttributesMessage().translateLore(commonPlayer, commonPlayer.getPlugin()));
		}
		else
		{
			lore.add(PluginMessage.STRUCTURE_NOT_FOR_SALE.translate(commonPlayer));
		}
		
		meta.lore(lore);
		
		item.setItemMeta(meta);
		
		itemStructureMap.put(item, itemBuilder);
	}
	
	@Override
	public void updateTitle()
	{
	
	}
	
	@Override
	public boolean hasItemBuilder(ItemStack item)
	{
		return getItemBuilder(item) != null;
	}
	
	@Override
	public ItemBuilder getItemBuilder(ItemStack item)
	{
		return itemStructureMap.get(item);
	}
}
