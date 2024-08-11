package com.eul4.model.inventory.craft;

import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.AssistantGui;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.eul4.i18n.PluginMessage.INVENTORY_ASSISTANT_TITLE;
import static org.bukkit.event.inventory.InventoryType.HOPPER;

@Getter
public class CraftAssistantGui extends CraftGui implements AssistantGui
{
	private static final ItemStack GLASS;
	
	static
	{
		ItemMeta meta;
		
		GLASS = ItemStack.of(Material.BLACK_STAINED_GLASS_PANE);
		meta = GLASS.getItemMeta();
		meta.displayName(Component.empty());
		GLASS.setItemMeta(meta);
	}
	
	private final ItemStack moveAssistant;
	private final ItemStack structureShop;
	private final ItemStack backToSpawn;
	
	public CraftAssistantGui(CommonPlayer commonPlayer)
	{
		super(commonPlayer, commonPlayer.createInventory(HOPPER, INVENTORY_ASSISTANT_TITLE));
		
		ItemMeta meta;
		
		moveAssistant = ItemStack.of(Material.VILLAGER_SPAWN_EGG);
		meta = moveAssistant.getItemMeta();
		meta.displayName(PluginMessage.INVENTORY_ASSISTANT_MOVE_ASSISTANT.translateOne(commonPlayer));
		moveAssistant.setItemMeta(meta);
		
		structureShop = ItemStack.of(Material.BLACK_CONCRETE);
		meta = structureShop.getItemMeta();
		meta.displayName(PluginMessage.INVENTORY_ASSISTANT_STRUCTURE_SHOP.translateOne(commonPlayer));
		structureShop.setItemMeta(meta);
		
		backToSpawn = ItemStack.of(Material.GRASS_BLOCK);
		meta = backToSpawn.getItemMeta();
		meta.displayName(PluginMessage.INVENTORY_ASSISTANT_BACK_TO_SPAWN.translateOne(commonPlayer));
		backToSpawn.setItemMeta(meta);
		
		inventory.setItem(0, moveAssistant);
		inventory.setItem(1, GLASS);
		inventory.setItem(2, structureShop);
		inventory.setItem(3, GLASS);
		inventory.setItem(4, backToSpawn);
	}
	
	@Override
	public void updateTitle()
	{
	
	}
	
	@Override
	public ItemStack getMoveAssistantIcon()
	{
		return moveAssistant.clone();
	}
	
	@Override
	public ItemStack getStructureShopIcon()
	{
		return structureShop.clone();
	}
	
	@Override
	public ItemStack getBackToSpawnIcon()
	{
		return backToSpawn.clone();
	}
}
