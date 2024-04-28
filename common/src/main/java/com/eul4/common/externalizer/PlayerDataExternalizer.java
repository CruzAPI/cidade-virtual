package com.eul4.common.externalizer;

import com.eul4.common.Common;
import com.eul4.common.model.data.PlayerData;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@RequiredArgsConstructor
public class PlayerDataExternalizer
{
	private final Common plugin;
	
	public static final long VERSION = 0L;
	
	public PlayerData read(long version, ObjectInput in) throws IOException, ClassNotFoundException
	{
		if(version == 0L)
		{
			Location location = plugin.getLocationExternalizer().read(in);
			ItemStack[] contents = plugin.getInventoryExternalizer().read(in);
			
			return PlayerData.builder()
					.location(location)
					.contents(contents)
					.build();
		}
		
		throw new RuntimeException();
	}
	
	public PlayerData read(ObjectInput in) throws IOException, ClassNotFoundException
	{
		return read(in.readLong(), in);
	}
	
	public void write(PlayerData playerData, ObjectOutput out) throws IOException
	{
		out.writeLong(VERSION);
		
		plugin.getLocationExternalizer().write(playerData.getLocation(), out);
		plugin.getInventoryExternalizer().write(playerData.getContents(), out);
	}
}
