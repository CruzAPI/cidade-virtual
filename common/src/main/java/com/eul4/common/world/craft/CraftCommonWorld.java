package com.eul4.common.world.craft;

import com.eul4.common.world.CommonWorld;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;

@RequiredArgsConstructor
@Getter
public abstract class CraftCommonWorld implements CommonWorld
{
	protected final World world;
}
