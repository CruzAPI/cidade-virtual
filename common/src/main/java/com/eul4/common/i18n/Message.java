package com.eul4.common.i18n;

import com.eul4.common.model.player.CommonPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;

import java.util.*;
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
	
	default List<Component> translateLore(CommonPlayer commonPlayer, Object... args)
	{
		return translateLore(commonPlayer.getLocale(), UnaryOperator.identity(), args);
	}
	
	default List<Component> translateLore(Locale locale, Object... args)
	{
		return translateLore(locale, UnaryOperator.identity(), args);
	}
	
	default List<Component> translateLore(Locale locale, UnaryOperator<String> operator, Object... args)
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
	
	default String translateLegacy(CommonPlayer commonPlayer, Object... args)
	{
		return translateLegacy(commonPlayer.getLocale(), args);
	}
	
	default String translateLegacy(Locale locale, Object... args)
	{
		return LegacyComponentSerializer.legacySection().serialize(translateOne(locale, args));
	}
	
	default String translatePlain(Locale locale, Object[] args)
	{
		return PlainTextComponentSerializer.plainText().serialize(translateOne(locale, args));
	}
	
	default Component translateOne(ResourceBundle bundle, Object... args)
	{
		return translateOne(bundle.getLocale(), args);
	}
	
	default Component translateOne(ResourceBundle bundle, UnaryOperator<String> operator, Object... args)
	{
		return translateOne(bundle.getLocale(), operator, args);
	}
	
	default Component translateOne(CommonPlayer commonPlayer, UnaryOperator<String> operator, Object... args)
	{
		return translateOne(commonPlayer.getLocale(), operator, args);
	}
	
	default Component translateOne(CommonPlayer commonPlayer, Object... args)
	{
		return translateOne(commonPlayer.getLocale(), args);
	}
	
	default Component translateOne(Locale locale, Object... args)
	{
		return translateOne(locale, UnaryOperator.identity(), args);
	}
	
	default Component translateOne(Locale locale, UnaryOperator<String> operator, Object... args)
	{
		return translateLore(locale, operator, args).get(0);
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
	
	default BiFunction<Locale, Object[], List<Component>> getUntranslatableComponentBiFunction()
	{
		return null;
	}
	
	String name();
}