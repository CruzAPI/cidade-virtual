package com.eul4.common.i18n;

public enum CommonBundleBaseName implements BundleBaseName
{
	COMMON("common"),
	COMMON_RICH_MESSAGE("common_rich_message"),
	;
	
	private final String bundleBaseName;
	
	CommonBundleBaseName(String bundleBaseName)
	{
		this.bundleBaseName = bundleBaseName;
	}
	
	@Override
	public String getName()
	{
		return bundleBaseName;
	}
}
