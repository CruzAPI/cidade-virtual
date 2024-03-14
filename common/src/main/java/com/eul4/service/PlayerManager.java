package com.eul4.service;

import com.eul4.Common;
import com.eul4.event.CommonPlayerRegisterEvent;
import com.eul4.event.CommonPlayerReregisterEvent;
import com.eul4.exception.InvalidCommonPlayerException;
import com.eul4.model.player.CommonPlayer;
import com.eul4.type.player.CommonPlayerType;
import com.eul4.type.player.PlayerType;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerManager
{
	private final Common plugin;
	
	private final Map<UUID, CommonPlayer> commonPlayers = new HashMap<>();
	
	public <CP extends CommonPlayer> CP register(Player player, PlayerType<CP> playerType)
	{
		if(commonPlayers.containsKey(player.getUniqueId()))
		{
			throw new InvalidCommonPlayerException("Player already registered");
		}
		
		final CP newCommonPlayer = playerType.getNewInstanceBiFunction().apply(player, plugin);
		commonPlayers.put(newCommonPlayer.getUniqueId(), newCommonPlayer);
		plugin.getServer().getPluginManager().callEvent(new CommonPlayerRegisterEvent(newCommonPlayer));
		return newCommonPlayer;
	}
	
	public <CP extends CommonPlayer> CP reregister(CommonPlayer oldCommonPlayer, CommonPlayerType<CP> commonPlayerType)
	{
		if(!isValid(oldCommonPlayer))
		{
			throw new InvalidCommonPlayerException("This CommonPlayer instance is invalid");
		}
		
		final CP newCommonPlayer = commonPlayerType.getNewInstanceFunction().apply(oldCommonPlayer);
		commonPlayers.put(newCommonPlayer.getUniqueId(), newCommonPlayer);
		plugin.getServer().getPluginManager().callEvent(new CommonPlayerReregisterEvent(oldCommonPlayer, newCommonPlayer));
		return newCommonPlayer;
	}
	
	public CommonPlayer get(Player player)
	{
		return get(player.getUniqueId());
	}
	
	public CommonPlayer get(UUID uuid)
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
	
	public Collection<CommonPlayer> getAll()
	{
		return commonPlayers.values();
	}
}
