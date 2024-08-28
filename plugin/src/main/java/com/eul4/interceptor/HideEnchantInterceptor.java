package com.eul4.interceptor;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.eul4.common.Common;

import java.util.List;

public class HideEnchantInterceptor extends PacketAdapter
{
	private final Common plugin;
	
	public HideEnchantInterceptor(Common plugin)
	{
		super(plugin, PacketType.Play.Server.WINDOW_DATA);
		
		this.plugin = plugin;
	}
	
	@Override
	public void onPacketSending(PacketEvent event)
	{
		PacketContainer packet = event.getPacket();
		List<Integer> data = packet.getIntegers().getValues();
		
		if(data.get(1) == 4 || data.get(1) == 5 || data.get(1) == 6)
		{
			event.setCancelled(true);
		}
	}
}
