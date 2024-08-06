package com.eul4.listener;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.eul4.Main;
import com.eul4.common.constant.CommonNamespacedKey;
import com.eul4.common.hologram.Hologram;
import com.eul4.common.util.ContainerUtil;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Armory;
import com.eul4.model.town.structure.Turret;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.text.MessageFormat;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class EntityRecyclerListener implements Listener
{
	private final Main plugin;
	
	private static final Map<UUID, ?>[] UUID_MAP = new Map[]
	{
		Hologram.TranslatedHologramLine.ARMOR_STAND_UUID_MAP,
		Town.ASSISTANT_UUID_MAP,
		Turret.EVOKER_UUID_MAP,
		Armory.NPC_UUID_MAP,
	};
	
	@EventHandler
	public void onTranslatedHologramLine(EntityAddToWorldEvent event)
	{
		Entity entity = event.getEntity();
		
		if(!(entity instanceof ArmorStand armorStand))
		{
			return;
		}
		
		ContainerUtil.hasFlag(armorStand.getPersistentDataContainer(), CommonNamespacedKey.FAWE_IGNORE);
		
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
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAddToWorldRemoveGhostEntity(EntityAddToWorldEvent event)
	{
		plugin.getServer()
				.getScheduler()
				.runTask(plugin, () -> removeEntityIfGhosted(event.getEntity()));
	}
	
	private void removeEntityIfGhosted(Entity entity)
	{
		if(isGhostEntity(entity))
		{
			entity.remove();
			plugin.getLogger().warning(MessageFormat.format(
					"Ghost entity removed! "
					+ "type={0} "
					+ "uuid={1} "
					+ "loc={2} ",
					entity.getType(),
					entity.getUniqueId(),
					entity.getLocation()));
		}
	}
	
	private boolean isGhostEntity(Entity entity)
	{
		if(!ContainerUtil.hasFlag(entity.getPersistentDataContainer(), CommonNamespacedKey.FAWE_IGNORE)
				|| !plugin.getTownManager().isLoaded())
		{
			return false;
		}
		
		for(Map<UUID, ?> map : UUID_MAP)
		{
			if(map.containsKey(entity.getUniqueId()))
			{
				return false;
			}
		}
		
		return true;
	}
}
