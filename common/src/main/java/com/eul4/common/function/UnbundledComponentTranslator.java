package com.eul4.common.function;

import net.kyori.adventure.text.Component;

import java.util.Locale;

@FunctionalInterface
public interface UnbundledComponentTranslator
{
	Component translate(Locale locale, Object... args);
}
