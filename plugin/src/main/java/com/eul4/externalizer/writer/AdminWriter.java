package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.player.Admin;

import java.io.IOException;

public class AdminWriter extends PluginPlayerWriter<Admin>
{
	public AdminWriter(Writers writers)
	{
		super(writers, Admin.class);
	}
	
	@Override
	protected void writeObject(Admin admin) throws IOException
	{
		super.writeObject(admin);
	}
}
