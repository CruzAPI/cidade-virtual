package com.eul4.model.inventory.craft;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.CrownDepositGui;
import com.eul4.model.town.structure.CrownDeposit;
import com.eul4.model.town.structure.Structure;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;

import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public class CraftCrownDepositGui extends CraftStructureGui implements CrownDepositGui
{
	public CraftCrownDepositGui(CommonPlayer commonPlayer, Structure structure)
	{
		this(commonPlayer, (CrownDeposit) structure);
	}
	
	public CraftCrownDepositGui(CommonPlayer commonPlayer, CrownDeposit crownDeposit)
	{
		super(commonPlayer, crownDeposit, commonPlayer.createInventory(InventoryType.HOPPER,
				PluginMessage.COMPONENT_STRUCTURE_TITLE,
				Component.empty().color(YELLOW),
				PluginMessage.STRUCTURE_CROWN_DEPOSIT_NAME,
				crownDeposit.getLevel()));
	}
	
	@Override
	public Component getUpdatedTitleComponent()
	{
		CrownDeposit crownDeposit = (CrownDeposit) structure;
		
		return PluginMessage.COMPONENT_STRUCTURE_TITLE.translate(commonPlayer.getLocale(),
				Component.empty().color(YELLOW),
				PluginMessage.STRUCTURE_CROWN_DEPOSIT_NAME,
				crownDeposit.getLevel());
	}
}
