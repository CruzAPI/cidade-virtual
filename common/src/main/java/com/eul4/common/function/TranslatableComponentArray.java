package com.eul4.common.function;

import net.kyori.adventure.text.Component;

import java.util.ResourceBundle;

@FunctionalInterface
public interface TranslatableComponentArray
{
	Component[] translate(ResourceBundle bundle, Object... args);
}
