package com.eul4.common.wrapper;

import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.UUID;

@RequiredArgsConstructor
public class UUIDHashSet
{
	private final HashSet<UUID> uuids;
	
	public UUIDHashSet()
	{
		uuids = new HashSet<>();
	}
	
	public HashSet<UUID> getHashSet()
	{
		return uuids;
	}
	
	public boolean contains(UUID uuid)
	{
		return uuids.contains(uuid);
	}
	
	public boolean add(UUID uuid)
	{
		return uuids.add(uuid);
	}
	
	public boolean remove(UUID uuid)
	{
		return uuids.remove(uuid);
	}
	
	public int size()
	{
		return uuids.size();
	}
}
