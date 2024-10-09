package com.eul4.i18n;

import com.eul4.common.i18n.BundleBaseName;

public enum PluginBundleBaseName implements BundleBaseName
{
	PLUGIN("plugin"),
	ANALYZER_SCOREBOARD("analyzer_scoreboard"),
	INITIAL_SCOREBOARD("initial_scoreboard"),
	TOWN_SCOREBOARD("town_scoreboard"),
	TOWN_TUTORIAL("town_tutorial"),
	PLUGIN_RICH_MESSAGE("plugin_rich_message"),
	BROADCAST_RICH_MESSAGE("broadcast_rich_message"),
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
