package com.eul4.wrapper;

import com.eul4.model.town.structure.Structure;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.UUID;

@RequiredArgsConstructor
public class StructureMap extends HashMap<UUID, Structure>
{
	private final UUID uuid;
	
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
