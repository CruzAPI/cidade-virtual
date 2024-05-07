package com.eul4;

import com.eul4.externalizer.writer.PluginPlayerWriter;
import com.eul4.model.player.PluginPlayer;

import java.io.ObjectOutput;

@FunctionalInterface
public interface PluginWriterConstructor
{
	PluginPlayerWriter<? extends PluginPlayer> newInstance(ObjectOutput out);
}
