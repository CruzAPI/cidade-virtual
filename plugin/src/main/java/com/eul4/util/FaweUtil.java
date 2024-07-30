package com.eul4.util;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.Region;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

@UtilityClass
public final class FaweUtil
{
	public static World toBukkitWorld(com.sk89q.worldedit.world.World weWorld)
	{
		return Bukkit.getServer().getWorld(weWorld.getName());
	}
	
	public static Block toBukkitBlock(BlockVector3 blockVector3, com.sk89q.worldedit.world.World weWorld)
	{
		return toBukkitBlock(blockVector3, toBukkitWorld(weWorld));
	}
	
	public static Block toBukkitBlock(BlockVector3 blockVector3, World world)
	{
		return world.getBlockAt(blockVector3.x(), blockVector3.y(), blockVector3.z());
	}
	
	public static BoundingBox boundingBox(BlockVector3 min, BlockVector3 max, com.sk89q.worldedit.world.World weWorld)
	{
		Block minBlock = toBukkitBlock(min, weWorld);
		Block maxBlock = toBukkitBlock(max, weWorld);
		
		return BoundingBox.of(minBlock, maxBlock);
	}
	
	public static Vector3 toFaweVector(Vector vector)
	{
		return Vector3.at(vector.getX(), vector.getY(), vector.getZ());
	}
	
	public static Vector toBukkitVector(Vector3 vector3)
	{
		return new Vector(vector3.x(), vector3.y(), vector3.z());
	}
}
