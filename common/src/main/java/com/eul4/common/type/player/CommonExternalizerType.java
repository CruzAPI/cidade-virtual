package com.eul4.common.type.player;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.*;
import com.eul4.common.externalizer.writer.*;
import com.eul4.common.wrapper.UUIDHashSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommonExternalizerType implements ExternalizerType
{
	BLOCK_VECTOR(BlockVectorReader::new, BlockVectorReader.class, BlockVectorWriter::new, BlockVectorWriter.class),
	BLOCK(BlockReader::new, BlockReader.class, BlockWriter::new, BlockWriter.class),
	CHUNK(ChunkReader::new, ChunkReader.class, ChunkWriter::new, ChunkWriter.class),
	COMMON_PLAYER_DATA(CommonPlayerDataReader::new, CommonPlayerDataReader.class, CommonPlayerDataWriter::new, CommonPlayerDataWriter.class),
	ENTITY(EntityReader::new, EntityReader.class, EntityWriter::new, EntityWriter.class),
	GROUP(GroupReader::new, GroupReader.class, GroupWriter::new, GroupWriter.class),
	GROUP_GROUP(GroupGroupReader::new, GroupGroupReader.class, GroupGroupWriter::new, GroupGroupWriter.class),
	GROUP_GROUP_MAP(GroupGroupMapReader::new, GroupGroupMapReader.class, GroupGroupMapWriter::new, GroupGroupMapWriter.class),
	GROUP_MAP(GroupMapReader::new, GroupMapReader.class, GroupMapWriter::new, GroupMapWriter.class),
	GROUP_USER(GroupUserReader::new, GroupUserReader.class, GroupUserWriter::new, GroupUserWriter.class),
	GROUP_USER_MAP(GroupUserMapReader::new, GroupUserMapReader.class, GroupUserMapWriter::new, GroupUserMapWriter.class),
	HOLOGRAM(HologramReader::new, HologramReader.class, HologramWriter::new, HologramWriter.class),
	INVENTORY(InventoryReader::new, InventoryReader.class, InventoryWriter::new, InventoryWriter.class),
	ITEM_STACK(ItemStackReader::new, ItemStackReader.class, ItemStackWriter::new, ItemStackWriter.class),
	LOCATION(LocationReader::new, LocationReader.class, LocationWriter::new, LocationWriter.class),
	PERMISSION(PermissionReader::new, PermissionReader.class, PermissionWriter::new, PermissionWriter.class),
	PERMISSION_MAP(PermissionMapReader::new, PermissionMapReader.class, PermissionMapWriter::new, PermissionMapWriter.class),
	PLAYER_DATA(PlayerDataReader::new, PlayerDataReader.class, PlayerDataWriter::new, PlayerDataWriter.class),
	POTION_EFFECT_COLLECTION(PotionEffectCollectionReader::new, PotionEffectCollectionReader.class, PotionEffectCollectionWriter::new, PotionEffectCollectionWriter.class),
	POTION_EFFECT(PotionEffectReader::new, PotionEffectReader.class, PotionEffectWriter::new, PotionEffectWriter.class),
	TIMED_TICK(TimedTickReader::new, TimedTickReader.class, TimedTickWriter::new, TimedTickWriter.class),
	TRANSLATED_HOLOGRAM_LINE(TranslatedHologramLineReader::new, TranslatedHologramLineReader.class, TranslatedHologramLineWriter::new, TranslatedHologramLineWriter.class),
	USER(UserReader::new, UserReader.class, UserWriter::new, UserWriter.class),
	UUID(UUIDReader::new, UUIDReader.class, UUIDWriter::new, UUIDWriter.class),
	UUID_HASH_SET(UUIDHashSetReader::new, UUIDHashSetReader.class, UUIDHashSetWriter::new, UUIDHashSetWriter.class),
	;
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
