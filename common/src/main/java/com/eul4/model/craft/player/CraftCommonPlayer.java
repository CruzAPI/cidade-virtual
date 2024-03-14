package com.eul4.model.craft.player;

import com.eul4.Common;
import com.eul4.i18n.Message;
import com.eul4.model.player.CommonPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.UUID;

@Getter
public class CraftCommonPlayer implements CommonPlayer
{
	private final Player player;
	private final Common plugin;
	
	private final Locale locale = new Locale("pt", "BR");
	
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
	public void sendMessage(Message message, Object... args)
	{
		player.sendMessage(message.translate(locale, args));
	}
	
	@Override
	public UUID getUniqueId()
	{
		return player.getUniqueId();
	}
}
