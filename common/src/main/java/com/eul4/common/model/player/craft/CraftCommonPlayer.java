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
import com.eul4.common.model.player.ScoreboardPlayer;
import com.eul4.common.world.CommonWorld;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public abstract class CraftCommonPlayer implements CommonPlayer
{
	private static final long VERSION = 0L;
	
	protected final Player player;
	protected final Common plugin;
	
	private CommonPlayer oldInstance;
	
	private final Locale locale = new Locale("pt", "BR");
	private Gui gui;
	protected CommonPlayerData commonPlayerData;
	
	private boolean valid = true;
	
	public CraftCommonPlayer(Player player, Common plugin)
	{
		this.player = player;
		this.plugin = plugin;
		
		this.oldInstance = null;
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
		player.resetTitle();
		resetScoreboard();
	}
	
	@Override
	public final void resetScoreboard()
	{
		if(this instanceof ScoreboardPlayer scoreboardPlayer && commonPlayerData.isScoreboardEnabled())
		{
			player.setScoreboard(scoreboardPlayer.getScoreboard().getBukkitScoreboard());
		}
		else
		{
			player.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
		}
	}
	
	@Override
	public void resetPlayerData()
	{
		player.clearActivePotionEffects();
		player.setArrowsInBody(0);
		player.getInventory().clear();
		player.setExhaustion(0.0F);
		player.setExp(0.0F);
		player.setFallDistance(0.0F);
		player.setFireTicks(0);
		player.setFlying(false);
		player.setFlySpeed(0.1F);
		player.setFoodLevel(20);
		player.setFreezeTicks(0);
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH)
				.setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
		player.setHealth(player.getHealthScale());
		player.setItemOnCursor(null);
		player.setLevel(0);
		player.setMaximumAir(0);
		player.setMaximumNoDamageTicks(20);
		player.setNoDamageTicks(0);
		player.setRemainingAir(0);
		player.setSaturation(0.0F);
		player.setUnsaturatedRegenRate(0);
		player.setWalkSpeed(0.2F);
	}
	
	@Override
	public void sendMessage(Message message, Object... args)
	{
		try
		{
			message.translateLore(locale, args).forEach(player::sendMessage);
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
		this.gui = null;
	}
	
	@Override
	public Inventory createInventory(InventoryType inventoryType, Message message, Object... args)
	{
		return createInventory(inventoryType, message.translateOne(locale, args));
	}
	
	@Override
	public Inventory createInventory(InventoryType inventoryType)
	{
		return plugin.getServer().createInventory(player, inventoryType);
	}
	
	@Override
	public Inventory createInventory(int size, Message message, Object... args)
	{
		return plugin.getServer().createInventory(player, size, message.translateOne(locale, args));
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
			var item = Optional.ofNullable(commonPlayerData.getPlayerData().getContents()[0]).orElse(ItemStack.empty());
			plugin.getLogger().info("save=" + getPlayerType() + " item=" + item.getType());
		}
	}
	
	@Override
	public boolean isRegistered()
	{
		return plugin.getPlayerManager().isRegistered(this);
	}
	
	@Override
	public boolean isValid()
	{
		return valid;
	}
	
	@Override
	public void invalidate()
	{
		this.valid = false;
		this.oldInstance = null;
		//TODO: ??
	}
	
	@Override
	public final CommonWorld getCommonWorld()
	{
		return plugin.getWorldManager().get(player.getWorld());
	}
	
	@Override
	public Optional<CommonPlayer> findOldInstance()
	{
		return Optional.ofNullable(oldInstance);
	}
	
	@Override
	public void clearChat()
	{
		for(int i = 0; i < 200; i++)
		{
			player.sendMessage(Component.empty());
		}
	}
	
	@Override
	public void clearChat(int emptyLines)
	{
		Preconditions.checkArgument(emptyLines > 0);
		
		for(int i = 0; i < emptyLines; i++)
		{
			player.sendMessage(Component.empty());
		}
	}
	
	@Override
	public boolean hasPermission(String perm)
	{
		return plugin.getPermissionService().hasPermission(this, perm);
	}
}
