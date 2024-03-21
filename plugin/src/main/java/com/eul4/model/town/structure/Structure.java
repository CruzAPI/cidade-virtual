package com.eul4.model.town.structure;

import com.eul4.StructureType;
import com.eul4.exception.CannotConstructException;

import java.io.File;

public interface Structure
{
	String getName();
	int getLevel();
	File getSchematicFile();
	StructureType getStructureType();
	void pasteSchematic() throws CannotConstructException;
}
