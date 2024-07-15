package com.eul4.wrapper;

import java.util.HashMap;

public class BoughtTileMapByDepth extends HashMap<Integer, Integer>
{
	public int getTilesBoughtInDepth(int depth)
	{
		return this.getOrDefault(depth, 0);
	}
	
	public void incrementTilesBoughtInDepth(int depth)
	{
		this.compute(depth, (k, v) -> v == null ? 1 : v + 1);
	}
}
