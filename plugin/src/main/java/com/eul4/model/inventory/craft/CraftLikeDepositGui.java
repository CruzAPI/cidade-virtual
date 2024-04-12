package com.eul4.model.inventory.craft;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.structure.Generator;
import com.eul4.model.town.structure.LikeDeposit;
import com.eul4.model.town.structure.Structure;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;

import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class CraftLikeDepositGui extends CraftStructureGui
{
	public CraftLikeDepositGui(CommonPlayer commonPlayer, Structure structure)
	{
		this(commonPlayer, (LikeDeposit) structure);
	}
	
	public CraftLikeDepositGui(CommonPlayer commonPlayer, LikeDeposit likeDeposit)
	{
		super(commonPlayer, likeDeposit, commonPlayer.createInventory(InventoryType.HOPPER,
				PluginMessage.COMPONENT_STRUCTURE_TITLE,
				Component.empty().color(GREEN),
				PluginMessage.STRUCTURE_LIKE_DEPOSIT_NAME,
				likeDeposit.getLevel()));
	}
	
	@Override
	public Component getUpdatedTitleComponent()
	{
		Generator generator = (Generator) structure;
		
		return PluginMessage.COMPONENT_STRUCTURE_TITLE.translate(commonPlayer.getLocale(),
				Component.empty().color(GREEN),
				PluginMessage.STRUCTURE_LIKE_DEPOSIT_NAME,
				generator.getLevel());
	}
}
