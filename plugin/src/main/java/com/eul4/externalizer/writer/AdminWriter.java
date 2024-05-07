package com.eul4.externalizer.writer;

import com.eul4.model.player.Admin;

import java.io.IOException;
import java.io.ObjectOutput;

public class AdminWriter extends PluginPlayerWriter<Admin>
{
	public AdminWriter(ObjectOutput out)
	{
		super(out);
	}
	
	@Override
	protected void writeObject(Admin admin) throws IOException
	{
		super.writeObject(admin);
	}
}
