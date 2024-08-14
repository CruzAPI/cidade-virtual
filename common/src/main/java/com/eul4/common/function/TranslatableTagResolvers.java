package com.eul4.common.function;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ResourceBundle;

@FunctionalInterface
public interface TranslatableTagResolvers
{
	TagResolver[] translate(ResourceBundle bundle, Object... args);
}
