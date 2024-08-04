package com.eul4.listener;

import com.eul4.Main;
import com.eul4.common.util.EntityUtil;
import com.eul4.event.AttackFinishEvent;
import com.eul4.event.AttackStartEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class AssistantHideListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onAttackStart(AttackStartEvent event)
	{
		EntityUtil.hideNullable(plugin, event.getTown().getAssistant());
	}
	
	@EventHandler
	public void onAttackFinish(AttackFinishEvent event)
	{
		EntityUtil.showNullable(plugin, event.getTown().getAssistant());
	}
}
