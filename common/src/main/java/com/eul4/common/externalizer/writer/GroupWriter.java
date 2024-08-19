package com.eul4.common.externalizer.writer;

import com.eul4.common.model.permission.Group;
import com.eul4.common.model.permission.User;
import com.eul4.common.type.player.Writers;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class GroupWriter extends ObjectWriter<Group>
{
	public GroupWriter(Writers writers)
	{
		super(writers, Group.class);
	}
	
	@Override
	protected void writeObject(Group group) throws IOException
	{
		writers.getWriter(UUIDWriter.class).writeReference(group.getGroupUniqueId());
		out.writeUTF(group.getName());
		writers.getWriter(PermissionMapWriter.class).writeReference(group.getPermissionMap());
		writers.getWriter(GroupUserMapWriter.class).writeReference(group.getGroupUserMap());
		writers.getWriter(GroupGroupMapWriter.class).writeReference(group.getGroupGroupMap());
		out.writeInt(group.getOrder());
	}
}
