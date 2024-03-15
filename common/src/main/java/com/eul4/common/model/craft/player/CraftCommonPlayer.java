package com.eul4.common.model.craft.player;

import com.eul4.common.Common;
import com.eul4.common.i18n.Message;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.CommonPlayerType;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.UUID;

@Getter
public class CraftCommonPlayer implements CommonPlayer
{
	protected final Player player;
	protected final Common plugin;
	
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
	public void reset()
	{
	
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
