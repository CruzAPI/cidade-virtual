package com.eul4.common.util;

import java.io.File;
import java.util.logging.Logger;

public class FileUtil
{
	public static boolean deleteDirectory(File dir)
	{
		File[] allContents = dir.listFiles();
		
		if(allContents != null)
		{
			for(File file : allContents)
			{
				deleteDirectory(file);
			}
		}
		
		return dir.delete();
	}
	
	public static void deleteTempFile(File tmp, Logger logger)
	{
		if(tmp != null && tmp.exists())
		{
			if(tmp.delete())
			{
				logger.info("Temp file " + tmp.getName() + " deleted.");
			}
			else
			{
				logger.warning("Failed to delete temp file: " + tmp.getName());
			}
		}
	}
}
