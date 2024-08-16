package com.eul4.common.i18n;

import com.eul4.common.model.player.CommonPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public interface TranslatableProcessableMessage extends TranslatableMessage
{
	Component translate(Locale locale, UnaryOperator<String> preProcessor, Object... args);
	List<Component> translateLines(Locale locale, UnaryOperator<String> preProcessor, Object... args);
	
	@Override
	default Component translate(Locale locale, Object... args)
	{
		return translate(locale, UnaryOperator.identity(), args);
	}
	
	@Override
	default List<Component> translateLines(Locale locale, Object... args)
	{
		return translateLines(locale, UnaryOperator.identity(), args);
	}
	
	default Component translate(ResourceBundle bundle, UnaryOperator<String> preProcessor, Object... args)
	{
		return translate(bundle.getLocale(), preProcessor, args);
	}
	
	default Component translate(CommonPlayer commonPlayer, UnaryOperator<String> preProcessor, Object... args)
	{
		return translate(commonPlayer.getLocale(), preProcessor, args);
	}
	
	default List<Component> translateLines(ResourceBundle bundle, UnaryOperator<String> preProcessor, Object... args)
	{
		return translateLines(bundle.getLocale(), preProcessor, args);
	}
	
	default List<Component> translateLines(CommonPlayer commonPlayer, UnaryOperator<String> preProcessor, Object... args)
	{
		return translateLines(commonPlayer.getLocale(), preProcessor, args);
	}
	
	default String translateLegacy(ResourceBundle bundle, UnaryOperator<String> preProcessor, Object... args)
	{
		return translateLegacy(bundle.getLocale(), preProcessor, args);
	}
	
	default String translateLegacy(CommonPlayer commonPlayer, UnaryOperator<String> preProcessor, Object... args)
	{
		return translateLegacy(commonPlayer.getLocale(), preProcessor, args);
	}
	
	default String translateLegacy(Locale locale, UnaryOperator<String> preProcessor, Object... args)
	{
		return LegacyComponentSerializer.legacySection().serialize(translate(locale, preProcessor, args));
	}
	
	default String translatePlain(ResourceBundle bundle, UnaryOperator<String> preProcessor, Object... args)
	{
		return translatePlain(bundle.getLocale(), preProcessor, args);
	}
	
	default String translatePlain(CommonPlayer commonPlayer, UnaryOperator<String> preProcessor, Object... args)
	{
		return translatePlain(commonPlayer.getLocale(), preProcessor, args);
	}
	
	default String translatePlain(Locale locale, UnaryOperator<String> preProcessor, Object... args)
	{
		return PlainTextComponentSerializer.plainText().serialize(translate(locale, preProcessor, args));
	}
}
