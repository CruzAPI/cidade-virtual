package com.eul4.wrapper;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class BoughtTileMapByDepth extends HashMap<Integer, Integer>
{
	private final UUID uuid;
	
	public BoughtTileMapByDepth(UUID uuid, Map<Integer, Integer> map)
	{
		super(map);
		this.uuid = uuid;
	}
	
	public int getTilesBoughtInDepth(int depth)
	{
		return this.getOrDefault(depth, 0);
	}
	
	public void incrementTilesBoughtInDepth(int depth)
	{
		this.compute(depth, (k, v) -> v == null ? 1 : v + 1);
	}
	
	@Override
	public int hashCode()
	{
		return uuid.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		return this == o;
	}
}
