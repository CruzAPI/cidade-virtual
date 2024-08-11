package com.eul4.common.externalizer.writer;

import com.eul4.common.model.permission.Group;
import com.eul4.common.model.permission.Permission;
import com.eul4.common.model.permission.GroupMap;
import com.eul4.common.type.player.Writers;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class GroupMapWriter extends ObjectWriter<GroupMap>
{
	public GroupMapWriter(Writers writers)
	{
		super(writers, GroupMap.class);
	}
	
	@Override
	protected void writeObject(GroupMap groupMap) throws IOException
	{
		GroupWriter groupWriter = writers.getWriter(GroupWriter.class);
		
		TreeMap<Group, Group> groups = groupMap.getGroups();
		
		out.writeInt(groups.size());
		
		for(Group group : groups.values())
		{
			groupWriter.writeReference(group);
		}
	}
}
