package com.eul4.common.interceptor;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.eul4.common.Common;
import com.eul4.common.constant.CommonNamespacedKey;
import com.eul4.common.util.ContainerUtil;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class HideEntityFlagInterceptor extends PacketAdapter
{
	private final Common plugin;
	
	public HideEntityFlagInterceptor(Common plugin)
	{
		super(plugin, PacketType.Play.Server.SPAWN_ENTITY);
		
		this.plugin = plugin;
	}
	
	@Override
	public void onPacketSending(PacketEvent event)
	{
		final UUID entityUniqueId = event.getPacket().getUUIDs().readSafely(0);
		Entity entity = plugin.getEntityRegisterListener().getEntityByUuid(entityUniqueId);
		
		if(entity == null)
		{
			return;
		}
		
		if(ContainerUtil.hasFlag(entity.getPersistentDataContainer(), CommonNamespacedKey.HIDE_ENTITY))
		{
			event.setCancelled(true);
		}
	}
}
