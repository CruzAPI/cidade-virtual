package com.eul4.enums;

import com.eul4.common.i18n.Message;
import com.eul4.i18n.PluginMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

@RequiredArgsConstructor
@Getter
public enum Rarity
{
	COMMON
	(
		(byte) 0, 1.0F, 1.0F, 1, 50,
		PluginMessage.COMMON,
		PluginMessage.RARITY_COMMON,
		PluginMessage.COMMON_INCOMPATIBILITY_$CONTAINER_TITLE,
		style(GREEN, BOLD),
		BossBar.Color.GREEN
	),
	
	RARE
	(
		(byte) 1, 10.0F, 10.0F, 3, 100,
		PluginMessage.RARE,
		PluginMessage.RARITY_RARE,
		PluginMessage.RARE_INCOMPATIBILITY_$CONTAINER_TITLE,
		style(DARK_PURPLE, BOLD),
		BossBar.Color.PURPLE
	),
	
	LEGENDARY
	(
		(byte) 2, 100.0F, 100.0F, 9, 300,
		PluginMessage.LEGENDARY,
		PluginMessage.RARITY_LEGENDARY,
		PluginMessage.LEGENDARY_INCOMPATIBILITY_$CONTAINER_TITLE,
		style(GOLD, BOLD),
		BossBar.Color.RED
	),
	;
	
	public static final Rarity DEFAULT_RARITY = COMMON;
	public static final Rarity MAX_RARITY = LEGENDARY;
	
	private final byte id;
	private final float maxHealth;
	private final float explosionMultiplierDamage;
	private final int bookshelfBonus;
	private final int enchantmentRandomBound;
	private final Message rawMessage;
	private final Message stylizedMessage;
	private final Message containerIncompatibilityMessage;
	private final Style style;
	private final BossBar.Color bossBarColor;
	
	public static Rarity getRarityById(byte id)
	{
		for(Rarity rarity : values())
		{
			if(rarity.getId() == id)
			{
				return rarity;
			}
		}
		
		return null;
	}
	
	public int getMaxEnchantmentBonus()
	{
		return bookshelfBonus * 15;
	}
}
