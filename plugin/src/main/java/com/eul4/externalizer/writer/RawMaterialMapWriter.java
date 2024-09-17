package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.wrapper.RawMaterial;
import com.eul4.wrapper.RawMaterialMap;
import org.bukkit.Material;

import java.io.IOException;
import java.util.HashMap;

public class RawMaterialMapWriter extends ObjectWriter<RawMaterialMap>
{
	public RawMaterialMapWriter(Writers writers)
	{
		super(writers, RawMaterialMap.class);
	}
	
	@Override
	protected void writeObject(RawMaterialMap rawMaterialMap) throws IOException
	{
		RawMaterialWriter rawMaterialWriter = writers.getWriter(RawMaterialWriter.class);
		
		HashMap<Material, RawMaterial> map = rawMaterialMap.getMap();
		
		out.writeInt(map.size());
		
		for(RawMaterial rawMaterial : map.values())
		{
			rawMaterialWriter.writeReference(rawMaterial);
		}
	}
}
