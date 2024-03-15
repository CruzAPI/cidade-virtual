package com.eul4.util;

import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

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
}
