package com.eul4.common.externalizer.writer;

import com.eul4.common.model.permission.GroupUser;
import com.eul4.common.model.permission.GroupUserMap;
import com.eul4.common.type.player.Writers;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class GroupUserMapWriter extends ObjectWriter<GroupUserMap>
{
	public GroupUserMapWriter(Writers writers)
	{
		super(writers, GroupUserMap.class);
	}
	
	@Override
	protected void writeObject(GroupUserMap groupUserMap) throws IOException
	{
		GroupUserWriter groupUserWriter = writers.getWriter(GroupUserWriter.class);
		HashMap<UUID, GroupUser> groupUsers = groupUserMap.getGroupUsers();
		
		out.writeInt(groupUsers.size());
		
		for(GroupUser groupUser : groupUsers.values())
		{
			groupUserWriter.writeReference(groupUser);
		}
	}
}
