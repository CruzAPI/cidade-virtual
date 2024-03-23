package com.eul4.common.i18n;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

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
	private final BaseComponent baseComponent;
	private final BiFunction<ResourceBundle, Object[], BaseComponent[]> componentBiFunction;
	
	protected Message(BundleBaseName bundleBaseName,
			String key)
	{
		this(bundleBaseName, key, new TextComponent());
	}
	
	protected Message(BundleBaseName bundleBaseName,
			String key,
			BaseComponent baseComponent)
	{
		this(bundleBaseName, key, baseComponent, (bundle, args) -> new BaseComponent[0]);
	}
	
	protected Message(BundleBaseName bundleBaseName,
			String key,
			BaseComponent baseComponent,
			BiFunction<ResourceBundle, Object[], BaseComponent[]> componentBiFunction)
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
	
	public BaseComponent translate(Locale locale, Object... args)
	{
		final ResourceBundle bundle = ResourceBundleHandler.getBundle(bundleBaseName, locale);
		final String template = getTemplate(locale);
		final Matcher matcher = PATTERN.matcher(template);
		final BaseComponent component = baseComponent.duplicate();
		Bukkit.broadcastMessage(baseComponent.getColor().getName());
		final BaseComponent[] extra = componentBiFunction.apply(bundle, args);
		
		while(matcher.find())
		{
			final String baseText = matcher.group(1);
			final String index = matcher.group(2);
			
			component.addExtra(baseText);
			
			if(index != null)
			{
				component.addExtra(extra[Integer.parseInt(index)]);
			}
		}
		
		return component;
	}
}