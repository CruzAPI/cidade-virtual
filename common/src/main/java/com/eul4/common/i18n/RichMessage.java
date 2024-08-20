package com.eul4.common.i18n;

import com.eul4.common.model.player.CommonPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

public interface RichMessage extends TranslatableMessage
{
	BundleBaseName getBundleBaseName();
	String getKey();
	BiFunction<Locale, Object[], TagResolver[]> getTagResolversFunction();
	
	default Component translate(CommonPlayer commonPlayer, Object... args)
	{
		return translate(commonPlayer.getLocale(), args);
	}
	
	default Component translate(Locale locale, Object... args)
	{
		return translate(locale, getTagResolversFunction().apply(locale, args));
	}
	
	default List<Component> translateLines(CommonPlayer commonPlayer, Object... args)
	{
		return translateLines(commonPlayer.getLocale(), args);
	}
	
	default List<Component> translateLines(Locale locale, Object... args)
	{
		return translateLines(locale, getTagResolversFunction().apply(locale, args));
	}
	
	private List<Component> translateLines(Locale locale, TagResolver... tagResolvers)
	{
		final ResourceBundle bundle = getBundle(locale);
		final String input = bundle.getString(getKey());
		final String[] inputLines = input.split("\\n");
		final List<Component> components = new ArrayList<>();
		
		for(String inputLine : inputLines)
		{
			components.add(deserialize(inputLine, tagResolvers));
		}
		
		return components;
	}
	
	private Component translate(Locale locale, TagResolver... tagResolvers)
	{
		ResourceBundle bundle = getBundle(locale);
		String input = bundle.getString(getKey());
		return deserialize(input, tagResolvers);
	}
	
	private Component deserialize(String input, TagResolver... tagResolvers)
	{
		return MiniMessage.miniMessage()
				.deserialize(input, tagResolvers)
				.applyFallbackStyle(TextDecoration.ITALIC.withState(false), NamedTextColor.WHITE);
	}
	
	private ResourceBundle getBundle(Locale locale)
	{
		return ResourceBundleHandler.getBundle(getBundleBaseName(), locale);
	}
	
	String name();
}