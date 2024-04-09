package com.eul4.service;

import com.eul4.Main;
import com.eul4.StructureType;
import com.eul4.common.wrapper.BlockSerializable;
import com.eul4.model.craft.town.CraftTown;
import com.eul4.model.craft.town.CraftTownBlock;
import com.eul4.model.craft.town.CraftTownTile;
import com.eul4.model.town.Town;
import com.eul4.model.town.TownBlock;
import com.eul4.model.town.TownTile;
import com.eul4.model.town.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;

@RequiredArgsConstructor
public class TownSerializer
{
	private final Main plugin;
	
	public Map<UUID, Town> readTowns(ObjectInput in) throws IOException, ClassNotFoundException
	{
		Map<UUID, Town> towns = new HashMap<>();
		
		int size = in.readInt();
		
		for(int i = 0; i < size; i++)
		{
			CraftTown craftTown = new CraftTown(plugin);
			
			UUID uuid = (UUID) in.readObject();
			craftTown.readExternal(in);
			
			towns.put(uuid, craftTown);
		}
		
		return towns;
	}
	
	public void writeTowns(ObjectOutput out) throws IOException
	{
		Map<UUID, Town> towns = plugin.getTownManager().getTowns();
		
		out.writeInt(towns.size());
		
		for(Map.Entry<UUID, Town> entry : towns.entrySet())
		{
			out.writeObject(entry.getKey());
			entry.getValue().writeExternal(out);
		}
	}
	
	public Map<Block, TownTile> readTownTiles(Town town, ObjectInput in) throws IOException, ClassNotFoundException
	{
		int size = in.readInt();
		
		Map<Block, TownTile> deserializedTownTiles = new HashMap<>();
		
		for(int i = 0; i < size; i++)
		{
			CraftTownTile craftTownTile = new CraftTownTile(town);
			
			Block block = ((BlockSerializable) in.readObject()).getBukkitBlock(plugin.getServer());
			craftTownTile.readExternal(in);
			
			deserializedTownTiles.put(block, craftTownTile);
		}
		
		return deserializedTownTiles;
	}
	
	public void writeTownTiles(Town town, ObjectOutput out) throws IOException
	{
		Map<Block, TownTile> deserializedTownTiles = town.getTownTiles();
		
		out.writeInt(deserializedTownTiles.size());
		
		for(Map.Entry<Block, TownTile> entry : deserializedTownTiles.entrySet())
		{
			out.writeObject(new BlockSerializable(entry.getKey()));
			entry.getValue().writeExternal(out);
		}
	}
	
	public Map<UUID, Structure> readStructures(Town town, ObjectInput in) throws IOException, ClassNotFoundException
	{
		int size = in.readInt();
		
		Map<UUID, Structure> structures = new HashMap<>();
		
		for(int i = 0; i < size; i++)
		{
			structures.put((UUID) in.readObject(), readStructure(town, in));
		}
		
		return structures;
	}
	
	public Structure readStructureReference(Town town, ObjectInput in) throws IOException, ClassNotFoundException
	{
		UUID uuid = (UUID) in.readObject();
		return town.getStructures().get(uuid);
	}
	
	public void writeStructureReference(Structure structure, ObjectOutput out) throws IOException
	{
		out.writeObject(structure == null ? null : structure.getUUID());
	}
	
	private Structure readStructure(Town town, ObjectInput in) throws IOException, ClassNotFoundException
	{
		StructureType<?, ?> structureType = StructureType.values().get(in.readInt());
		Structure structure = structureType.getNewStructureTown().apply(town);
		
		structure.readExternal(in);
		
		return structure;
	}
	
	public void writeStructures(Map<UUID, Structure> structures, ObjectOutput out) throws IOException
	{
		out.writeInt(structures.size());
		
		for(Map.Entry<UUID, Structure> entry : structures.entrySet())
		{
			out.writeObject(entry.getKey());
			writeStructure(entry.getValue(), out);
		}
	}
	
	private void writeStructure(Structure structure, ObjectOutput out) throws IOException
	{
		out.writeInt(structure.getStructureType().ordinal());
		structure.writeExternal(out);
	}
	
	public Map<Block, TownBlock> readTownBlocks(Town town, ObjectInput in) throws IOException, ClassNotFoundException
	{
		int size = in.readInt();
		
		Map<Block, TownBlock> deserializedTownBlocks = new HashMap<>();
		
		for(int i = 0; i < size; i++)
		{
			CraftTownBlock craftTownBlock = new CraftTownBlock(town);
			Block block = ((BlockSerializable) in.readObject()).getBukkitBlock(plugin.getServer());
			craftTownBlock.readExternal(in);
			
			deserializedTownBlocks.put(block, craftTownBlock);
		}
		
		return deserializedTownBlocks;
	}
	
	public void writeTownBlocks(Map<Block, TownBlock> townBlocks, ObjectOutput out) throws IOException
	{
		out.writeInt(townBlocks.size());
		
		for(Map.Entry<Block, TownBlock> entry : townBlocks.entrySet())
		{
			out.writeObject(new BlockSerializable(entry.getKey()));
			entry.getValue().writeExternal(out);
		}
	}
	
	public void writeStructureTownBlocks(Set<TownBlock> townBlocks, ObjectOutput out) throws IOException
	{
		out.writeInt(townBlocks.size());
		
		for(TownBlock townBlock : townBlocks)
		{
			out.writeObject(new BlockSerializable(townBlock.getBlock()));
		}
	}
	
	public Set<TownBlock> readStructureTownBlocks(Structure structure, ObjectInput in)
			throws IOException, ClassNotFoundException
	{
		Set<TownBlock> townBlocks = new HashSet<>();
		
		int size = in.readInt();
		
		for(int i = 0; i < size; i++)
		{
			Block block = ((BlockSerializable) in.readObject()).getBukkitBlock(structure.getTown().getPlugin().getServer());
			TownBlock townBlock = structure.getTown().getTownBlock(block);
			townBlock.setStructure(structure);
			townBlocks.add(townBlock);
		}
		
		return townBlocks;
	}
}
