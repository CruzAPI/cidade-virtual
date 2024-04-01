package com.eul4.common.interceptor;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.eul4.common.Common;
import org.bukkit.entity.Player;

import java.util.*;

public class SpawnEntityInterceptor extends PacketAdapter
{
	private final Common plugin;
	
	public final Map<Integer, Set<UUID>> entities = new HashMap<>();
	
	public SpawnEntityInterceptor(Common plugin)
	{
		super(plugin, PacketType.Play.Server.SPAWN_ENTITY);
		
		this.plugin = plugin;
	}
	
	@Override
	public void onPacketSending(PacketEvent event)
	{
		final int entityId = event.getPacket().getIntegers().read(0);
		final Player player = event.getPlayer();
		
		if(entities.containsKey(entityId) && !entities.get(entityId).contains(player.getUniqueId()))
		{
			event.setCancelled(true);
		}
	}
	
	public void register(int entityId)
	{
		entities.put(entityId, new HashSet<>());
	}
	
	public void register(int entityId, UUID uuid)
	{
		entities.put(entityId, new HashSet<>(Set.of(uuid)));
	}
	
	public void register(int entityId, Set<UUID> uuids)
	{
		entities.put(entityId, uuids);
	}
	
	public void unregister(int entityId)
	{
		entities.remove(entityId);
	}
}
