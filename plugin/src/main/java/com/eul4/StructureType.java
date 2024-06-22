package com.eul4;

import com.eul4.common.i18n.Message;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.externalizer.reader.*;
import com.eul4.externalizer.writer.*;
import com.eul4.function.StructureInstantiation;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.craft.town.structure.*;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.inventory.craft.*;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import com.eul4.rule.Rule;
import com.eul4.rule.attribute.GenericAttribute;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A ordem dos enums importa, ao criar um novo enum sempre deixar ele por ultimo.
 *
 * //TODO: create ID in enum instead of use ordinal.
 */
@Getter
@RequiredArgsConstructor
public enum StructureType
{
	TOWN_HALL
	(
		CraftTownHall.class,
		CraftTownHall::new,
		CraftTownHall::new,
		TownHallWriter.class,
		TownHallReader.class,
		CraftTownHallGui::new,
		Main::getTownHallRule,
		NamedTextColor.YELLOW,
		PluginMessage.STRUCTURE_TOWN_HALL_NAME,
		PluginMessage.STRUCTURE_TOWN_HALL_UPGRADE_PREVIEW_LORE
	),
	
	LIKE_GENERATOR
	(
		CraftLikeGenerator.class,
		CraftLikeGenerator::new,
		CraftLikeGenerator::new,
		LikeGeneratorWriter.class,
		LikeGeneratorReader.class,
		CraftLikeGeneratorGui::new,
		Main::getLikeGeneratorRule,
		NamedTextColor.GREEN,
		PluginMessage.STRUCTURE_LIKE_GENERATOR_NAME,
		PluginMessage.STRUCTURE_LIKE_GENERATOR_UPGRADE_PREVIEW_LORE
	),
	
	DISLIKE_GENERATOR
	(
		CraftDislikeGenerator.class,
		CraftDislikeGenerator::new,
		CraftDislikeGenerator::new,
		DislikeGeneratorWriter.class,
		DislikeGeneratorReader.class,
		CraftDislikeGeneratorGui::new,
		Main::getDislikeGeneratorRule,
		NamedTextColor.RED,
		PluginMessage.STRUCTURE_DISLIKE_GENERATOR_NAME,
		PluginMessage.STRUCTURE_DISLIKE_GENERATOR_UPGRADE_PREVIEW_LORE
	),
	
	DISLIKE_DEPOSIT
	(
		CraftDislikeDeposit.class,
		CraftDislikeDeposit::new,
		CraftDislikeDeposit::new,
		DislikeDepositWriter.class,
		DislikeDepositReader.class,
		CraftDislikeDepositGui::new,
		Main::getDislikeDepositRule,
		NamedTextColor.RED,
		PluginMessage.STRUCTURE_DISLIKE_DEPOSIT_NAME,
		PluginMessage.STRUCTURE_DISLIKE_DEPOSIT_UPGRADE_PREVIEW_LORE
	),
	
	LIKE_DEPOSIT
	(
		CraftLikeDeposit.class,
		CraftLikeDeposit::new,
		CraftLikeDeposit::new,
		LikeDepositWriter.class,
		LikeDepositReader.class,
		CraftLikeDepositGui::new,
		Main::getLikeDepositRule,
		NamedTextColor.RED,
		PluginMessage.STRUCTURE_LIKE_DEPOSIT_NAME,
		PluginMessage.STRUCTURE_LIKE_DEPOSIT_UPGRADE_PREVIEW_LORE
	),
	
	ARMORY
	(
		CraftArmory.class,
		CraftArmory::new,
		CraftArmory::new,
		ArmoryWriter.class,
		ArmoryReader.class,
		CraftArmoryGui::new,
		Main::getArmoryRule,
		NamedTextColor.GOLD,
		PluginMessage.STRUCTURE_ARMORY_NAME,
		PluginMessage.STRUCTURE_ARMORY_UPGRADE_PREVIEW_LORE
	);
	
	private final Class<? extends Structure> structureClass;
	private final StructureInstantiation instantiation;
	private final Function<Town, ? extends Structure> newStructureTown;
	private final Class<? extends StructureWriter<? extends Structure>> writerClass;
	private final Class<? extends StructureReader<? extends Structure>> readerClass;
	private final BiFunction<CommonPlayer, Structure, StructureGui> newStructureGui;
	private final Function<Main, Rule<? extends GenericAttribute>> ruleFunction;
	private final TextColor color;
	private final Message nameMessage;
	private final Message upgradePreviewLoreMessage;
	
	public Rule<?> getRule(Main plugin)
	{
		return ruleFunction.apply(plugin);
	}
}
