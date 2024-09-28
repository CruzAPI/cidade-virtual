package com.eul4.common.i18n;

import java.util.*;

public class ResourceBundleHandler
{
	public static final Map<String, ResourceBundle> RESOURCE_BUNDLES = new HashMap<>();
	public static final Locale[] SUPPORTED_LOCALES = new Locale[] { new Locale("pt", "BR") };
	public static final Locale DEFAULT_LOCALE = SUPPORTED_LOCALES[0];
	
	public static void registerBundle(ResourceBundle bundle)
	{
		RESOURCE_BUNDLES.put(getFileName(bundle), bundle);
	}
	
	public static ResourceBundle getBundle(BundleBaseName bundleBaseName, Locale locale) throws MissingResourceException
	{
		return Optional.ofNullable(RESOURCE_BUNDLES.get(getFileName(bundleBaseName, locale))).orElseThrow(
				() -> new MissingResourceException("Can't find bundle for base name " + bundleBaseName.getName()
						+ ", locale " + locale, "", ""));
	}
	
	public static String getFileName(ResourceBundle bundle)
	{
		return getFileName(bundle.getBaseBundleName(), bundle.getLocale());
	}
	
	private static String getFileName(BundleBaseName bundleBaseName, Locale locale)
	{
		return getFileName(bundleBaseName.getName(), locale);
	}
	
	private static String getFileName(String baseName, Locale locale)
	{
		return baseName + "_" + locale;
	}
}
