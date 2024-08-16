package com.eul4.common.i18n;

import lombok.Getter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Locale;
import java.util.function.BiFunction;

@Getter
public enum CommonRichMessage implements RichMessage
{
	HELLO_WORLD("hello.world"),
	HELLO_PLAYER("hello.player", (locale, args) -> new TagResolver[]
	{
		Placeholder.unparsed("player", args[0].toString()),
	}),
	HELLO_WORLD_LORE("hello.world.lore"),
	;
	
	private final String key;
	private final BiFunction<Locale, Object[], TagResolver[]> tagResolversFunction;
	
	CommonRichMessage(String key)
	{
		this(key, (locale, args) -> new TagResolver[0]);
	}
	
	CommonRichMessage(String key, BiFunction<Locale, Object[], TagResolver[]> tagResolversFunction)
	{
		this.key = key;
		this.tagResolversFunction = tagResolversFunction;
	}
	
	@Override
	public BundleBaseName getBundleBaseName()
	{
		return CommonBundleBaseName.COMMON_RICH_MESSAGE;
	}
}
