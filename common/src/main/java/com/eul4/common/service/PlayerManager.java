package com.eul4.common.service;

import com.eul4.common.Common;
import com.eul4.common.event.CommonPlayerRegisterEvent;
import com.eul4.common.exception.InvalidCommonPlayerException;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.PlayerType;
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
	
	public CommonPlayer register(Player player, Common plugin, PlayerType playerType)
	{
		if(commonPlayers.containsKey(player.getUniqueId()))
		{
			throw new InvalidCommonPlayerException("Player already registered");
		}
		
		final CommonPlayer newCommonPlayer = playerType.newInstance(player, plugin);
		commonPlayers.put(newCommonPlayer.getUniqueId(), newCommonPlayer);
		plugin.getServer().getPluginManager().callEvent(new CommonPlayerRegisterEvent(null, newCommonPlayer));
		return newCommonPlayer;
	}
	
	public CommonPlayer register(CommonPlayer oldInstance, PlayerType playerType)
	{
		return register(oldInstance.getPlayer(), oldInstance, playerType);
	}
	
	public CommonPlayer register(Player player, CommonPlayer oldInstance)
	{
		return register(player, oldInstance, oldInstance.getPlayerType());
	}
	
	public CommonPlayer register(Player player, CommonPlayer oldInstance, PlayerType playerType)
	{
		oldInstance.invalidate();
		final CommonPlayer newCommonPlayer = playerType.newInstance(player, oldInstance);
		commonPlayers.put(newCommonPlayer.getUniqueId(), newCommonPlayer);
		plugin.getServer().getPluginManager().callEvent(new CommonPlayerRegisterEvent(oldInstance, newCommonPlayer));
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
