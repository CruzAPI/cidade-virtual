package com.eul4.common.util;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.StructurePiece;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoundingBoxUtil
{
	public static void removeEntities(BoundingBox boundingBox, World world)
	{
		for(Chunk chunk : getChunks(boundingBox, world))
		{
			for(Entity entity : chunk.getEntities())
			{
				if(boundingBox.contains(entity.getLocation().toVector()))
				{
					entity.remove();
				}
			}
		}
	}
	
	public static List<BoundingBox> getBoundingBoxes(GeneratedStructure generatedStructure)
	{
		List<BoundingBox> boundingBoxes = new ArrayList<>();
		
		for(StructurePiece piece : generatedStructure.getPieces())
		{
			boundingBoxes.add(piece.getBoundingBox());
		}
		
		return boundingBoxes;
	}
	
	public static Set<Chunk> getChunks(GeneratedStructure generatedStructure, World world)
	{
		return getChunks(getBoundingBoxes(generatedStructure), world);
	}
	
	public static Set<Chunk> getChunks(List<BoundingBox> boundingBoxes, World world)
	{
		Set<Chunk> chunks = new HashSet<>();
		
		for(BoundingBox boundingBox : boundingBoxes)
		{
			chunks.addAll(getChunks(boundingBox, world));
		}
		
		return chunks;
	}
	
	public static Set<Chunk> getChunks(BoundingBox boundingBox, World world)
	{
		Set<Chunk> chunks = new HashSet<>();
		
		final int minX = (int) Math.floor(boundingBox.getMinX()) >> 4;
		final int minZ = (int) Math.floor(boundingBox.getMinZ()) >> 4;
		final int maxX = (int) Math.floor(boundingBox.getMaxX()) >> 4;
		final int maxZ = (int) Math.floor(boundingBox.getMaxZ()) >> 4;
		
		for(int x = minX; x <= maxX; x++)
		{
			for(int z = minZ; z <= maxZ; z++)
			{
				chunks.add(world.getChunkAt(x, z));
			}
		}
		
		return chunks;
	}
}
