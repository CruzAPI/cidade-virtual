package com.eul4.listener.world;

import com.eul4.Main;
import com.eul4.type.PluginWorldType;
import com.eul4.world.VanillaLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

@RequiredArgsConstructor
public class VanillaLevelListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(PlayerPortalEvent event)
	{
		Location from = event.getFrom();
		Location to = event.getTo();
		
		boolean fromNether = from.getWorld().getEnvironment() == World.Environment.NETHER;
		
		World worldFrom = from.getWorld();
		
		if(!(plugin.getWorldManager().get(worldFrom) instanceof VanillaLevel vanillaLevel))
		{
			return;
		}
		
		PluginWorldType related = vanillaLevel.getRelated(to.getWorld().getEnvironment());
		
		if(related == null)
		{
			return;
		}
		
		World toWorld = related.getWorld();
		
		to.setWorld(toWorld);
		
		if(fromNether)
		{
			to.setX(from.getX() * 8.0D);
			to.setZ(from.getZ() * 8.0D);
		}
		else
		{
			to.setX(from.getX() / 8.0D);
			to.setZ(from.getZ() / 8.0D);
		}
		
		event.setTo(truncate(to));
	}
	
	public static Location truncate(Location loc)
	{
		World world = loc.getWorld();
		WorldBorder border = world.getWorldBorder();
		
		if(border.isInside(loc))
		{
			return loc;
		}
		
		Location center = border.getCenter();
		double borderSize = border.getSize() / 2.0D;
		
		double minX = center.getX() - borderSize;
		double maxX = center.getX() + borderSize;
		double minZ = center.getZ() - borderSize;
		double maxZ = center.getZ() + borderSize;
		
		double locX = loc.getX();
		double locZ = loc.getZ();
		
		if(locX < minX) locX = minX;
		if(locX > maxX) locX = maxX;
		if(locZ < minZ) locZ = minZ;
		if(locZ > maxZ) locZ = maxZ;
		
		loc.setX(locX);
		loc.setZ(locZ);
		
		loc.setY(Math.max(world.getMinHeight(), Math.min(loc.getY(), world.getMaxHeight())));
		
		return loc;
	}
}
