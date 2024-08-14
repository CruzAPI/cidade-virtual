package com.eul4.common.i18n;

import com.eul4.common.model.player.CommonPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Message
{
	BundleBaseName getBundleBaseName();
	String getKey();
	
	default TagResolver[] translateTagResolvers(Locale locale, Object... args)
	{
		return new TagResolver[0];
	}
	
	default MessageArgs withArgs(Object... args)
	{
		return new MessageArgs(this, args);
	}
	
	default List<Component> translateLines(Locale locale, UnaryOperator<String> preProcessor, TagResolver... tagResolvers)
	{
		final ResourceBundle bundle = getBundle(locale);
		final String input = bundle.getString(getKey());
		final String[] inputLines = input.split("\\n");
		final List<Component> components = new ArrayList<>();
		
		for(String inputLine : inputLines)
		{
			components.add(deserialize(inputLine, preProcessor, tagResolvers));
		}
		
		return components;
	}
	
	default Component translate(Locale locale, UnaryOperator<String> preProcessor, TagResolver... tagResolvers)
	{
		ResourceBundle bundle = getBundle(locale);
		String input = bundle.getString(getKey());
		return deserialize(input, preProcessor, tagResolvers);
	}
	
	private Component deserialize(String input, UnaryOperator<String> preProcessor, TagResolver... tagResolvers)
	{
		return MiniMessage.miniMessage().deserialize(input, tagResolvers);
	}
	
	private ResourceBundle getBundle(Locale locale)
	{
		return ResourceBundleHandler.getBundle(getBundleBaseName(), locale);
	}
	
	
	
	
	
	
	
	String name();
}