package com.eul4.common.i18n;

import net.kyori.adventure.text.Component;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Message
{
	private static final Pattern PATTERN = Pattern.compile("(?=.+)((?:[^{]|(?<=\\\\).)*)(?:\\{([0-9]*)[^}]*\\})?");
	
	private final BundleBaseName bundleBaseName;
	private final String key;
	private final Component baseComponent;
	private final BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction;
	
	protected Message(BundleBaseName bundleBaseName,
			String key)
	{
		this(bundleBaseName, key, Component.empty());
	}
	
	protected Message(BundleBaseName bundleBaseName,
			String key,
			Component baseComponent)
	{
		this(bundleBaseName, key, baseComponent, (bundle, args) -> new Component[0]);
	}
	
	protected Message(BundleBaseName bundleBaseName,
			String key,
			Component baseComponent,
			BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		this.bundleBaseName = bundleBaseName;
		this.key = key;
		this.baseComponent = baseComponent;
		this.componentBiFunction = componentBiFunction;
	}
	
	public String getTemplate(Locale locale)
	{
		final ResourceBundle bundle = ResourceBundleHandler.getBundle(bundleBaseName, locale);
		return bundle.getString(key);
	}
	
	public Component translate(Locale locale, Object... args)
	{
		final ResourceBundle bundle = ResourceBundleHandler.getBundle(bundleBaseName, locale);
		final String template = getTemplate(locale);
		final Matcher matcher = PATTERN.matcher(template);
		final Component[] extra = componentBiFunction.apply(bundle, args);
		
		Component component = baseComponent;
		
		while(matcher.find())
		{
			String baseText = matcher.group(1);
			component = component.append(Component.text(baseText));
			
			String index = matcher.group(2);
			
			if(index != null)
			{
				int i = Integer.parseInt(index);
				component = component.append(extra[i]);
			}
		}
		
		return component;
	}
}