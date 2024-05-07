package com.eul4;

import com.eul4.common.i18n.Message;
import com.eul4.common.model.player.CommonPlayer;
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

@Getter
@RequiredArgsConstructor
public enum StructureType
{
	TOWN_HALL(
			CraftTownHall::new,
			CraftTownHall::new,
			CraftTownHallGui::new,
			Main::getTownHallRule,
			NamedTextColor.YELLOW,
			PluginMessage.STRUCTURE_TOWN_HALL_NAME,
			PluginMessage.STRUCTURE_TOWN_HALL_UPGRADE_PREVIEW_LORE
	),
	
	LIKE_GENERATOR(
			CraftLikeGenerator::new,
			CraftLikeGenerator::new,
			CraftLikeGeneratorGui::new,
			Main::getLikeGeneratorRule,
			NamedTextColor.GREEN,
			PluginMessage.STRUCTURE_LIKE_GENERATOR_NAME,
			PluginMessage.STRUCTURE_LIKE_GENERATOR_UPGRADE_PREVIEW_LORE
	),
	
	DISLIKE_GENERATOR(
			CraftDislikeGenerator::new,
			CraftDislikeGenerator::new,
			CraftDislikeGeneratorGui::new,
			Main::getDislikeGeneratorRule,
			NamedTextColor.RED,
			PluginMessage.STRUCTURE_DISLIKE_GENERATOR_NAME,
			PluginMessage.STRUCTURE_DISLIKE_GENERATOR_UPGRADE_PREVIEW_LORE
	),
	
	DISLIKE_DEPOSIT(
			CraftDislikeDeposit::new,
			CraftDislikeDeposit::new,
			CraftDislikeDepositGui::new,
			Main::getDislikeDepositRule,
			NamedTextColor.RED,
			PluginMessage.STRUCTURE_DISLIKE_DEPOSIT_NAME,
			PluginMessage.STRUCTURE_DISLIKE_DEPOSIT_UPGRADE_PREVIEW_LORE
	),
	
	LIKE_DEPOSIT(
			CraftLikeDeposit::new,
			CraftLikeDeposit::new,
			CraftLikeDepositGui::new,
			Main::getLikeDepositRule,
			NamedTextColor.RED,
			PluginMessage.STRUCTURE_LIKE_DEPOSIT_NAME,
			PluginMessage.STRUCTURE_LIKE_DEPOSIT_UPGRADE_PREVIEW_LORE
	);
	
	private final StructureInstantiation instantiation;
	private final Function<Town, ? extends Structure> newStructureTown;
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
