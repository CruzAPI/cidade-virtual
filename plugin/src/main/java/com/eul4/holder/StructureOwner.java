package com.eul4.holder;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface StructureOwner extends TownOwner
{
	@NotNull UUID getStructureUniqueId();
}
