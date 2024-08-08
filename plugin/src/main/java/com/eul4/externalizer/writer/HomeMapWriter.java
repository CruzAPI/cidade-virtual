package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.LocationWriter;
import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.wrapper.HomeMap;
import org.bukkit.Location;

import java.io.IOException;
import java.util.Map;

public class HomeMapWriter extends ObjectWriter<HomeMap>
{
	public HomeMapWriter(Writers writers)
	{
		super(writers, HomeMap.class);
	}
	
	@Override
	protected void writeObject(HomeMap homeMap) throws IOException
	{
		final int size = homeMap.size();
		
		out.writeInt(size);
		
		for(Map.Entry<String, Location> entry : homeMap.entrySet())
		{
			final String homeName = entry.getKey();
			final Location homeLocation = entry.getValue();
			
			out.writeUTF(homeName);
			writers.getWriter(LocationWriter.class).writeReferenceNotNull(homeLocation);
		}
	}
}
