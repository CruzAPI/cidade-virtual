package com.eul4.common.model.permission;

import lombok.Getter;

import java.util.Map;
import java.util.TreeMap;

@Getter
public class GroupMap
{
	private final TreeMap<Group, Group> groups;
	
	public GroupMap()
	{
		groups = new TreeMap<>();
	}
	
	public void put(Group group)
	{
		groups.put(group, group);
	}
	
	public boolean containsKeyEqual(Group group)
	{
		return getEqual(group) != null;
	}
	
	public Group getEqual(Group group)
	{
		for(Map.Entry<Group, Group> entry : groups.entrySet())
		{
			if(entry.getKey().equals(group))
			{
				return entry.getKey();
			}
		}
		
		return null;
	}
	
	public Group removeEqual(Group group)
	{
		Group groupToRemove = getEqual(group);
		
		if(groupToRemove == null)
		{
			return null;
		}
		
		return groups.remove(groupToRemove);
	}
}
