package com.eul4.common.i18n;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Message extends TranslatableProcessableMessage
{
	Pattern PATTERN = Pattern.compile("(?=.+)((?:[^{]|(?<=\\\\).)*)(?:\\{([0-9]*)[^}]*\\})?");
	
	BundleBaseName getBundleBaseName();
	String getKey();
	BiFunction<ResourceBundle, Object[], Component[]> getComponentBiFunction();
	
	default BiFunction<Locale, Object[], List<Component>> getUntranslatableComponentBiFunction()
	{
		return null;
	}
	
	@Override
	default Component translate(Locale locale, UnaryOperator<String> preProcessor, Object... args)
	{
		return translateLines(locale, preProcessor, args).get(0);
	}
	
	default List<Component> translateLines(Locale locale, UnaryOperator<String> operator, Object... args)
	{
		if(isUntranslatable())
		{
			return getUntranslatableComponentBiFunction().apply(locale, args);
		}
		
		final ResourceBundle bundle = ResourceBundleHandler.getBundle(getBundleBaseName(), locale);
		final String[] splitTemplates = getTemplate(bundle).split("\\n");
		final List<Component> components = new ArrayList<>();
		
		for(String splitTemplate : splitTemplates)
		{
			components.add(translateSplitTemplate(splitTemplate, bundle, operator, args));
		}
		
		return components;
	}
	
	private String getTemplate(ResourceBundle bundle)
	{
		return bundle.getString(getKey());
	}
	
	private Component translateSplitTemplate(String template, ResourceBundle bundle, UnaryOperator<String> operator, Object... args)
	{
		final Matcher matcher = PATTERN.matcher(template);
		final Component[] components = getComponentBiFunction().apply(bundle, args);
		
		Component component = components[0];
		
		while(matcher.find())
		{
			String baseText = operator.apply(matcher.group(1));
			component = component.append(Component.text(baseText));
			
			String index = matcher.group(2);
			
			if(index != null)
			{
				int i = Integer.parseInt(index);
				component = component.append(components[i + 1]);
			}
		}
		
		if(!component.hasDecoration(TextDecoration.ITALIC))
		{
			component = component.decoration(TextDecoration.ITALIC, false);
		}
		
		return component;
	}
	
	private boolean isUntranslatable()
	{
		return getUntranslatableComponentBiFunction() != null;
	}
}