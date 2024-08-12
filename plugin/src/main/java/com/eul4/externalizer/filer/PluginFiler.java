package com.eul4.externalizer.filer;

import com.eul4.Main;
import com.eul4.common.externalizer.filer.Filer;

public abstract class PluginFiler extends Filer
{
	protected final Main plugin;
	
	public PluginFiler(Main plugin, byte version)
	{
		super(plugin, version);
		this.plugin = plugin;
	}
}
