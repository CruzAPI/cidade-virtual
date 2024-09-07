package com.eul4.interceptor;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedParticle;
import com.eul4.Main;
import org.bukkit.Particle;

public class HeartParticleInterceptor extends PacketAdapter
{
	private final Main plugin;
	
	public HeartParticleInterceptor(Main plugin)
	{
		super(plugin, PacketType.Play.Server.WORLD_PARTICLES);
		
		this.plugin = plugin;
	}
	
	@Override
	public void onPacketSending(PacketEvent event)
	{
		PacketContainer packet = event.getPacket();
		
		WrappedParticle<?> wrappedParticle = packet.getNewParticles().readSafely(0);
		
		if(wrappedParticle == null)
		{
			return;
		}
		
		Particle particle = wrappedParticle.getParticle();
		
		if(particle == Particle.DAMAGE_INDICATOR)
		{
			event.setCancelled(true);
		}
	}
}
