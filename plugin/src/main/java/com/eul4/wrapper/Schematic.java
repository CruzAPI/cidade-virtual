package com.eul4.wrapper;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.data.BlockData;

@Getter
@Setter
public class Schematic
{
	private static final BlockData[][][] EMPTY_VECTOR_3D = new BlockData[0][0][0];
	
	private final BlockData[][][] blockDataVector3D;
	private final CuboidRegion region;
	
	private BlockVector3 origin;
	
	public Schematic(CuboidRegion region)
	{
		this.region = region;
		this.origin = region.getMinimumPoint();
		
		final int x = region.getWidth();
		final int y = region.getHeight();
		final int z = region.getLength();
		
		this.blockDataVector3D = new BlockData[x][y][z];
	}
	
	private void write()
	{
		for(int x = 0; x < blockDataVector3D.length; x++)
		{
			for(int y = 0; y < blockDataVector3D[x].length; y++)
			{
				for(int z = 0; z < blockDataVector3D[x][y].length; z++)
				{
					//write...
				}
			}
		}
	}
	
	private void read()
	{
		int X = 10;
		int Y = 10;
		int Z = 10;
		
		BlockData[][][] blockDataVector3D = new BlockData[X][Y][Z];
		
		for(int x = 0; x < blockDataVector3D.length; x++)
		{
			for(int y = 0; y < blockDataVector3D[x].length; y++)
			{
				for(int z = 0; z < blockDataVector3D[x][y].length; z++)
				{
//					blockDataVector3D[x][y][z] = read
				}
			}
		}
	}
	
}
