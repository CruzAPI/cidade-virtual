package com.eul4.common.i18n;

import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Message
{
	private static final Pattern PATTERN = Pattern.compile("(?=.+)((?:[^{]|(?<=\\\\).)*)(?:\\{([0-9]*)[^}]*\\})?");
	
	private final BundleBaseName bundleBaseName;
	@Getter
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
	
	public String getTemplate(ResourceBundle bundle)
	{
		return bundle.getString(key);
	}
	
	public String getTemplate(Locale locale)
	{
		return getTemplate(ResourceBundleHandler.getBundle(bundleBaseName, locale));
	}
	
	public Component translateWord(Locale locale)
	{
		return translateWord(locale, UnaryOperator.identity());
	}
	
	public Component translateWord(Locale locale, UnaryOperator<String> operator)
	{
		final ResourceBundle bundle = ResourceBundleHandler.getBundle(bundleBaseName, locale);
		String translatedWord = bundle.getString(key);
		return Component.text(operator.apply(translatedWord));
	}
	
	public Component translate(Locale locale, Object... args)
	{
		final ResourceBundle bundle = ResourceBundleHandler.getBundle(bundleBaseName, locale);
		final String template = bundle.getString(key);
		final Matcher matcher = PATTERN.matcher(template);
		final Component[] extra = componentBiFunction.apply(bundle, args);
		
		Component component = baseComponent;
		
		while(matcher.find())
		{
			final String baseText = matcher.group(1);
			final String index = matcher.group(2);
			
			component = component.append(Component.text(baseText));
			
			if(index != null)
			{
				component = component.append(extra[Integer.parseInt(index)]);
			}
		}
		
		return component;
	}
}