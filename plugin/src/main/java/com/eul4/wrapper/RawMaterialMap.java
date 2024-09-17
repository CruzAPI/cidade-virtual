package com.eul4.wrapper;

import lombok.Getter;
import org.bukkit.Material;

import java.util.HashMap;

@Getter
public class RawMaterialMap
{
	private final HashMap<Material, RawMaterial> map;
	
	public RawMaterialMap()
	{
		this(createDefaultMap());
	}
	
	private RawMaterialMap(HashMap<Material, RawMaterial> map)
	{
		this.map = map;
	}
	
	private static HashMap<Material, RawMaterial> createDefaultMap()
	{
		HashMap<Material, RawMaterial> rawMaterialMap = new HashMap<>();
		
		for(Material material : Material.values())
		{
			rawMaterialMap.put(material, new RawMaterial(material, new CryptoInfo()));
		}
		
		return rawMaterialMap;
	}
	
	public boolean containsKey(Material material)
	{
		return map.containsKey(material);
	}
	
	public RawMaterial get(Material material)
	{
		return map.get(material);
	}
	
	public void put(RawMaterial rawMaterial)
	{
		map.put(rawMaterial.getMaterial(), rawMaterial);
	}
}
