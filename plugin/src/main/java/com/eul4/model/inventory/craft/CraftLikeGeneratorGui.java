package com.eul4.model.inventory.craft;

import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.town.structure.Generator;
import com.eul4.model.town.structure.LikeGenerator;
import com.eul4.model.town.structure.Structure;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;

public class CraftLikeGeneratorGui extends CraftGeneratorGui
{
	public CraftLikeGeneratorGui(CommonPlayer commonPlayer, Structure structure)
	{
		this(commonPlayer, (LikeGenerator) structure);
	}
	
	public CraftLikeGeneratorGui(CommonPlayer commonPlayer, LikeGenerator generator)
	{
		super(commonPlayer, generator, commonPlayer.createInventory(InventoryType.HOPPER, PluginMessage.STRUCTURE_GENERATOR_TITLE,
				PluginMessage.STRUCTURE_LIKE_GENERATOR_NAME,
				generator.getLevel(),
				generator.getBalance(),
				generator.getMaxBalance()));
	}
	
	@Override
	public Component getUpdatedTitleComponent()
	{
		Generator generator = (Generator) structure;
		
		return PluginMessage.STRUCTURE_GENERATOR_TITLE.translate(commonPlayer.getLocale(),
				PluginMessage.STRUCTURE_LIKE_GENERATOR_NAME,
				generator.getLevel(),
				generator.getBalance(),
				generator.getMaxBalance());
	}
}
