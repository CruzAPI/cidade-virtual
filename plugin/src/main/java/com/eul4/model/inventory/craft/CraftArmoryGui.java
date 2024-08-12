package com.eul4.model.inventory.craft;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.ArmoryGui;
import com.eul4.model.town.structure.Armory;
import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;

@Getter
public class CraftArmoryGui extends CraftStructureGui implements ArmoryGui
{
	public CraftArmoryGui(CommonPlayer commonPlayer, Structure structure)
	{
		this(commonPlayer, (Armory) structure);
	}
	
	public CraftArmoryGui(CommonPlayer commonPlayer, Armory armory)
	{
		super(commonPlayer, armory, commonPlayer.createInventory(InventoryType.HOPPER,
				PluginMessage.STRUCTURE_TITLE,
				PluginMessage.STRUCTURE_ARMORY_NAME,
				armory.getLevel()));
	}
	
	@Override
	public Component getUpdatedTitleComponent()
	{
		return PluginMessage.STRUCTURE_TITLE.translateOne(commonPlayer.getLocale(),
				PluginMessage.STRUCTURE_ARMORY_NAME,
				structure.getLevel());
	}
	
	@Override
	public Armory getArmory()
	{
		return (Armory) structure;
	}
}
