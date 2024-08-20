package com.eul4.i18n;

import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.RichMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Locale;
import java.util.function.BiFunction;

@RequiredArgsConstructor
@Getter
public enum PluginRichMessage implements RichMessage
{
	COMMAND_TOGGLE_COMBAT_NEW("command.toggle-combat.new"),
	COMMAND_TOGGLE_COMBAT_OLD("command.toggle-combat.old"),
	;
	
	private final String key;
	private final BiFunction<Locale, Object[], TagResolver[]> tagResolversFunction;
	
	PluginRichMessage(String key)
	{
		this(key, (locale, args) -> new TagResolver[0]);
	}
	
	@Override
	public BundleBaseName getBundleBaseName()
	{
		return PluginBundleBaseName.PLUGIN_RICH_MESSAGE;
	}
}
