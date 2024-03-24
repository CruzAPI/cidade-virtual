package com.eul4.model.town.structure;

import com.eul4.StructureType;
import com.eul4.exception.CannotConstructException;
import com.eul4.model.town.TownBlock;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public interface Structure
{
	String getName();
	int getLevel();
	File getSchematicFile();
	StructureType getStructureType();
	
	void startMove() throws IOException;
	
	void cancelMove() throws CannotConstructException;
	
	void finishMove(TownBlock centerTownBlock, int rotation) throws CannotConstructException;
	
	ItemStack getItem();
	
	void finishMove(TownBlock centerTownBlock) throws CannotConstructException;
	Location getLocation();
}
