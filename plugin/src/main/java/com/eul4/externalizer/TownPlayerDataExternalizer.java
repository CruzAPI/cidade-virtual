package com.eul4.externalizer;

import com.eul4.Main;
import com.eul4.model.playerdata.TownPlayerData;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@RequiredArgsConstructor
public class TownPlayerDataExternalizer
{
	private final Main plugin;
	
	private static final long VERSION = 0L;
	
	public TownPlayerData read(ObjectInput in) throws IOException, ClassNotFoundException
	{
		return read(in.readLong(), in);
	}
	
	public TownPlayerData read(long version, ObjectInput in) throws IOException, ClassNotFoundException
	{
		if(version == 0L)
		{
			return TownPlayerData.builder()
					.test(in.readBoolean())
					.build();
		}
		
		throw new RuntimeException();
	}
	
	public void write(TownPlayerData townPlayerData, ObjectOutput out) throws IOException
	{
		out.writeLong(VERSION);
		
		out.writeBoolean(townPlayerData.isTest());
	}
}
