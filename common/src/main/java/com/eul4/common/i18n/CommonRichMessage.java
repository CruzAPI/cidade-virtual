package com.eul4.common.i18n;

import lombok.Getter;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Locale;
import java.util.function.BiFunction;

import static com.eul4.common.i18n.CommonMessage.COMMAND_REPLY_USAGE_$ALIASES;
import static com.eul4.common.util.CommonMessageUtil.*;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;

@Getter
public enum CommonRichMessage implements RichMessage
{
	COMMAND_REPLY_STATUS_$PLAYER_$ALIASES("command.reply.status", (locale, args) -> new TagResolver[]
	{
		component("player", displayName(args[0])),
		component("usage", COMMAND_REPLY_USAGE_$ALIASES.translate(locale, args[1])),
	}),
	
	PAGE_GROUP_GROUP_TITLE("page.group-group.title", (bundle, args) -> new TagResolver[]
	{
		component("amount", argToComponent(args[0])),
		component("name", inParentheses(args[1])),
	}),
	
	PAGE_GROUP_USER_TITLE("page.group-user.title", (bundle, args) -> new TagResolver[]
	{
		component("amount", argToComponent(args[0])),
		component("name", inParentheses(args[1])),
	}),
	
	PAGE_GROUP_PERM_TITLE("page.group-perm.title", (bundle, args) -> new TagResolver[]
	{
		component("amount", argToComponent(args[0])),
		component("name", inParentheses(args[1])),
	}),
	
	PAGE_USER_PERM_TITLE("page.user-perm.title", (bundle, args) -> new TagResolver[]
	{
		component("amount", argToComponent(args[0])),
		component("name", inParentheses(args[1])),
	}),
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
