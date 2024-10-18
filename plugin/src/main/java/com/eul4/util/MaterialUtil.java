package com.eul4.util;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

import java.util.Map;

import static net.kyori.adventure.text.format.Style.style;
import static org.bukkit.Material.DIAMOND;
import static org.bukkit.Material.EMERALD;

public class MaterialUtil
{
	public static final Map<Material, Style> MATERIAL_STYLE = Map.ofEntries
	(
//		Map.entry(DIAMOND, style(createColorFromHexString("#34EBE8"))),
//		Map.entry(EMERALD, style(createColorFromHexString("#50C878")))
			//TODO Agora quero que você faça o para todos os materiais
	);
	
	private static TextColor createColorFromHexString(String hexCode)
	{
		final TextColor color = TextColor.fromHexString(hexCode);
		return color == null ? NamedTextColor.WHITE : color;
	}
	
	public static Style getStyle(Material material)
	{
		return getStyle(material, Style.empty());
	}
	
	public static Style getStyle(Material material, Style defaultStyle)
	{
		return MATERIAL_STYLE.getOrDefault(material, defaultStyle);
	}
}
