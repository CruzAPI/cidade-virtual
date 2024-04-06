package com.eul4.service;

import com.eul4.Main;
import com.eul4.Price;
import com.eul4.StructurePriceChart;
import com.eul4.StructureType;
import com.google.common.io.ByteStreams;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class StructurePriceSerializer
{
	private final Main plugin;
	
	public void saveStructurePriceChart(StructurePriceChart structurePriceChart) throws IOException
	{
		File file = plugin.getDataFileManager().createStructurePricesFileIfNotExists();
		
		try(FileOutputStream fileOutputStream = new FileOutputStream(file);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream))
		{
			structurePriceChart.writeExternal(out);
			out.flush();
			fileOutputStream.write(byteArrayOutputStream.toByteArray());
		}
	}
	
	private void writeStructurePriceChart(Map<StructureType, Price> priceChart, ObjectOutput out) throws IOException
	{
		out.writeInt(priceChart.size());
		
		for(Map.Entry<StructureType, Price> entry : priceChart.entrySet())
		{
			out.writeUTF(entry.getKey().name());
			entry.getValue().writeExternal(out);
		}
	}
	
	public StructurePriceChart loadStructurePriceChart() throws IOException, ClassNotFoundException
	{
		File file = plugin.getDataFileManager().getStructurePricesFile();
		
		if(!file.exists() || file.length() == 0L)
		{
			plugin.getLogger().warning("Structure prices file not found.");
			plugin.getLogger().warning("Loading empty structure prices instead.");
			return new StructurePriceChart();
		}
		
		try(FileInputStream fileInputStream = new FileInputStream(file);
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ByteStreams.toByteArray(fileInputStream));
				ObjectInputStream in = new ObjectInputStream(byteArrayInputStream))
		{
			StructurePriceChart structurePriceChart = new StructurePriceChart();
			structurePriceChart.readExternal(in);
			return structurePriceChart;
		}
	}
}
