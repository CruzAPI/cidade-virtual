package com.eul4.common.service;

import com.eul4.common.Common;
import com.eul4.common.event.CommonPlayerRegisterEvent;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.exception.InvalidCommonPlayerException;
import com.eul4.common.type.player.CommonPlayerType;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerManager<PP extends CommonPlayer>
{
	private final Common plugin;
	
	private final Map<UUID, PP> commonPlayers = new HashMap<>();
	
	public <PL extends Common, P extends PP> P register(Player player, PL plugin, CommonPlayerType<PP, PL, P> playerType)
	{
		if(commonPlayers.containsKey(player.getUniqueId()))
		{
			throw new InvalidCommonPlayerException("Player already registered");
		}
		
		final P newCommonPlayer = playerType.getPluginConstructor().apply(player, plugin);
		commonPlayers.put(newCommonPlayer.getUniqueId(), newCommonPlayer);
		plugin.getServer().getPluginManager().callEvent(new CommonPlayerRegisterEvent(null, newCommonPlayer));
		return newCommonPlayer;
	}
	
	public <P extends PP> P register(PP oldPluginPlayer, CommonPlayerType<PP, ?, P> commonPlayerType)
	{
		return register(oldPluginPlayer.getPlayer(), oldPluginPlayer, commonPlayerType);
	}
	
	public <P extends PP> P register(Player player, PP oldPluginPlayer, CommonPlayerType<PP, ?, P> commonPlayerType)
	{
		final P newCommonPlayer = commonPlayerType.getCommonPlayerConstructor().apply(player, oldPluginPlayer);
		commonPlayers.put(newCommonPlayer.getUniqueId(), newCommonPlayer);
		plugin.getServer().getPluginManager().callEvent(new CommonPlayerRegisterEvent(oldPluginPlayer, newCommonPlayer));
		return newCommonPlayer;
	}
	
	public PP get(Player player)
	{
		return get(player.getUniqueId());
	}
	
	public PP get(UUID uuid)
	{
		return commonPlayers.get(uuid);
	}
	
	public void unregister(CommonPlayer commonPlayer)
	{
		unregister(commonPlayer.getPlayer());
	}
	
	public void unregister(Player player)
	{
		unregister(player.getUniqueId());
	}
	
	public void unregister(UUID uuid)
	{
		commonPlayers.remove(uuid);
	}
	
	public boolean isValid(CommonPlayer commonPlayer)
	{
		return commonPlayers.get(commonPlayer.getUniqueId()) == commonPlayer;
	}
	
	public Collection<PP> getAll()
	{
		return commonPlayers.values();
	}
}
