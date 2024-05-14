package com.eul4.wrapper;

import com.eul4.service.BlockData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.util.HashMap;

@RequiredArgsConstructor
@Getter
public class BlockDataMap extends HashMap<Block, BlockData>
{
	private final Chunk chunk;
}
