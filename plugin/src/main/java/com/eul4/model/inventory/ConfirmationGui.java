package com.eul4.model.inventory;

import com.eul4.common.model.inventory.Gui;
import org.bukkit.inventory.ItemStack;

public interface ConfirmationGui extends Gui
{
	ItemStack getConfirm();
	ItemStack getCancel();
	
	void confirm();
	
	void cancel();
}
