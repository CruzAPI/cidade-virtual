package com.eul4.externalizer.reader;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.BlockReader;
import com.eul4.common.externalizer.reader.EntityReader;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.externalizer.reader.UUIDReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.ParameterizedReadable;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.model.craft.town.CraftTown;
import com.eul4.model.town.Town;
import com.eul4.type.player.PluginObjectType;
import lombok.Getter;
import org.bukkit.entity.Villager;

import java.io.IOException;
import java.util.UUID;

@Getter
public class TownReader extends ObjectReader<Town>
{
	private final Reader<Town> reader;
	private final ParameterizedReadable<Town, Main> parameterizedReadable;
	
	public TownReader(Readers readers) throws InvalidVersionException
	{
		super(readers, Town.class);
		
		final ObjectType objectType = PluginObjectType.TOWN;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		case 1:
			this.reader = this::readerVersion1;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		case 2:
			this.reader = this::readerVersion2;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		case 3:
			this.reader = this::readerVersion3;
			this.parameterizedReadable = this::parameterizedReadableVersion0;
			break;
		case 4:
			this.reader = this::readerVersion3;
			this.parameterizedReadable = this::parameterizedReadableVersion1;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private Readable<Town> parameterizedReadableVersion0(Main plugin)
	{
		return () -> new CraftTown(new UUID(in.readLong(), in.readLong()),
				readers.getReader(BlockReader.class).readReference(plugin),
				plugin);
	}
	
	private Readable<Town> parameterizedReadableVersion1(Main plugin)
	{
		UUIDReader uuidReader = readers.getReader(UUIDReader.class);
		
		return () -> new CraftTown(uuidReader.readReference(),
				uuidReader.readReference(),
				readers.getReader(BlockReader.class).readReference(plugin),
				plugin);
	}
	
	private void readerVersion0(Town town) throws IOException, ClassNotFoundException
	{
		town.setTownBlockMap(readers.getReader(TownBlockMapReader.class).readReference(town));
		town.setTownTileMap(readers.getReader(TownTileMapReader.class).readReference(town));
		town.setStructureMap(readers.getReader(StructureMapReader.class).readReference(town));
		town.setMovingStructure(readers.getReader(GenericStructureReader.class).readReference(town));
		town.setTownHall(readers.getReader(TownHallReader.class).readReference(town));
		town.setArmory(readers.getReader(ArmoryReader.class).readReference(town));
		town.setLikes(in.readInt());
		town.setDislikes(in.readInt());
		town.setHardnessField(in.readDouble());
		town.setLastAttackFinishTick(in.readLong());
		
		//FIXME: FULL BUGADO, NAO USAR!!! LENDO APENAS PARA NAO DAR EOF OU IOEXCEPTION
		readers.getReader(BoughtTileMapByDepthReader.class).readReference(town);
		
		//CALCULAR PROPRIO COM BASE NO TOWN TILE MAP!!!
		town.setDefaultBoughtTileMapByDepth();
		
		town.setDefaultTilesBought();
		
		town.setDefaultFinishedTutorial();
	}
	
	private void readerVersion1(Town town) throws IOException, ClassNotFoundException
	{
		town.setTownBlockMap(readers.getReader(TownBlockMapReader.class).readReference(town));
		town.setTownTileMap(readers.getReader(TownTileMapReader.class).readReference(town));
		town.setStructureMap(readers.getReader(StructureMapReader.class).readReference(town));
		town.setMovingStructure(readers.getReader(GenericStructureReader.class).readReference(town));
		town.setTownHall(readers.getReader(TownHallReader.class).readReference(town));
		town.setArmory(readers.getReader(ArmoryReader.class).readReference(town));
		town.setLikes(in.readInt());
		town.setDislikes(in.readInt());
		town.setHardnessField(in.readDouble());
		town.setLastAttackFinishTick(in.readLong());
		town.setBoughtTileMapByDepth(readers.getReader(BoughtTileMapByDepthReader.class).readReference(town));
		town.setTilesBought(in.readInt());
		town.setDefaultFinishedTutorial();
	}
	
	private void readerVersion2(Town town) throws IOException, ClassNotFoundException
	{
		town.setTownBlockMap(readers.getReader(TownBlockMapReader.class).readReference(town));
		town.setTownTileMap(readers.getReader(TownTileMapReader.class).readReference(town));
		town.setStructureMap(readers.getReader(StructureMapReader.class).readReference(town));
		town.setTownHall(readers.getReader(TownHallReader.class).readReference(town));
		town.setArmory(readers.getReader(ArmoryReader.class).readReference(town));
		town.setLikes(in.readInt());
		town.setDislikes(in.readInt());
		town.setHardnessField(in.readDouble());
		town.setLastAttackFinishTick(in.readLong());
		town.setBoughtTileMapByDepth(readers.getReader(BoughtTileMapByDepthReader.class).readReference(town));
		town.setTilesBought(in.readInt());
		town.setDefaultFinishedTutorial();
	}
	
	private void readerVersion3(Town town) throws IOException, ClassNotFoundException
	{
		town.setTownBlockMap(readers.getReader(TownBlockMapReader.class).readReference(town));
		town.setTownTileMap(readers.getReader(TownTileMapReader.class).readReference(town));
		town.setStructureMap(readers.getReader(StructureMapReader.class).readReference(town));
		town.setTownHall(readers.getReader(TownHallReader.class).readReference(town));
		town.setArmory(readers.getReader(ArmoryReader.class).readReference(town));
		town.setLikes(in.readInt());
		town.setDislikes(in.readInt());
		town.setHardnessField(in.readDouble());
		town.setLastAttackFinishTick(in.readLong());
		town.setBoughtTileMapByDepth(readers.getReader(BoughtTileMapByDepthReader.class).readReference(town));
		town.setTilesBought(in.readInt());
		town.setFinishedTutorial(in.readBoolean());
		town.setAssistant((Villager) readers.getReader(EntityReader.class).readReference(town.getPlugin()));
	}
	
	public Town readReference(Main plugin) throws IOException, ClassNotFoundException
	{
		return super.readReference(parameterizedReadable.getReadable(plugin));
	}
}
