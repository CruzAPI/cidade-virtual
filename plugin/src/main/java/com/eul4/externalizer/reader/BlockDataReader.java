package com.eul4.externalizer.reader;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.wrapper.Readable;
import com.eul4.common.wrapper.Reader;
import com.eul4.enums.Rarity;
import com.eul4.service.BlockData;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.StabilityFormula;
import lombok.Getter;

import java.io.IOException;
import java.util.Optional;

@Getter
public class BlockDataReader extends ObjectReader<BlockData>
{
	private final Reader<BlockData> reader;
	private final Readable<BlockData> readable;
	
	public BlockDataReader(Readers readers) throws InvalidVersionException
	{
		super(readers, BlockData.class);
		
		final ObjectType objectType = PluginObjectType.BLOCK_DATA;
		final byte version = readers.getVersions().get(objectType);
		
		switch(version)
		{
		case 0:
			this.reader = this::readerVersion0;
			this.readable = this::readableVersion0;
			break;
		case 1:
			this.reader = Reader.identity();
			this.readable = this::readableVersion1;
			break;
		case 2:
			this.reader = Reader.identity();
			this.readable = this::readableVersion2;
			break;
		case 3:
			this.reader = Reader.identity();
			this.readable = this::readableVersion3;
			break;
		case 4:
			this.reader = Reader.identity();
			this.readable = this::readableVersion4;
			break;
		case 5:
			this.reader = Reader.identity();
			this.readable = this::readableVersion5;
			break;
		default:
			throw new InvalidVersionException("Invalid " + objectType + " version: " + version);
		}
	}
	
	private void readerVersion0(BlockData blockData) throws IOException
	{
		byte[] main = new byte[1];
		
		in.readFully(main);
		
		if((main[0] & 0b10000000) == 0b10000000)
		{
			byte bitmap = in.readByte();
			
			blockData.hasHardness((bitmap & 0b10000000) == 0b10000000);
		}
	}
	
	private BlockData readableVersion0()
	{
		return BlockData.builder()
				.origin(BlockData.Origin.UNKNOWN)
				.build();
	}
	
	private BlockData readableVersion1() throws IOException
	{
		boolean hasHardness = in.readBoolean();
		Rarity rarity = Optional.ofNullable(Rarity.getRarityById(in.readByte())).orElse(Rarity.COMMON);
		float health = in.readFloat();
		boolean willDrop = in.readBoolean();
		
		return BlockData.builder()
				.hasHardness(hasHardness)
				.rarity(rarity)
				.health(health)
				.willDrop(willDrop)
				.origin(BlockData.Origin.UNKNOWN)
				.build();
	}
	
	private BlockData readableVersion2() throws IOException
	{
		boolean hasHardness = in.readBoolean();
		Rarity rarity = Optional.ofNullable(Rarity.getRarityById(in.readByte())).orElse(Rarity.COMMON);
		float health = in.readFloat();
		boolean willDrop = in.readBoolean();
		byte[] enchantmens = new byte[2];
		in.readFully(enchantmens);
		
		return BlockData.builder()
				.hasHardness(hasHardness)
				.rarity(rarity)
				.health(health)
				.willDrop(willDrop)
				.enchantments(enchantmens)
				.origin(BlockData.Origin.UNKNOWN)
				.build();
	}
	
	private BlockData readableVersion3() throws IOException
	{
		boolean hasHardness = in.readBoolean();
		Rarity rarity = Optional.ofNullable(Rarity.getRarityById(in.readByte())).orElse(Rarity.COMMON);
		float health = in.readFloat();
		boolean willDrop = in.readBoolean();
		byte[] enchantmens = new byte[2];
		in.readFully(enchantmens);
		BlockData.Origin origin = BlockData.Origin.getById(in.readByte());
		
		return BlockData.builder()
				.hasHardness(hasHardness)
				.rarity(rarity)
				.health(health)
				.willDrop(willDrop)
				.enchantments(enchantmens)
				.origin(origin)
				.build();
	}
	
	private BlockData readableVersion4() throws IOException, ClassNotFoundException
	{
		boolean hasHardness = in.readBoolean();
		Rarity rarity = Optional.ofNullable(Rarity.getRarityById(in.readByte())).orElse(Rarity.COMMON);
		float health = in.readFloat();
		boolean willDrop = in.readBoolean();
		byte[] enchantments = new byte[3];
		in.readFully(enchantments);
		BlockData.Origin origin = BlockData.Origin.getById(in.readByte());
		StabilityFormula stabilityFormula = readers.getReader(StabilityFormularReader.class).readReference();
		
		return BlockData.builder()
				.hasHardness(hasHardness)
				.rarity(rarity)
				.health(health)
				.willDrop(willDrop)
				.enchantments(enchantments)
				.origin(origin)
				.stabilityFormula(stabilityFormula)
				.build();
	}
	
	private BlockData readableVersion5() throws IOException, ClassNotFoundException
	{
		boolean hasHardness = in.readBoolean();
		Rarity rarity = Optional.ofNullable(Rarity.getRarityById(in.readByte())).orElse(Rarity.COMMON);
		float health = in.readFloat();
		boolean willDrop = in.readBoolean();
		byte[] enchantments = new byte[3];
		in.readFully(enchantments);
		BlockData.Origin origin = BlockData.Origin.getById(in.readByte());
		StabilityFormula stabilityFormula = readers.getReader(StabilityFormularReader.class).readReference();
		byte scrapeHealth = in.readByte();
		
		return BlockData.builder()
				.hasHardness(hasHardness)
				.rarity(rarity)
				.health(health)
				.willDrop(willDrop)
				.enchantments(enchantments)
				.origin(origin)
				.stabilityFormula(stabilityFormula)
				.scrapeHealth(scrapeHealth)
				.build();
	}
	
	public BlockData readReference() throws IOException, ClassNotFoundException
	{
		return super.readReference(readable);
	}
	
	public BlockData readObject() throws IOException, ClassNotFoundException
	{
		BlockData blockData = readable.read();
		reader.readObject(blockData);
		return blockData;
	}
}
