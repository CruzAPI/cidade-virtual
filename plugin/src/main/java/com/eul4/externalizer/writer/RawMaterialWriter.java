package com.eul4.externalizer.writer;

import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.Writers;
import com.eul4.wrapper.RawMaterial;
import com.sk89q.worldedit.math.Vector3;
import org.bukkit.Material;

import java.io.IOException;

public class RawMaterialWriter extends ObjectWriter<RawMaterial>
{
	public RawMaterialWriter(Writers writers)
	{
		super(writers, RawMaterial.class);
	}
	
	@Override
	protected void writeObject(RawMaterial rawMaterial) throws IOException
	{
		CryptoInfoWriter cryptoInfoWriter = writers.getWriter(CryptoInfoWriter.class);
		
		Material material = rawMaterial.getMaterial();
		
		out.writeUTF(material.name());
		cryptoInfoWriter.writeReference(rawMaterial.getCryptoInfo());
	}
}
