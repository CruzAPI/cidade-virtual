package com.eul4.listener;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.eul4.Main;
import com.eul4.common.hologram.Hologram;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Armory;
import com.eul4.model.town.structure.Turret;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class EntityRecyclerListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void onTranslatedHologramLine(EntityAddToWorldEvent event)
	{
		Entity entity = event.getEntity();
		
		if(!(entity instanceof ArmorStand armorStand))
		{
			return;
		}
		
		Hologram.TranslatedHologramLine.ARMOR_STAND_UUID_MAP
				.computeIfPresent(armorStand.getUniqueId(), (uuid, line) ->
				{
					line.setArmorStand(armorStand);
					return line;
				});
	}
	
	@EventHandler
	public void onArmory(EntityAddToWorldEvent event)
	{
		Entity entity = event.getEntity();
		
		if(!(entity instanceof Villager npc))
		{
			return;
		}
		
		Armory.NPC_UUID_MAP
				.computeIfPresent(npc.getUniqueId(), (uuid, armory) ->
				{
					armory.setNPC(npc);
					return armory;
				});
	}
	
	@EventHandler
	public void onAssistant(EntityAddToWorldEvent event)
	{
		Entity entity = event.getEntity();
		
		if(!(entity instanceof Villager assistant))
		{
			return;
		}
		
		Town.ASSISTANT_UUID_MAP
				.computeIfPresent(assistant.getUniqueId(), (uuid, town) ->
				{
					town.setAssistant(assistant);
					return town;
				});
	}
	
	@EventHandler
	public void onTurretEvoker(EntityAddToWorldEvent event)
	{
		Entity entity = event.getEntity();
		
		if(!(entity instanceof Evoker evoker))
		{
			return;
		}
		
		Turret.EVOKER_UUID_MAP
				.computeIfPresent(evoker.getUniqueId(), (uuid, turret) ->
				{
					turret.setEvoker(evoker);
					return turret;
				});
	}
}
