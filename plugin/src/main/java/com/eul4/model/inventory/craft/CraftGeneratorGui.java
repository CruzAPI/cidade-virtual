package com.eul4.model.inventory.craft;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.inventory.GeneratorGui;
import com.eul4.model.town.structure.Generator;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.eul4.i18n.PluginMessage.*;

@Getter
public abstract class CraftGeneratorGui extends CraftStructureGui implements GeneratorGui
{
	private final Generator generator;
	
	protected ItemStack collect;
	
	public CraftGeneratorGui(CommonPlayer commonPlayer, Generator generator, Inventory inventory)
	{
		super(commonPlayer, generator, inventory);
		
		this.generator = generator;
		
		collect = ItemStack.of(getCollectType());
	}
	
	protected abstract Material getCollectType();
	
	@Override
	public void updateInventory()
	{
		ItemMeta meta;
		
		meta = collect.getItemMeta();
		meta.displayName(INVENTORY_GENERATOR_COLLECT.translateOne(commonPlayer.getLocale(), generator));
		
		if(generator.hasReachedMaxTownBalanceCapacity())
		{
			meta.lore(INVENTORY_GENERATOR_COLLECT_LORE_DEPOSIT_FULL.translateLore(commonPlayer.getLocale(), generator));
		}
		else
		{
			meta.lore(INVENTORY_GENERATOR_COLLECT_LORE.translateLore(commonPlayer.getLocale(), generator));
		}
		
		collect.setItemMeta(meta);
		
		inventory.setItem(4, collect);
		super.updateInventory();
	}
	
	@Override
	public Generator getStructure()
	{
		return (Generator) structure;
	}
	
	@Override
	public ItemStack getCollectIcon()
	{
		return collect.clone();
	}
}
