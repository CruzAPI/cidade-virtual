package com.eul4.util;

import java.io.File;

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
