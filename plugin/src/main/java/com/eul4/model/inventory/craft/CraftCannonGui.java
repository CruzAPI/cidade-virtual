package com.eul4.model.inventory.craft;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.CannonGui;
import com.eul4.model.inventory.CannonGui;
import com.eul4.model.town.structure.Cannon;
import com.eul4.model.town.structure.Cannon;
import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class CraftCannonGui extends CraftStructureGui implements CannonGui
{
	public CraftCannonGui(CommonPlayer commonPlayer, Structure structure)
	{
		this(commonPlayer, (Cannon) structure);
	}
	
	public CraftCannonGui(CommonPlayer commonPlayer, Cannon cannon)
	{
		super(commonPlayer, cannon, commonPlayer.createInventory(InventoryType.HOPPER,
				PluginMessage.STRUCTURE_TITLE,
				PluginMessage.STRUCTURE_CANNON_NAME,
				cannon.getLevel()));
	}
	
	@Override
	public Component getUpdatedTitleComponent()
	{
		return PluginMessage.STRUCTURE_TITLE.translate(commonPlayer.getLocale(),
				PluginMessage.STRUCTURE_CANNON_NAME,
				structure.getLevel());
	}
	
	@Override
	public Cannon getCannon()
	{
		return (Cannon) structure;
	}
}
