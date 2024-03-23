package com.eul4.model.inventory;

import com.eul4.common.model.inventory.Gui;
import com.eul4.model.town.structure.Structure;

public interface StructureGui extends Gui
{
	Structure getStructure();
}
