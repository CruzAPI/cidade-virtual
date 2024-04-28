package com.eul4.common.externalizer;

import com.eul4.common.Common;
import com.eul4.common.model.data.CommonPlayerData;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@RequiredArgsConstructor
public class CommonPlayerDataExternalizer
{
	private final Common plugin;
	
	public static final long VERSION = 0L;
	
	public CommonPlayerData read(long version, ObjectInput in) throws IOException, ClassNotFoundException
	{
		if(version == 0L)
		{
			return CommonPlayerData.builder()
					.playerData(plugin.getPlayerDataExternalizer().read(in))
					.build();
		}
		
		throw new RuntimeException();
	}
	
	public CommonPlayerData read(ObjectInput in) throws IOException, ClassNotFoundException
	{
		return read(in.readLong(), in);
	}
	
	public void write(CommonPlayerData commonPlayerData, ObjectOutput out) throws IOException
	{
		out.writeLong(VERSION);
		
		plugin.getPlayerDataExternalizer().write(commonPlayerData.getPlayerData(), out);
	}
}
