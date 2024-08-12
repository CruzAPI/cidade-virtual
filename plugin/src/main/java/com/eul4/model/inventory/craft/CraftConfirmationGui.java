package com.eul4.model.inventory.craft;

import com.eul4.common.model.inventory.craft.CraftGui;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.inventory.ConfirmationGui;
import lombok.Getter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.eul4.common.i18n.CommonMessage.CANCEL;
import static com.eul4.common.i18n.CommonMessage.CONFIRM;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@Getter
public class CraftConfirmationGui extends CraftGui implements ConfirmationGui
{
	private final Runnable confirmRunnable;
	private final Runnable cancelRunnable;
	
	private ItemStack confirm;
	private final ItemStack cancel;
	
	public CraftConfirmationGui(CommonPlayer commonPlayer)
	{
		this(commonPlayer, () -> {});
	}
	
	public CraftConfirmationGui(CommonPlayer commonPlayer, Runnable confirmRunnable)
	{
		this(commonPlayer, confirmRunnable, () -> {});
	}
	
	public CraftConfirmationGui(CommonPlayer commonPlayer, Runnable confirmRunnable, Runnable cancelRunnable)
	{
		super(commonPlayer, commonPlayer.createInventory(InventoryType.HOPPER, PluginMessage.CONFIRM_OPERATION));
		
		this.confirmRunnable = confirmRunnable;
		this.cancelRunnable = cancelRunnable;
		
		ItemMeta meta;
		
		confirm = getConfirmIcon();
		
		cancel = new ItemStack(Material.RED_CONCRETE);
		meta = cancel.getItemMeta();
		meta.displayName(CANCEL.translateOne(commonPlayer, WordUtils::capitalize).color(RED));
		cancel.setItemMeta(meta);
		
		inventory.setItem(1, confirm);
		inventory.setItem(3, cancel);
	}
	
	@Override
	public void updateTitle()
	{
	
	}
	
	public final ItemStack getConfirmIcon()
	{
		if(confirm != null)
		{
			return confirm.clone();
		}
		else
		{
			return confirm = getConfirm();
		}
	}
	
	public ItemStack getConfirm()
	{
		ItemMeta meta;
		
		confirm = ItemStack.of(Material.LIME_CONCRETE);
		meta = confirm.getItemMeta();
		meta.displayName(CONFIRM.translateOne(commonPlayer, WordUtils::capitalize).color(GREEN));
		confirm.setItemMeta(meta);
		
		return confirm;
	}
	
	@Override
	public void confirm()
	{
		confirmRunnable.run();
	}
	
	@Override
	public void cancel()
	{
		cancelRunnable.run();
	}
}
