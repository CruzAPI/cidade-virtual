package com.eul4.common.model.player.craft;

import com.eul4.common.Common;
import com.eul4.common.factory.GuiEnum;
import com.eul4.common.i18n.Message;
import com.eul4.common.model.inventory.Gui;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.CommonPlayerType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.UUID;

@Getter
@Setter
public class CraftCommonPlayer implements CommonPlayer
{
	protected final Player player;
	protected final Common plugin;
	
	private final Locale locale = new Locale("pt", "BR");
	
	private Gui gui;
	
	public CraftCommonPlayer(Player player, Common plugin)
	{
		this.player = player;
		this.plugin = plugin;
	}
	
	public CraftCommonPlayer(CommonPlayer oldCommonPlayer)
	{
		this(oldCommonPlayer.getPlayer(), oldCommonPlayer.getPlugin());
	}
	
	@Override
	public void reset()
	{
	
	}
	
	@Override
	public void sendMessage(Message message, Object... args)
	{
		player.spigot().sendMessage(message.translate(locale, args));
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
		this.gui = gui;
	}
	
	@Override
	public void nullifyGui()
	{
		this.gui = null;
	}
}
