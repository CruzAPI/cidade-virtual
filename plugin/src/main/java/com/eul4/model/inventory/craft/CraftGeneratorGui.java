package com.eul4.model.inventory.craft;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.GeneratorGui;
import com.eul4.model.town.structure.Generator;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public abstract class CraftGeneratorGui extends CraftStructureGui implements GeneratorGui
{
	protected ItemStack collect;
	
	public CraftGeneratorGui(CommonPlayer commonPlayer, Generator structure, Inventory inventory)
	{
		super(commonPlayer, structure, inventory);
		
		ItemMeta meta;
		
		collect = new ItemStack(Material.GOLD_INGOT);
		meta = collect.getItemMeta();
		meta.displayName(PluginMessage.COLLECT_LIKES.translate(commonPlayer.getLocale()));
		collect.setItemMeta(meta);
		
		inventory.setItem(0, info);
		inventory.setItem(1, move);
		inventory.setItem(4, collect);
	}
	
	@Override
	public Generator getStructure()
	{
		return (Generator) structure;
	}
}
