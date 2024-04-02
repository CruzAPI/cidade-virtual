package com.eul4.model.inventory;

import com.eul4.model.town.structure.Generator;
import org.bukkit.inventory.ItemStack;

public interface GeneratorGui extends StructureGui
{
	@Override
	Generator getStructure();
	
	ItemStack getCollect();
}
