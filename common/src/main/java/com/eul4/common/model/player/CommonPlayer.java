package com.eul4.common.model.player;

import com.eul4.common.Common;
import com.eul4.common.factory.GuiEnum;
import com.eul4.common.i18n.Message;
import com.eul4.common.i18n.Messageable;
import com.eul4.common.model.data.CommonPlayerData;
import com.eul4.common.model.inventory.Gui;
import com.eul4.common.type.player.PlayerType;
import com.eul4.common.world.CommonWorld;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public interface CommonPlayer extends Messageable
{
	CommonPlayerData getCommonPlayerData();
	void setCommonPlayerData(CommonPlayerData commonPlayerData);
	void savePlayerData();
	Player getPlayer();
	Common getPlugin();
	
	void resetScoreboard();
	void resetPlayerData();
	
	UUID getUniqueId();
	void reset();
	Gui getGui();
	void openGui(GuiEnum guiEnum);
	void openGui(Gui gui);
	void nullifyGui();
	Locale getLocale();
	Inventory createInventory(InventoryType inventoryType, Message message, Object... args);
	Inventory createInventory(InventoryType inventoryType);
	Inventory createInventory(int size, Message message, Object... args);
	Inventory createInventory(InventoryType inventoryType, Component component);
	PlayerType getPlayerType();
	boolean mustSavePlayerData();
	
	void invalidate();
	CommonPlayer load();
	CommonPlayer reload();
	CommonWorld getCommonWorld();
	boolean isValid();
	boolean isRegistered();
	Optional<CommonPlayer> findOldInstance();
	CommonPlayer getOldInstance();
	
	void clearChat();
	void clearChat(int emptyLines);
}
