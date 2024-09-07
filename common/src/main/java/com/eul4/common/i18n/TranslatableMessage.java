package com.eul4.common.i18n;

import com.eul4.common.model.player.CommonPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.eul4.common.i18n.ResourceBundleHandler.DEFAULT_LOCALE;

public interface TranslatableMessage
{
	Component translate(Locale locale, Object... args);
	List<Component> translateLines(Locale locale, Object... args);
	
	String name();
	
	default MessageArgs withArgs(Object... args)
	{
		return new MessageArgs(this, args);
	}
	
	default Component translate(Object... args)
	{
		return translate(DEFAULT_LOCALE, args);
	}
	
	default Component translate(ResourceBundle bundle, Object... args)
	{
		return translate(bundle.getLocale(), args);
	}
	
	default Component translate(CommonPlayer commonPlayer, Object... args)
	{
		return translate(commonPlayer.getLocale(), args);
	}
	
	default List<Component> translateLines(ResourceBundle bundle, Object... args)
	{
		return translateLines(bundle.getLocale(), args);
	}
	
	default List<Component> translateLines(Object... args)
	{
		return translateLines(DEFAULT_LOCALE, args);
	}
	
	default List<Component> translateLines(CommonPlayer commonPlayer, Object... args)
	{
		return translateLines(commonPlayer.getLocale(), args);
	}
	
	default String translateLegacy(ResourceBundle bundle, Object... args)
	{
		return translateLegacy(bundle.getLocale(), args);
	}
	
	default String translateLegacy(CommonPlayer commonPlayer, Object... args)
	{
		return translateLegacy(commonPlayer.getLocale(), args);
	}
	
	default String translateLegacy(Locale locale, Object... args)
	{
		return LegacyComponentSerializer.legacySection().serialize(translate(locale, args));
	}
	
	default String translatePlain(ResourceBundle bundle, Object... args)
	{
		return translatePlain(bundle.getLocale(), args);
	}
	
	default String translatePlain(CommonPlayer commonPlayer, Object... args)
	{
		return translatePlain(commonPlayer.getLocale(), args);
	}
	
	default String translatePlain(Locale locale, Object... args)
	{
		return PlainTextComponentSerializer.plainText().serialize(translate(locale, args));
	}
}
