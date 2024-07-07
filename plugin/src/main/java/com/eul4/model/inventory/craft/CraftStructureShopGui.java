package com.eul4.model.inventory.craft;

import com.eul4.Main;
import com.eul4.Price;
import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.Currency;
import com.eul4.enums.ItemBuilder;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.StructureShopGui;
import com.eul4.util.MessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eul4.i18n.PluginMessage.DECORATED_VALUE_CURRENCY;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class CraftStructureShopGui extends CraftGui implements StructureShopGui
{
	private final Map<ItemStack, ItemBuilder> itemStructureMap = new HashMap<>();
	
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
		
		likeGenerator = new ItemStack(Material.EMERALD);
		setDisplayNameAndPriceInLore(likeGenerator, ItemBuilder.LIKE_GENERATOR);
		
		dislikeGenerator = new ItemStack(Material.REDSTONE);
		setDisplayNameAndPriceInLore(dislikeGenerator, ItemBuilder.DISLIKE_GENERATOR);
		
		likeDeposit = new ItemStack(Material.EMERALD_BLOCK);
		setDisplayNameAndPriceInLore(likeDeposit, ItemBuilder.LIKE_DEPOSIT);
		
		dislikeDeposit = new ItemStack(Material.REDSTONE_BLOCK);
		setDisplayNameAndPriceInLore(dislikeDeposit, ItemBuilder.DISLIKE_DEPOSIT);
		
		armory = new ItemStack(Material.DIAMOND_BLOCK); //TODO: duplicated Material in ItemBuilder?? Probably one for icon and the other for be placed in ground.
		setDisplayNameAndPriceInLore(armory, ItemBuilder.ARMORY);
		
		cannon = new ItemStack(Material.DISPENSER);
		setDisplayNameAndPriceInLore(cannon, ItemBuilder.CANNON);
		
		turret = new ItemStack(Material.SMOOTH_STONE);
		setDisplayNameAndPriceInLore(turret, ItemBuilder.TURRET);
		
		itemStructureMap.keySet().forEach(getInventory()::addItem);
	}
	
	private void setDisplayNameAndPriceInLore(ItemStack item, ItemBuilder itemBuilder)
	{
		ItemMeta meta = item.getItemMeta();
		meta.displayName(itemBuilder.getStructureType().getNameMessage().translate(commonPlayer));
		
		Price price = itemBuilder.getStructureType().getRule((Main) commonPlayer.getPlugin())
				.getAttribute(1).getPrice();
		
		if(price != null)
		{
			meta.lore(MessageUtil.getPriceLore(price, commonPlayer));
		}
		else
		{
			meta.lore(List.of(PluginMessage.STRUCTURE_NOT_FOR_SALE.translate(commonPlayer)));
		}
		
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
