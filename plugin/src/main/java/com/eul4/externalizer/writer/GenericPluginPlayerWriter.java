package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.PluginPlayerType;

import java.io.IOException;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GenericPluginPlayerWriter extends ObjectWriter<PluginPlayer>
{
	private final Map<Class<? extends PluginPlayer>, PluginPlayerWriter<?>> writers;
	
	public GenericPluginPlayerWriter(ObjectOutput out)
	{
		super(out);
		
		Map<Class<? extends PluginPlayer>, PluginPlayerWriter<?>> tmpMap = new HashMap<>();
		
		for(PluginPlayerType type : PluginPlayerType.values())
		{
			tmpMap.put(type.getType(), type.getPluginWriterConstructor().newInstance(out));
		}
		
		this.writers = Collections.unmodifiableMap(tmpMap);
	}
	
	@Override
	protected void writeObject(PluginPlayer pluginPlayer) throws IOException
	{
		out.writeInt(pluginPlayer.getPlayerType().ordinal());
		getWriterAndWriteReference(pluginPlayer, pluginPlayer.getPlayerType().getType());
	}
	
	@SuppressWarnings("unchecked")
	private <P extends PluginPlayer> void getWriterAndWriteReference(PluginPlayer pluginPlayer, Class<P> type) throws IOException
	{
		((PluginPlayerWriter<P>) writers.get(type)).writeReference(type.cast(pluginPlayer));
	}
}
