package com.eul4.factory;

import com.eul4.common.factory.GuiEnum;
import com.eul4.common.factory.GuiInstantiation;
import com.eul4.model.inventory.craft.CraftStructureGui;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PluginGuiEnum implements GuiEnum
{
//	STRUCTURE(CraftStructureGui::new);
	;
	private final GuiInstantiation instantiation;
}
