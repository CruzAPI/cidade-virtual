package com.eul4.model.inventory.craft;

import com.eul4.Main;
import com.eul4.Price;
import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.Currency;
import com.eul4.enums.ItemBuilder;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.StructureShopGui;
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
	
	public CraftStructureShopGui(CommonPlayer commonPlayer)
	{
		super(commonPlayer, commonPlayer.createInventory(9 * 6, PluginMessage.STRUCTURE_SHOP_TITLE));
		
		likeGenerator = new ItemStack(Material.EMERALD);
		setDisplayNameAndPriceInLore(likeGenerator, ItemBuilder.LIKE_GENERATOR);
		
		dislikeGenerator = new ItemStack(Material.REDSTONE);
		setDisplayNameAndPriceInLore(dislikeGenerator, ItemBuilder.DISLIKE_GENERATOR);
		
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
			List<Component> components = new ArrayList<>();
			
			if(price.getLikes() > 0)
			{
				components.add(DECORATED_VALUE_CURRENCY.translate(commonPlayer,
						empty().color(GREEN),
						price.getLikes(),
						Currency.LIKE));
			}
			
			if(price.getDislikes() > 0)
			{
				components.add(DECORATED_VALUE_CURRENCY.translate(commonPlayer,
						empty().color(RED),
						price.getDislikes(),
						Currency.DISLIKE));
			}
			
			meta.lore(components);
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
