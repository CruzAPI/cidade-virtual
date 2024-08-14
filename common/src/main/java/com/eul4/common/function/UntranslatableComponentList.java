package com.eul4.common.function;

import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Locale;

@FunctionalInterface
public interface UntranslatableComponentList
{
	List<Component> translate(Locale locale, Object... args);
}
