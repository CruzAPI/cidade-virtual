package com.eul4.common.i18n;

import com.eul4.common.model.player.CommonPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.List;
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
	
	default String getTemplate(CommonPlayer commonPlayer)
	{
		return getTemplate(commonPlayer.getLocale());
	}
	
	default String getTemplate(ResourceBundle bundle)
	{
		return bundle.getString(getKey());
	}
	
	default String getTemplate(Locale locale)
	{
		return getTemplate(ResourceBundleHandler.getBundle(getBundleBaseName(), locale));
	}
	
	private Component translateTemplate(String template, ResourceBundle bundle, Object... args)
	{
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
		
		if(!component.hasDecoration(TextDecoration.ITALIC))
		{
			component = component.decoration(TextDecoration.ITALIC, false);
		}
		
		return component;
	}
	
	default List<Component> translateLore(CommonPlayer commonPlayer, Object... args)
	{
		return translateLore(commonPlayer.getLocale(), args);
	}
	
	default List<Component> translateLore(Locale locale, Object... args)
	{
		final ResourceBundle bundle = ResourceBundleHandler.getBundle(getBundleBaseName(), locale);
		final String[] templateSplit = getTemplate(bundle).split("\\n");
		final List<Component> components = new ArrayList<>();
		
		for(String template : templateSplit)
		{
			components.add(translateTemplate(template, bundle, args));
		}
		
		return components;
	}
	
	default Component translateWord(CommonPlayer commonPlayer, UnaryOperator<String> operator)
	{
		return translateWord(commonPlayer.getLocale(), operator);
	}
	
	default Component translateWord(ResourceBundle bundle)
	{
		return translateWord(bundle, UnaryOperator.identity());
	}
	
	default Component translateWord(ResourceBundle bundle, UnaryOperator<String> operator)
	{
		return translateWord(bundle.getLocale(), operator);
	}
	
	default Component translateWord(Locale locale)
	{
		return translateWord(locale, UnaryOperator.identity());
	}
	
	default Component translateWord(Locale locale, UnaryOperator<String> operator)
	{
		final ResourceBundle bundle = ResourceBundleHandler.getBundle(getBundleBaseName(), locale);
		String translatedWord = bundle.getString(getKey());
		return Component.text(operator.apply(translatedWord)).decoration(TextDecoration.ITALIC, false);
	}
	
	default Component translate(ResourceBundle bundle, Object... args)
	{
		return translate(bundle, UnaryOperator.identity(), args);
	}
	
	default Component translate(ResourceBundle bundle, UnaryOperator<String> operator, Object... args)
	{
		return translate(bundle.getLocale(), operator, args);
	}
	
	default String translateToLegacyText(CommonPlayer commonPlayer, Object... args)
	{
		return LegacyComponentSerializer.legacySection().serialize(translate(commonPlayer, args));
	}
	
	default Component translate(CommonPlayer commonPlayer, Object... args)
	{
		return translate(commonPlayer, UnaryOperator.identity(), args);
	}
	
	default Component translate(CommonPlayer commonPlayer, UnaryOperator<String> operator, Object... args)
	{
		return translate(commonPlayer.getLocale(), operator, args);
	}
	
	default Component translate(Locale locale, Object... args)
	{
		return translate(locale, UnaryOperator.identity(), args);
	}
	
	default Component translate(Locale locale, UnaryOperator<String> operator, Object... args)
	{
		if(isUntranslatable())
		{
			return getUntranslatableComponentBiFunction().apply(locale, args);
		}
		
		final ResourceBundle bundle = ResourceBundleHandler.getBundle(getBundleBaseName(), locale);
		final String template = getTemplate(bundle);
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
	
	default MessageArgs withArgs(Object... args)
	{
		return new MessageArgs(this, args);
	}
	
	default boolean hasTranslation()
	{
		return getKey() != null;
	}
	
	default boolean isUntranslatable()
	{
		return getUntranslatableComponentBiFunction() != null;
	}
	
	default BiFunction<Locale, Object[], Component> getUntranslatableComponentBiFunction()
	{
		return null;
	}
	
	String name();
}