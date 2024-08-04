package com.eul4.common.util;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.entity.CraftDisplay;

public class PacketUtil
{
	public static Packet<?> createAddEntityPacket(CraftDisplay display)
	{
		return new ClientboundAddEntityPacket(display.getEntityId(),
				display.getUniqueId(),
				display.getX(),
				display.getY(),
				display.getZ(),
				display.getPitch(),
				display.getYaw(),
				display.getHandle().getType(),
				0,
				new Vec3(0, 0, 0),
				display.getYaw());
	}
}
