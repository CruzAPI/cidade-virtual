package com.eul4.model.inventory.craft;

import com.eul4.Price;
import com.eul4.common.model.inventory.UpdatableGui;
import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.StructureStatus;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.StructureGui;
import com.eul4.model.town.structure.Structure;
import com.eul4.rule.attribute.GenericAttribute;
import com.eul4.util.MessageUtil;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Optional;

import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

@Getter
public abstract class CraftStructureGui extends CraftGui implements StructureGui, UpdatableGui
{
	protected final Structure structure;
	
	protected final ItemStack upgrade;
	protected final ItemStack move;
	protected final ItemStack finishBuild;
	
	private BukkitRunnable updateTask;
	
	public CraftStructureGui(CommonPlayer commonPlayer, Structure structure, Inventory inventory)
	{
		super(commonPlayer, inventory);
		
		this.structure = structure;
		
		ItemMeta meta;
		
		upgrade = new ItemStack(Material.EXPERIENCE_BOTTLE);
		
		
		move = new ItemStack(Material.TARGET);
		meta = move.getItemMeta();
		meta.setDisplayName("Move Structure");
		move.setItemMeta(meta);
		
		finishBuild = new ItemStack(Material.LIME_CONCRETE);
		meta = finishBuild.getItemMeta();
		meta.setDisplayName("Finish Build");
		finishBuild.setItemMeta(meta);
	}
	
	@Override
	public void updateInventory()
	{
		ItemMeta meta;
		
		updateItemStacks();
		
		inventory.setItem(0, upgrade);
		inventory.setItem(1, move);
		
		if(structure.getStatus() != StructureStatus.BUILT)
		{
			close();
		}
	}
	
	private void updateItemStacks()
	{
		updateUpgradeItem();
	}
	
	private void updateUpgradeItem()
	{
		GenericAttribute currentAttribute = structure.getRule().getAttribute(structure.getLevel());
		GenericAttribute nextAttribute = structure.getRule().getAttribute(structure.getLevel() + 1);
		Price price = nextAttribute == null ? null : nextAttribute.getPrice();
		
		ItemMeta meta = upgrade.getItemMeta();
		
		if(currentAttribute == null || nextAttribute == null || price == null)
		{
			meta.displayName(PluginMessage.STRUCTURE_MAX_UPGRADE_REACHED.translate(commonPlayer));
		}
		else
		{
			if(structure.hasUpgradeUnlocked())
			{
				meta.displayName(PluginMessage.UPGRADE.translateWord(commonPlayer, WordUtils::capitalize)
						.color(GREEN)
						.decorate(BOLD));
			}
			else
			{
				meta.displayName(PluginMessage.UPGRADE_LOCKED
						.translate(commonPlayer, nextAttribute.getRequiresTownHallLevel()));
			}
		
			List<Component> lore = MessageUtil.getPriceLore(price, commonPlayer);
			
			lore.add(Component.empty());
			
			lore.addAll(structure.getStructureType().getUpgradePreviewLoreMessage()
					.translateLore(commonPlayer, currentAttribute, nextAttribute));
			
			meta.lore(lore);
		}
		
		upgrade.setItemMeta(meta);
	}
	
	@Override
	public void scheduleUpdate()
	{
		if(getPlugin().isQueued(updateTask))
		{
			return;
		}
		
		updateTask = new BukkitRunnable()
		{
			@Override
			public void run()
			{
				updateInventory();
			}
		};
		
		updateTask.runTaskTimer(getPlugin(), 0L, 1L);
	}
	
	@Override
	public void cancelScheduleUpdate()
	{
		Optional.ofNullable(updateTask).ifPresent(BukkitRunnable::cancel);
	}
	
	@Override
	public void updateTitle()
	{
		if(commonPlayer.getGui() != this
				|| commonPlayer.getPlayer().getOpenInventory().getTopInventory() != inventory)
		{
			return;
		}
		
		Component updatedTitleComponent = getUpdatedTitleComponent();
		
		String legacyText = LegacyComponentSerializer.legacySection().serialize(updatedTitleComponent);
		
		commonPlayer.getPlayer().getOpenInventory().setTitle(legacyText);
	}
}
