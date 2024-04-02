package com.eul4.common.model.player;

import com.eul4.common.Common;
import com.eul4.common.factory.GuiEnum;
import com.eul4.common.i18n.Message;
import com.eul4.common.i18n.Messageable;
import com.eul4.common.model.inventory.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Locale;
import java.util.UUID;

public interface CommonPlayer extends Messageable
{
	Player getPlayer();
	Common getPlugin();
	UUID getUniqueId();
	void reset();
	Gui getGui();
	void openGui(GuiEnum guiEnum);
	void openGui(Gui gui);
	void nullifyGui();
	Locale getLocale();
	Inventory createInventory(InventoryType inventoryType, Message message, Object... args);
	Inventory createInventory(InventoryType inventoryType);
	Inventory createInventory(InventoryType inventoryType, Component component);
}
