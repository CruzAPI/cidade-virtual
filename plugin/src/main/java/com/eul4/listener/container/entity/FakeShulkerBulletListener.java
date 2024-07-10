package com.eul4.listener.container.entity;

import com.eul4.Main;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static com.eul4.enums.PluginNamespacedKey.FAKE_SHULKER_BULLET;
import static org.bukkit.persistence.PersistentDataType.BOOLEAN;

@RequiredArgsConstructor
public class FakeShulkerBulletListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFakeShulkerBulletDamage(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof ShulkerBullet shulkerBullet))
		{
			return;
		}
		
		var container = shulkerBullet.getPersistentDataContainer();
		
		boolean isFakeShulkerBullet = container.getOrDefault(FAKE_SHULKER_BULLET, BOOLEAN, false);
		
		if(isFakeShulkerBullet)
		{
			event.setCancelled(true);
		}
	}
}
