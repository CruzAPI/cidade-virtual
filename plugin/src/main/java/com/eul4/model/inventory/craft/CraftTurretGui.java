package com.eul4.model.inventory.craft;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.TurretGui;
import com.eul4.model.town.structure.Turret;
import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;

@Getter
public class CraftTurretGui extends CraftStructureGui implements TurretGui
{
	public CraftTurretGui(CommonPlayer commonPlayer, Structure structure)
	{
		this(commonPlayer, (Turret) structure);
	}
	
	public CraftTurretGui(CommonPlayer commonPlayer, Turret turret)
	{
		super(commonPlayer, turret, commonPlayer.createInventory(InventoryType.HOPPER,
				PluginMessage.STRUCTURE_TITLE,
				PluginMessage.STRUCTURE_TURRET_NAME,
				turret.getLevel()));
	}
	
	@Override
	public Component getUpdatedTitleComponent()
	{
		return PluginMessage.STRUCTURE_TITLE.translate(commonPlayer.getLocale(),
				PluginMessage.STRUCTURE_TURRET_NAME,
				structure.getLevel());
	}
	
	@Override
	public Turret getTurret()
	{
		return (Turret) structure;
	}
}
