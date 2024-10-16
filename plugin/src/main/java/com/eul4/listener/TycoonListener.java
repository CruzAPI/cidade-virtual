package com.eul4.listener;

import com.eul4.Main;
import com.eul4.economy.Transaction;
import com.eul4.event.TransactionExecuteEvent;
import com.eul4.holder.Holder;
import com.eul4.holder.TownOwner;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class TycoonListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void updateCrownBalance(TransactionExecuteEvent event)
	{
		Transaction<?> transaction = event.getTransaction();
		
		for(Holder<?> involvedHolder : transaction.getInvolvedHolders())
		{
			if(involvedHolder instanceof TownOwner)
			{
				plugin.getTycoonManager().updateTycoonAsync();
				return;
			}
		}
	}
}
