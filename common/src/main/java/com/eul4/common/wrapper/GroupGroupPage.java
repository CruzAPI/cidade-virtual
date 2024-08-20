package com.eul4.common.wrapper;

import com.eul4.common.Common;
import com.eul4.common.model.permission.GroupGroup;

import java.util.List;

public class GroupGroupPage extends ExpirablePage<GroupGroup>
{
	public GroupGroupPage(Common plugin, int index, int pageSize, List<GroupGroup> pageElements, List<GroupGroup> fullList)
	{
		super(plugin, index, pageSize, pageElements, fullList);
	}
}
