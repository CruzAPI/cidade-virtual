package com.eul4.model.inventory.craft;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.structure.DislikeDeposit;
import com.eul4.model.town.structure.Generator;
import com.eul4.model.town.structure.Structure;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;

import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class CraftDislikeDepositGui extends CraftStructureGui
{
	public CraftDislikeDepositGui(CommonPlayer commonPlayer, Structure structure)
	{
		this(commonPlayer, (DislikeDeposit) structure);
	}
	
	public CraftDislikeDepositGui(CommonPlayer commonPlayer, DislikeDeposit dislikeDeposit)
	{
		super(commonPlayer, dislikeDeposit, commonPlayer.createInventory(InventoryType.HOPPER,
				PluginMessage.COMPONENT_STRUCTURE_TITLE,
				Component.empty().color(RED),
				PluginMessage.STRUCTURE_DISLIKE_DEPOSIT_NAME,
				dislikeDeposit.getLevel()));
	}
	
	@Override
	public Component getUpdatedTitleComponent()
	{
		Generator generator = (Generator) structure;
		
		return PluginMessage.COMPONENT_STRUCTURE_TITLE.translateOne(commonPlayer.getLocale(),
				Component.empty().color(RED),
				PluginMessage.STRUCTURE_DISLIKE_DEPOSIT_NAME,
				generator.getLevel());
	}
}
