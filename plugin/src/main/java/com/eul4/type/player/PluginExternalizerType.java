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
	ARMORY(ArmoryReader::new, ArmoryReader.class, ArmoryWriter::new, ArmoryWriter.class),
	ATTACKER(AttackerReader::new, AttackerReader.class, AttackerWriter::new, AttackerWriter.class),
	BOUGHT_TILE_MAP_BY_DEPTH(BoughtTileMapByDepthReader::new, BoughtTileMapByDepthReader.class, BoughtTileMapByDepthWriter::new, BoughtTileMapByDepthWriter.class),
	BLOCK_DATA_MAP(BlockDataMapReader::new, BlockDataMapReader.class, BlockDataMapWriter::new, BlockDataMapWriter.class),
	BLOCK_DATA(BlockDataReader::new, BlockDataReader.class, BlockDataWriter::new, BlockDataWriter.class),
	BROADCAST_HASH_SET(BroadcastHashSetReader::new, BroadcastHashSetReader.class, BroadcastHashSetWriter::new, BroadcastHashSetWriter.class),
	CANNON(CannonReader::new, CannonReader.class, CannonWriter::new, CannonWriter.class),
	CAPACITATED_CROWN_HOLDER(CapacitatedCrownHolderReader::new, CapacitatedCrownHolderReader.class, CapacitatedCrownHolderWriter::new, CapacitatedCrownHolderWriter.class),
	CHECKPOINT_STEP_ENUM(CheckpointStepEnumReader::new, CheckpointStepEnumReader.class, CheckpointStepEnumWriter::new, CheckpointStepEnumWriter.class),
	CROWN_DEPOSIT(CrownDepositReader::new, CrownDepositReader.class, CrownDepositWriter::new, CrownDepositWriter.class),
	CROWN_INFO(CrownInfoReader::new, CrownInfoReader.class, CrownInfoWriter::new, CrownInfoWriter.class),
	CRYPTO_INFO(CryptoInfoReader::new, CryptoInfoReader.class, CryptoInfoWriter::new, CryptoInfoWriter.class),
	DEFENDER(DefenderReader::new, DefenderReader.class, DefenderWriter::new, DefenderWriter.class),
	DEFENDER_SPECTATOR(DefenderSpectatorReader::new, DefenderSpectatorReader.class, DefenderSpectatorWriter::new, DefenderSpectatorWriter.class),
	DISLIKE_DEPOSIT(DislikeDepositReader::new, DislikeDepositReader.class, DislikeDepositWriter::new, DislikeDepositWriter.class),
	DISLIKE_GENERATOR(DislikeGeneratorReader::new, DislikeGeneratorReader.class, DislikeGeneratorWriter::new, DislikeGeneratorWriter.class),
	GENERIC_PLUGIN_PLAYER(GenericPluginPlayerReader::new, GenericPluginPlayerReader.class, GenericPluginPlayerWriter::new, GenericPluginPlayerWriter.class),
	GENERIC_STRUCTURE(GenericStructureReader::new, GenericStructureReader.class, GenericStructureWriter::new, GenericStructureWriter.class),
	HOME_MAP(HomeMapReader::new, HomeMapReader.class, HomeMapWriter::new, HomeMapWriter.class),
	INVENTORY_ORGANIZER_PLAYER(InventoryOrganizerPlayerReader::new, InventoryOrganizerPlayerReader.class, InventoryOrganizerPlayerWriter::new, InventoryOrganizerPlayerWriter.class),
	LIKE_DEPOSIT(LikeDepositReader::new, LikeDepositReader.class, LikeDepositWriter::new, LikeDepositWriter.class),
	LIKE_GENERATOR(LikeGeneratorReader::new, LikeGeneratorReader.class, LikeGeneratorWriter::new, LikeGeneratorWriter.class),
	PLUGIN_PLAYER_DATA(PluginPlayerDataReader::new, PluginPlayerDataReader.class, PluginPlayerDataWriter::new, PluginPlayerDataWriter.class),
	POINT_4_BIT(Point4BitReader::new, Point4BitReader.class, Point4BitWriter::new, Point4BitWriter.class),
	RAID_ANALYZER(RaidAnalyzerReader::new, RaidAnalyzerReader.class, RaidAnalyzerWriter::new, RaidAnalyzerWriter.class),
	RAID_SPECTATOR(RaidSpectatorReader::new, RaidSpectatorReader.class, RaidSpectatorWriter::new, RaidSpectatorWriter.class),
	RAW_MATERIAL(RawMaterialReader::new, RawMaterialReader.class, RawMaterialWriter::new, RawMaterialWriter.class),
	RAW_MATERIAL_MAP(RawMaterialMapReader::new, RawMaterialMapReader.class, RawMaterialMapWriter::new, RawMaterialMapWriter.class),
	SHORT_COORDINATE_BLOCK_CHUNK(ShortCoordinateBlockChunkReader::new, ShortCoordinateBlockChunkReader.class, ShortCoordinateBlockChunkWriter::new, ShortCoordinateBlockChunkWriter.class),
	SPAWN_PLAYER(SpawnPlayerReader::new, SpawnPlayerReader.class, SpawnPlayerWriter::new, SpawnPlayerWriter.class),
	STABILITY_FORMULA(StabilityFormularReader::new, StabilityFormularReader.class, StabilityFormulaWriter::new, StabilityFormulaWriter.class),
	STRUCTURE_MAP(StructureMapReader::new, StructureMapReader.class, StructureMapWriter::new, StructureMapWriter.class),
	TAG(TagReader::new, TagReader.class, TagWriter::new, TagWriter.class),
	TOWN_BLOCK_MAP(TownBlockMapReader::new, TownBlockMapReader.class, TownBlockMapWriter::new, TownBlockMapWriter.class),
	TOWN_BLOCK(TownBlockReader::new, TownBlockReader.class, TownBlockWriter::new, TownBlockWriter.class),
	TOWN_BLOCK_SET(TownBlockSetReader::new, TownBlockSetReader.class, TownBlockSetWriter::new, TownBlockSetWriter.class),
	TOWN_HALL(TownHallReader::new, TownHallReader.class, TownHallWriter::new, TownHallWriter.class),
	TOWN_MAP(TownMapReader::new, TownMapReader.class, TownMapWriter::new, TownMapWriter.class),
	TOWN_PLAYER_DATA(TownPlayerDataReader::new, TownPlayerDataReader.class, TownPlayerDataWriter::new, TownPlayerDataWriter.class),
	TOWN_PLAYER(TownPlayerReader::new, TownPlayerReader.class, TownPlayerWriter::new, TownPlayerWriter.class),
	TOWN_TILE_MAP(TownTileMapReader::new, TownTileMapReader.class, TownTileMapWriter::new, TownTileMapWriter.class),
	TOWN_TILE(TownTileReader::new, TownTileReader.class, TownTileWriter::new, TownTileWriter.class),
	TOWN(TownReader::new, TownReader.class, TownWriter::new, TownWriter.class),
	TURRET(TurretReader::new, TurretReader.class, TurretWriter::new, TurretWriter.class),
	TUTORIAL_TOWN_PLAYER_DATA(TutorialTownPlayerDataReader::new, TutorialTownPlayerDataReader.class, TutorialTownPlayerDataWriter::new, TutorialTownPlayerDataWriter.class),
	TUTORIAL_TOWN_PLAYER(TutorialTownPlayerReader::new, TutorialTownPlayerReader.class, TutorialTownPlayerWriter::new, TutorialTownPlayerWriter.class),
	VANILLA_PLAYER_DATA(VanillaPlayerDataReader::new, VanillaPlayerDataReader.class, VanillaPlayerDataWriter::new, VanillaPlayerDataWriter.class),
	VANILLA_PLAYER(VanillaPlayerReader::new, VanillaPlayerReader.class, VanillaPlayerWriter::new, VanillaPlayerWriter.class),
	VECTOR_3(Vector3Reader::new, Vector3Reader.class, Vector3Writer::new, Vector3Writer.class),
	;
	private final ReaderConstructor readerConstructor;
	private final Class<? extends ObjectReader> readerClass;
	private final WriterConstructor writerConstructor;
	private final Class<? extends ObjectWriter> writerClass;
	
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
