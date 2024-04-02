package com.eul4.common.i18n;

import net.kyori.adventure.text.Component;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Message
{
	Pattern PATTERN = Pattern.compile("(?=.+)((?:[^{]|(?<=\\\\).)*)(?:\\{([0-9]*)[^}]*\\})?");
	
	BundleBaseName getBundleBaseName();
	String getKey();
	BiFunction<ResourceBundle, Object[], Component[]> getComponentBiFunction();
	
	default String getTemplate(ResourceBundle bundle)
	{
		return bundle.getString(getKey());
	}
	
	default String getTemplate(Locale locale)
	{
		return getTemplate(ResourceBundleHandler.getBundle(getBundleBaseName(), locale));
	}
	
	default Component translateWord(Locale locale)
	{
		return translateWord(locale, UnaryOperator.identity());
	}
	
	default Component translateWord(Locale locale, UnaryOperator<String> operator)
	{
		final ResourceBundle bundle = ResourceBundleHandler.getBundle(getBundleBaseName(), locale);
		String translatedWord = bundle.getString(getKey());
		return Component.text(operator.apply(translatedWord));
	}
	
	default Component translate(Locale locale, Object... args)
	{
		final ResourceBundle bundle = ResourceBundleHandler.getBundle(getBundleBaseName(), locale);
		final String template = getTemplate(bundle);
		final Matcher matcher = PATTERN.matcher(template);
		final Component[] components = getComponentBiFunction().apply(bundle, args);
		
		Component component = components[0];
		
		while(matcher.find())
		{
			String baseText = matcher.group(1);
			component = component.append(Component.text(baseText));
			
			String index = matcher.group(2);
			
			if(index != null)
			{
				int i = Integer.parseInt(index);
				component = component.append(components[i + 1]);
			}
		}
		
		return component;
	}
}