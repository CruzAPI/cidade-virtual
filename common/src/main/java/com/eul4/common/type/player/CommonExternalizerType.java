package com.eul4.common.type.player;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.*;
import com.eul4.common.externalizer.writer.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommonExternalizerType implements ExternalizerType
{
	BLOCK_VECTOR(BlockVectorReader::new, BlockVectorReader.class, BlockVectorWriter::new, BlockVectorWriter.class),
	BLOCK(BlockReader::new, BlockReader.class, BlockWriter::new, BlockWriter.class),
	COMMON_PLAYER_DATA(CommonPlayerDataReader::new, CommonPlayerDataReader.class, CommonPlayerDataWriter::new, CommonPlayerDataWriter.class),
	HOLOGRAM(HologramReader::new, HologramReader.class, HologramWriter::new, HologramWriter.class),
	INVENTORY(InventoryReader::new, InventoryReader.class, InventoryWriter::new, InventoryWriter.class),
	ITEM_STACK(ItemStackReader::new, ItemStackReader.class, ItemStackWriter::new, ItemStackWriter.class),
	LOCATION(LocationReader::new, LocationReader.class, LocationWriter::new, LocationWriter.class),
	PLAYER_DATA(PlayerDataReader::new, PlayerDataReader.class, PlayerDataWriter::new, PlayerDataWriter.class),
	POTION_EFFECT_COLLECTION(PotionEffectCollectionReader::new, PotionEffectCollectionReader.class, PotionEffectCollectionWriter::new, PotionEffectCollectionWriter.class),
	POTION_EFFECT(PotionEffectReader::new, PotionEffectReader.class, PotionEffectWriter::new, PotionEffectWriter.class),
	TRANSLATED_HOLOGRAM_LINE(TranslatedHologramLineReader::new, TranslatedHologramLineReader.class, TranslatedHologramLineWriter::new, TranslatedHologramLineWriter.class);
	
	private final ReaderConstructor readerConstructor;
	private final Class<? extends ObjectReader<?>> readerClass;
	private final WriterConstructor writerConstructor;
	private final Class<? extends ObjectWriter<?>> writerClass;
	
	@Override
	public ObjectReader<?> newInstance(Readers readers) throws InvalidVersionException
	{
		return readerConstructor.newInstance(readers);
	}
	
	@Override
	public ObjectWriter<?> newInstance(Writers writers)
	{
		return writerConstructor.newInstance(writers);
	}
}
