package com.eul4;

import com.eul4.common.i18n.Message;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.function.StructureInstantiation;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.craft.town.structure.CraftDislikeGenerator;
import com.eul4.model.craft.town.structure.CraftLikeGenerator;
import com.eul4.model.craft.town.structure.CraftTownHall;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.inventory.craft.CraftDislikeGeneratorGui;
import com.eul4.model.inventory.craft.CraftLikeGeneratorGui;
import com.eul4.model.inventory.craft.CraftTownHallGui;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.DislikeGenerator;
import com.eul4.model.town.structure.LikeGenerator;
import com.eul4.model.town.structure.Structure;
import com.eul4.model.town.structure.TownHall;
import com.eul4.rule.*;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
public class StructureTypeEnum<S extends Structure, A extends GenericAttribute> implements StructureType<S, A>
{
	public static final List<StructureType<?, ?>> VALUES = new ArrayList<>();
	
	public static final StructureTypeEnum<TownHall, TownHallAttribute> TOWN_HALL = new StructureTypeEnum<>(
			CraftTownHall::new,
			CraftTownHall::new,
			CraftTownHallGui::new,
			Main::getTownHallRule,
			"TOWN_HALL",
			NamedTextColor.YELLOW,
			PluginMessage.STRUCTURE_TOWN_HALL_NAME,
			PluginMessage.STRUCTURE_TOWN_HALL_UPGRADE_PREVIEW_LORE
	);
	
	public static final StructureTypeEnum<LikeGenerator, LikeGeneratorAttribute> LIKE_GENERATOR = new StructureTypeEnum<>(
			CraftLikeGenerator::new,
			CraftLikeGenerator::new,
			CraftLikeGeneratorGui::new,
			Main::getLikeGeneratorRule,
			"LIKE_GENERATOR",
			NamedTextColor.GREEN,
			PluginMessage.STRUCTURE_LIKE_GENERATOR_NAME,
			PluginMessage.STRUCTURE_LIKE_GENERATOR_UPGRADE_PREVIEW_LORE
	);
	
	public static final StructureTypeEnum<DislikeGenerator, DislikeGeneratorAttribute> DISLIKE_GENERATOR = new StructureTypeEnum<>(
			CraftDislikeGenerator::new,
			CraftDislikeGenerator::new,
			CraftDislikeGeneratorGui::new,
			Main::getDislikeGeneratorRule,
			"DISLIKE_GENERATOR",
			NamedTextColor.RED,
			PluginMessage.STRUCTURE_DISLIKE_GENERATOR_NAME,
			PluginMessage.STRUCTURE_DISLIKE_GENERATOR_UPGRADE_PREVIEW_LORE
	);
	
	private final StructureInstantiation instantiation;
	private final Function<Town, S> newStructureTown;
	private final BiFunction<CommonPlayer, Structure, StructureGui> newStructureGui;
	private final Function<Main, Rule<A>> ruleFunction;
	private final String name;
	private final TextColor color;
	private final Message nameMessage;
	private final Message upgradePreviewLoreMessage;
	
	@Accessors(fluent = true)
	private final int ordinal;
	
	public StructureTypeEnum(StructureInstantiation instantiation,
			Function<Town, S> newStructureTown,
			BiFunction<CommonPlayer, Structure, StructureGui> newStructureGui,
			Function<Main, Rule<A>> ruleFunction,
			String name,
			TextColor color,
			Message nameMessage,
			Message upgradePreviewLoreMessage)
	{
		this.instantiation = instantiation;
		this.newStructureTown = newStructureTown;
		this.newStructureGui = newStructureGui;
		this.ruleFunction = ruleFunction;
		this.name = name;
		this.color = color;
		this.nameMessage = nameMessage;
		this.upgradePreviewLoreMessage = upgradePreviewLoreMessage;
		this.ordinal = VALUES.size();
		
		VALUES.add(this);
	}
	
	public static List<StructureType<?, ?>> values()
	{
		return VALUES;
	}
	
	public static StructureType<?, ?> valueOf(String name)
	{
		for(StructureType<?, ?> type : values())
		{
			if(name.equals(type.name()))
			{
				return type;
			}
		}
		
		throw new IllegalArgumentException();
	}
	
	@Override
	public Rule<A> getRule(Main plugin)
	{
		return ruleFunction.apply(plugin);
	}
	
	public String name()
	{
		return name;
	}
}
