package com.eul4.common.model.player.craft;

import com.eul4.common.Common;
import com.eul4.common.event.GuiCloseEvent;
import com.eul4.common.event.GuiOpenEvent;
import com.eul4.common.factory.GuiEnum;
import com.eul4.common.i18n.Message;
import com.eul4.common.model.data.CommonPlayerData;
import com.eul4.common.model.data.PlayerData;
import com.eul4.common.model.inventory.Gui;
import com.eul4.common.model.player.CommonPlayer;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.UUID;

@Getter
@Setter
public abstract class CraftCommonPlayer implements CommonPlayer
{
	private static final long VERSION = 0L;
	
	protected final Player player;
	protected final Common plugin;
	
	private final Locale locale = new Locale("pt", "BR");
	
	private Gui gui;
	protected CommonPlayer oldInstance;
	protected CommonPlayerData commonPlayerData;
	
	public CraftCommonPlayer(Player player, Common plugin)
	{
		this.player = player;
		this.plugin = plugin;
		
		this.commonPlayerData = new CommonPlayerData();
	}
	
	public CraftCommonPlayer(Player player, CommonPlayer oldCommonPlayer)
	{
		this.player = player;
		this.plugin = oldCommonPlayer.getPlugin();
		
		this.oldInstance = oldCommonPlayer;
		this.commonPlayerData = oldCommonPlayer.getCommonPlayerData();
	}
	
	@Override
	public void reset()
	{
		player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
	}
	
	@Override
	public void sendMessage(Message message, Object... args)
	{
		try
		{
			player.sendMessage(message.translate(locale, args));
		}
		catch(MissingResourceException e)
		{
			player.sendMessage(Component.text("Message not found: " + e.getKey()).color(NamedTextColor.RED));
		}
		catch(Exception e)
		{
			player.sendMessage(Component.text("Error while sending message: " + message.getKey()).color(NamedTextColor.RED));
			e.printStackTrace();
		}
	}
	
	@Override
	public UUID getUniqueId()
	{
		return player.getUniqueId();
	}
	
	@Override
	public void openGui(GuiEnum guiEnum)
	{
		gui = guiEnum.getInstantiation().newInstance(this);
		player.openInventory(gui.getInventory());
	}
	
	@Override
	public void openGui(Gui gui)
	{
		player.openInventory(gui.getInventory());
		gui.updateTitle();
		this.gui = gui;
		plugin.getServer().getPluginManager().callEvent(new GuiOpenEvent(gui));
	}
	
	@Override
	public void nullifyGui()
	{
		plugin.getServer().getPluginManager().callEvent(new GuiCloseEvent(gui));
		this.gui = null;
	}
	
	@Override
	public Inventory createInventory(InventoryType inventoryType, Message message, Object... args)
	{
		return createInventory(inventoryType, message.translate(locale, args));
	}
	
	@Override
	public Inventory createInventory(InventoryType inventoryType)
	{
		return plugin.getServer().createInventory(player, inventoryType);
	}
	
	@Override
	public Inventory createInventory(int size, Message message, Object... args)
	{
		return plugin.getServer().createInventory(player, size, message.translate(locale, args));
	}
	
	@Override
	public Inventory createInventory(InventoryType inventoryType, Component component)
	{
		return plugin.getServer().createInventory(player, inventoryType, component);
	}
	
	@Override
	public final void savePlayerData()
	{
		if(mustSavePlayerData())
		{
			commonPlayerData.setPlayerData(new PlayerData(player));
		}
	}
	
	@Override
	public void invalidate()
	{
		oldInstance = null;
	}
}
