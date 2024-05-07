package com.eul4.externalizer.writer;

import com.eul4.model.player.RaidAnalyzer;

import java.io.IOException;
import java.io.ObjectOutput;

public class RaidAnalyzerWriter extends PluginPlayerWriter<RaidAnalyzer>
{
	public RaidAnalyzerWriter(ObjectOutput out)
	{
		super(out);
	}
	
	@Override
	protected void writeObject(RaidAnalyzer raidAnalyzer) throws IOException
	{
		super.writeObject(raidAnalyzer);
	}
}
