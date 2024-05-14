package com.eul4.externalizer.writer;

import com.eul4.common.type.player.Writers;
import com.eul4.model.player.RaidAnalyzer;

import java.io.IOException;

public class RaidAnalyzerWriter extends PluginPlayerWriter<RaidAnalyzer>
{
	public RaidAnalyzerWriter(Writers writers)
	{
		super(writers, RaidAnalyzer.class);
	}
	
	@Override
	protected void writeObject(RaidAnalyzer raidAnalyzer) throws IOException
	{
		super.writeObject(raidAnalyzer);
	}
}
