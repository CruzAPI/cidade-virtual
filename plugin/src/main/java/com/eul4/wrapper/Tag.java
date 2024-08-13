package com.eul4.wrapper;

import com.eul4.common.i18n.Message;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.player.PluginPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Locale;
import java.util.Set;

import static com.eul4.i18n.PluginMessage.*;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

@RequiredArgsConstructor
@Getter
public enum Tag
{
	OWNER(TAG_OWNER, empty().color(DARK_RED).decorate(ITALIC), Set.of("owner", "dono")),
	ADMIN(TAG_ADMIN, empty().color(RED), Set.of("admin")),
	VIP(TAG_VIP, empty().color(GREEN), Set.of("vip")),
	MAYOR(TAG_MAYOR, empty().color(YELLOW), Set.of("mayor", "prefeito")),
	DEPUTY_MAYOR(TAG_DEPUTY_MAYOR, empty().color(YELLOW), Set.of("deputy-mayor", "vice-prefeito")),
	ALPHA(TAG_ALPHA, empty().color(DARK_PURPLE), Set.of("alpha")),
	
	TOWNEE(TAG_TOWNEE, empty().color(GRAY), Set.of("townee", "cidadão"))
	{
		@Override
		public boolean hasTag(PluginPlayer pluginPlayer)
		{
			return super.hasTag(pluginPlayer) || pluginPlayer.hasTown();
		}
	},
	
	INDIGENT(TAG_INDIGENT, empty().color(DARK_GRAY), Set.of("indigent", "indigente"))
	{
		@Override
		public boolean hasTag(PluginPlayer pluginPlayer)
		{
			return super.hasTag(pluginPlayer) || !pluginPlayer.hasTown();
		}
	},
	
	;
	
	private final Message message;
	private final Component displayNameComponent;
	private final Set<String> aliases;
	
	public boolean hasTag(PluginPlayer pluginPlayer)
	{
		return pluginPlayer.hasPermission(getPermission());
	}
	
	public String getPermission()
	{
		return "tag." + getName();
	}
	
	public static Tag getTag(String aliases)
	{
		aliases = aliases.toLowerCase();
		
		for(Tag tag : Tag.values())
		{
			if(tag.name().equalsIgnoreCase(aliases) || tag.aliases.contains(aliases))
			{
				return tag;
			}
		}
		
		return null;
	}
	
	public String getName()
	{
		return name().toLowerCase().replace("_", "-");
	}
	
	public Component getTagComponentTranslated(CommonPlayer commonPlayer)
	{
		return getTagComponentTranslated(commonPlayer.getLocale());
	}
	
	public Component getTagComponentTranslated(Locale locale)
	{
		return text("[")
				.append(message.translateOne(locale))
				.append(text("]"))
				.color(GRAY);
	}
}
