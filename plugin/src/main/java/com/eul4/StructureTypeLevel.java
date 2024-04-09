package com.eul4;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StructureTypeLevel implements Externalizable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	private StructureType<?, ?> structureType;
	private int level;
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeLong(serialVersionUID);
		
		out.writeInt(structureType.ordinal());
		out.writeInt(level);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		final long version = in.readLong();
		
		if(version == 1L)
		{
			structureType = StructureType.values().get(in.readInt());
			level = in.readInt();
		}
		else
		{
			throw new RuntimeException();
		}
	}
}
