package com.eul4.town.intercepter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.eul4.town.Main;
import com.eul4.town.model.craft.town.CraftTown;
import com.eul4.town.model.player.TownPlayer;
import com.eul4.town.model.town.Town;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SpawnEntityIntercepter extends PacketAdapter
{
	private final Main plugin;
	
	public SpawnEntityIntercepter(Main plugin)
	{
		super(plugin, PacketType.Play.Server.SPAWN_ENTITY);
		
		this.plugin = plugin;
	}
	
	@Override
	public void onPacketSending(PacketEvent event)
	{
		final int entityId = event.getPacket().getIntegers().read(0);
		final Player player = event.getPlayer();
		
		if(CraftTown.HOLOGRAMS_ID.contains(entityId)
				&& !isPlayerOwnerOfHologram(player, entityId))
		{
			event.setCancelled(true);
		}
		
		Bukkit.broadcastMessage(String.format("interceptor: %d %s %s:%s cancel:%s",
				entityId,
				CraftTown.HOLOGRAMS_ID.contains(entityId),
				player.getName(),
				isPlayerOwnerOfHologram(player, entityId),
				event.isCancelled()));
	}
	
	private boolean isPlayerOwnerOfHologram(Player player, int entityId)
	{
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
				|| !townPlayer.hasTown())
		{
			if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer))
			{
				Bukkit.broadcastMessage(player.getName() + " is not instanceof TownPlayer");
			}
			else
			{
				Bukkit.broadcastMessage(player.getName() + " has town? " + townPlayer.hasTown());
			}
			return false;
		}
		
		Town town = townPlayer.getTown();
		
		return town.getHologramsId().contains(entityId);
	}
}
