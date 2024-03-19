package com.eul4.intercepter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.eul4.Main;
import com.eul4.model.craft.town.CraftTown;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
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
		Bukkit.broadcastMessage("onPacketSending primaryThread? " + plugin.getServer().isPrimaryThread());
		final int entityId = event.getPacket().getIntegers().read(0);
		final Entity entity = plugin.getEntityRegisterListener().getEntityById(entityId);
		final Player player = event.getPlayer();
		
		if(CraftTown.HOLOGRAMS.contains(entity)
				&& entity instanceof ArmorStand hologram
				&& !isPlayerOwnerOfHologram(player, hologram))
		{
			event.setCancelled(true);
		}
		Bukkit.broadcastMessage("interceptor " + entityId + " " + event.isCancelled());
	}
	
	private boolean isPlayerOwnerOfHologram(Player player, ArmorStand hologram)
	{
		if(!(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer)
				|| !townPlayer.hasTown())
		{
			return false;
		}
		
		Town town = townPlayer.getTown();
		
		return town.getTileHolograms().contains(hologram);
	}
}
