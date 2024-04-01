package com.eul4.common.wrapper;

import lombok.EqualsAndHashCode;
import org.bukkit.Server;
import org.bukkit.block.Block;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
public class BlockSerializable implements Serializable
{
	private transient Block block;
	
	private final UUID worldUid;
	private final int x;
	private final int y;
	private final int z;
	
	public BlockSerializable(Block block)
	{
		this.block = block;
		
		worldUid = block.getWorld().getUID();
		
		x = block.getX();
		y = block.getY();
		z = block.getZ();
	}
	
	public Block getBukkitBlock(Server server)
	{
		return block == null ? block = server.getWorld(worldUid).getBlockAt(x, y, z) : block;
	}
}
