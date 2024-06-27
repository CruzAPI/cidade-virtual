package com.eul4.common.service;

import com.eul4.common.Common;
import com.eul4.common.event.CommonPlayerRegisterEvent;
import com.eul4.common.exception.InvalidCommonPlayerException;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.PlayerType;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.*;

@RequiredArgsConstructor
public class PlayerManager
{
	private final Common plugin;
	
	private final Map<UUID, CommonPlayer> commonPlayers = new HashMap<>();
	
//	TODO: rethink...
//	public CommonPlayer tryRegisterInvalidInstance(Player player, CommonPlayer oldInstance, PlayerType playerType)
//	{
//		oldInstance.invalidate();
//
//		boolean exists = commonPlayers.containsKey(oldInstance.getUniqueId());
//
//		CommonPlayer newCommonPlayer = playerType.newInstance(player, oldInstance);
//		commonPlayers.put(newCommonPlayer.getUniqueId(), newCommonPlayer);
//		plugin.getServer().getPluginManager().callEvent(new CommonPlayerRegisterEvent(oldInstance, newCommonPlayer));
//
//		try
//		{
//			newCommonPlayer = exists ? newCommonPlayer.reload() : newCommonPlayer.load();
//			return newCommonPlayer;
//		}
//		catch(Exception e)
//		{
//			newCommonPlayer.invalidate();
//			commonPlayers.put(newCommonPlayer.getUniqueId(), oldInstance);
//			plugin.getServer().getPluginManager().callEvent(new CommonPlayerRegisterEvent(newCommonPlayer, oldInstance));
//			return oldInstance;
//		}
//	}
//
//	public CommonPlayer tryRegister(Player player, CommonPlayer oldInstance, PlayerType playerType)
//	{
//		if(!oldInstance.isValid())
//		{
//			throw new InvalidCommonPlayerException();
//		}
//
//		boolean exists = commonPlayers.containsKey(oldInstance.getUniqueId());
//
//		CommonPlayer newCommonPlayer = playerType.newInstance(player, oldInstance);
//		commonPlayers.put(newCommonPlayer.getUniqueId(), newCommonPlayer);
//		plugin.getServer().getPluginManager().callEvent(new CommonPlayerRegisterEvent(oldInstance, newCommonPlayer));
//
//		try
//		{
//			newCommonPlayer = exists ? newCommonPlayer.reload() : newCommonPlayer.load();
//			oldInstance.invalidate();
//			return newCommonPlayer;
//		}
//		catch(Exception e)
//		{
//			newCommonPlayer.invalidate();
//			commonPlayers.put(newCommonPlayer.getUniqueId(), oldInstance);
//			plugin.getServer().getPluginManager().callEvent(new CommonPlayerRegisterEvent(newCommonPlayer, oldInstance));
//			return oldInstance;
//		}
//	}
	
	public CommonPlayer register(CommonPlayer newCommonPlayer)
	{
		boolean isJoining = !commonPlayers.containsKey(newCommonPlayer.getUniqueId());
		CommonPlayer oldInstance = newCommonPlayer.getOldInstance();
		
		if(oldInstance == null && commonPlayers.containsKey(newCommonPlayer.getUniqueId())
				|| newCommonPlayer.isRegistered())
		{
			throw new InvalidCommonPlayerException("Player already registered!");
		}
		
		if(!newCommonPlayer.isValid())
		{
			throw new InvalidCommonPlayerException("This CommonPlayer instance is invalid!");
		}
		
		newCommonPlayer.findOldInstance().ifPresent(CommonPlayer::invalidate);
		commonPlayers.put(newCommonPlayer.getUniqueId(), newCommonPlayer);
		plugin.getServer().getPluginManager().callEvent(new CommonPlayerRegisterEvent(newCommonPlayer));
		
		return isJoining ? newCommonPlayer.load() : newCommonPlayer.reload();
	}
	
	public CommonPlayer register(Player player, Common plugin, PlayerType playerType)
	{
		return register(playerType.newInstance(player, plugin));
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
		return register(playerType.newInstance(player, oldInstance));
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
	
	public boolean isRegistered(CommonPlayer commonPlayer)
	{
		return commonPlayers.get(commonPlayer.getUniqueId()) == commonPlayer;
	}
	
	public Collection<CommonPlayer> getAll()
	{
		return commonPlayers.values();
	}
}
