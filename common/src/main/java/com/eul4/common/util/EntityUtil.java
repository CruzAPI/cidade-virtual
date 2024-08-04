package com.eul4.common.util;

import com.eul4.common.constant.CommonNamespacedKey;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class EntityUtil
{
	public static void hideNullable(Plugin plugin, Entity entity)
	{
		if(entity != null)
		{
			hide(plugin, entity);
		}
	}
	
	public static void hide(Plugin plugin, Entity entity)
	{
//		PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
//		packet.getModifier().write(0, new IntArrayList(new int[] { entity.getEntityId() }));
		
		ContainerUtil.setFlag(entity.getPersistentDataContainer(), CommonNamespacedKey.HIDE_ENTITY);
		
		for(Player player : entity.getServer().getOnlinePlayers())
		{
			player.hideEntity(plugin, entity);
//			ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
		}
	}
	
	public static void showNullable(Plugin plugin, Entity entity)
	{
		if(entity != null)
		{
			show(plugin, entity);
		}
	}
	
	public static void show(Plugin plugin, Entity entity)
	{
		ContainerUtil.setFlag(entity.getPersistentDataContainer(), CommonNamespacedKey.HIDE_ENTITY, false);
		
		for(Player player : entity.getServer().getOnlinePlayers())
		{
			player.showEntity(plugin, entity);
		}
	}
	
	public static void targetEye(LivingEntity entity, LivingEntity target)
	{
		target(entity, entity.getEyeLocation(), target.getEyeLocation());
	}
	
	public static void target(Entity entity, Location entityLocation, Location targetLocation)
	{
		if(entityLocation.getWorld() != targetLocation.getWorld())
		{
			entity.setRotation(0.0F, 0.0F);
		}
		
		final Vector direction = targetLocation.toVector().subtract(entityLocation.toVector());
		
		final float yaw = (float) Math.toDegrees(Math.atan2(direction.getZ(), direction.getX())) - 90;
		final float pitch = (float) Math.toDegrees(Math.atan2(-direction.getY(), direction.length()));
		
		entity.setRotation(yaw, pitch);
	}
}
