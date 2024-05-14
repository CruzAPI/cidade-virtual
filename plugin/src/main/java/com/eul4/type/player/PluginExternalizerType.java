package com.eul4.type.player;

import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.externalizer.reader.ObjectReader;
import com.eul4.common.externalizer.writer.ObjectWriter;
import com.eul4.common.type.player.*;
import com.eul4.externalizer.reader.*;
import com.eul4.externalizer.writer.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PluginExternalizerType implements ExternalizerType
{
	ADMIN(AdminReader::new, AdminReader.class, AdminWriter::new, AdminWriter.class),
	ATTACKER(AttackerReader::new, AttackerReader.class, AttackerWriter::new, AttackerWriter.class),
	BLOCK_DATA_MAP(BlockDataMapReader::new, BlockDataMapReader.class, BlockDataMapWriter::new, BlockDataMapWriter.class),
	BLOCK_DATA(BlockDataReader::new, BlockDataReader.class, BlockDataWriter::new, BlockDataWriter.class),
	DISLIKE_DEPOSIT(DislikeDepositReader::new, DislikeDepositReader.class, DislikeDepositWriter::new, DislikeDepositWriter.class),
	DISLIKE_GENERATOR(DislikeGeneratorReader::new, DislikeGeneratorReader.class, DislikeGeneratorWriter::new, DislikeGeneratorWriter.class),
	GENERIC_PLUGIN_PLAYER(GenericPluginPlayerReader::new, GenericPluginPlayerReader.class, GenericPluginPlayerWriter::new, GenericPluginPlayerWriter.class),
	GENERIC_STRUCTURE(GenericStructureReader::new, GenericStructureReader.class, GenericStructureWriter::new, GenericStructureWriter.class),
	LIKE_DEPOSIT(LikeDepositReader::new, LikeDepositReader.class, LikeDepositWriter::new, LikeDepositWriter.class),
	LIKE_GENERATOR(LikeGeneratorReader::new, LikeGeneratorReader.class, LikeGeneratorWriter::new, LikeGeneratorWriter.class),
	RAID_ANALYZER(RaidAnalyzerReader::new, RaidAnalyzerReader.class, RaidAnalyzerWriter::new, RaidAnalyzerWriter.class),
	SHORT_COORDINATE_BLOCK_CHUNK(ShortCoordinateBlockChunkReader::new, ShortCoordinateBlockChunkReader.class, ShortCoordinateBlockChunkWriter::new, ShortCoordinateBlockChunkWriter.class),
	STRUCTURE_SET(StructureSetReader::new, StructureSetReader.class, StructureSetWriter::new, StructureSetWriter.class),
	TOWN_BLOCK_MAP(TownBlockMapReader::new, TownBlockMapReader.class, TownBlockMapWriter::new, TownBlockMapWriter.class),
	TOWN_BLOCK(TownBlockReader::new, TownBlockReader.class, TownBlockWriter::new, TownBlockWriter.class),
	TOWN_BLOCK_SET(TownBlockSetReader::new, TownBlockSetReader.class, TownBlockSetWriter::new, TownBlockSetWriter.class),
	TOWN_HALL(TownHallReader::new, TownHallReader.class, TownHallWriter::new, TownHallWriter.class),
	TOWN_MAP(TownMapReader::new, TownMapReader.class, TownMapWriter::new, TownMapWriter.class),
	TOWN_PLAYER_DATA(TownPlayerDataReader::new, TownPlayerDataReader.class, TownPlayerDataWriter::new, TownPlayerDataWriter.class),
	TOWN_PLAYER(TownPlayerReader::new, TownPlayerReader.class, TownPlayerWriter::new, TownPlayerWriter.class),
	TOWN(TownReader::new, TownReader.class, TownWriter::new, TownWriter.class),
	TOWN_TILE_MAP(TownTileMapReader::new, TownTileMapReader.class, TownTileMapWriter::new, TownTileMapWriter.class),
	TOWN_TILE(TownTileReader::new, TownTileReader.class, TownTileWriter::new, TownTileWriter.class),
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
