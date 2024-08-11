package com.eul4.common.model.permission;

import com.eul4.common.Common;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class GroupUserMap extends ExpirableMap<UUID, GroupUser>
{
	private final HashMap<UUID, GroupUser> groupUsers;
	
	public GroupUserMap()
	{
		this(new HashMap<>());
	}
	
	private GroupUserMap(HashMap<UUID, GroupUser> groupUsers)
	{
		super(groupUsers);
		this.groupUsers = groupUsers;
	}
	
	public void put(GroupUser groupUser)
	{
		groupUsers.put(groupUser.getUserUniqueId(), groupUser);
	}
	
	public void compute(Common plugin, GroupUser groupUser)
	{
		computeExpirable(plugin, groupUser.getUserUniqueId(), groupUser);
	}
}
