package com.eul4.i18n;

import com.eul4.common.i18n.BundleBaseName;

public enum PluginBundleBaseName implements BundleBaseName
{
	PLUGIN("plugin"),
	TOWN_SCOREBOARD("town_scoreboard"),
	ANALYZER_SCOREBOARD("analyzer_scoreboard"),
	;
	
	private final String bundleBaseName;
	
	PluginBundleBaseName(String bundleBaseName)
	{
		this.bundleBaseName = bundleBaseName;
	}
	
	@Override
	public String getName()
	{
		return bundleBaseName;
	}
}
