package com.eul4.i18n;

public enum CommonBundleBaseName implements BundleBaseName
{
	COMMON("common");
	
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
