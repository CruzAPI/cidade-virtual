package com.eul4.model.craft.player;

import com.eul4.Main;
import com.eul4.model.player.spiritual.InventoryOrganizerPlayer;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.SpiritualPlayerType;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.function.BiConsumer;

@Getter
public class CraftInventoryOrganizerPlayer extends CraftSpiritualPlayer implements InventoryOrganizerPlayer
{
	private final ItemStack[] contents;
	private final BiConsumer<InventoryOrganizerPlayer, PlayerInventory> onCloseAction;
	
	public CraftInventoryOrganizerPlayer(Player player, Main plugin)
	{
		super(player, plugin);
		this.contents = null;
		this.onCloseAction = (x, y) -> {};
	}
	
	public CraftInventoryOrganizerPlayer(Player player, PluginPlayer pluginPlayer)
	{
		super(player, pluginPlayer);
		this.contents = null;
		this.onCloseAction = (x, y) -> {};
	}
	
	public CraftInventoryOrganizerPlayer(PluginPlayer pluginPlayer, ItemStack[] contents)
	{
		this(pluginPlayer, contents, (x, y) -> {});
	}
	
	public CraftInventoryOrganizerPlayer(PluginPlayer pluginPlayer, ItemStack[] contents, BiConsumer<InventoryOrganizerPlayer, PlayerInventory> onCloseAction)
	{
		super(pluginPlayer.getPlayer(), pluginPlayer);
		this.contents = contents;
		this.onCloseAction = onCloseAction;
	}
	
	@Override
	public SpiritualPlayerType getPlayerType()
	{
		return SpiritualPlayerType.INVENTORY_ORGANIZER_PLAYER;
	}
	
	@Override
	public void reset()
	{
		super.reset();
		player.getInventory().setContents(contents);
	}
	
	@Override
	public PluginPlayer reload()
	{
		if(contents == null)
		{
			return reincarnate();
		}
		
		reset();
		return this;
	}
	
	@Override
	public PluginPlayer load()
	{
		return super.load();
	}
	
	@Override
	public void onCloseInventory()
	{
		this.onCloseAction.accept(this, player.getInventory());
	}
	
	@Override
	public void onSneak()
	{
	
	}
}
