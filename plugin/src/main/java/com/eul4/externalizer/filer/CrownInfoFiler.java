package com.eul4.externalizer.filer;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.Writers;
import com.eul4.common.util.FileUtil;
import com.eul4.externalizer.reader.CrownInfoReader;
import com.eul4.externalizer.writer.CrownInfoWriter;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.CrownInfo;

import java.io.*;
import java.text.MessageFormat;
import java.util.logging.Level;

public class CrownInfoFiler extends PluginFiler
{
	private static final byte VERSION = 0;
	
	private static final ObjectType[]
	
	OBJECT_TYPES_V0 = new ObjectType[]
	{
		CommonObjectType.BIG_DECIMAL,
		CommonObjectType.OBJECT,
		PluginObjectType.CROWN_INFO,
		PluginObjectType.UNLIMITED_CROWN_HOLDER,
	};
	
	private CrownInfo crownInfo;
	
	public CrownInfoFiler(Main plugin)
	{
		super(plugin, VERSION);
	}
	
	public void save()
	{
		File tmp = null;
		
		try
		{
			File file = plugin.getDataFileManager().createCrownInfoFileIfNotExists();
			tmp = new File(file.getParent(), "." + file.getName() + ".tmp");
			
			try
			(
				FileOutputStream fileOut = new FileOutputStream(tmp);
				DataOutputStream out = new DataOutputStream(fileOut)
			)
			{
				Writers.of(plugin, out, writeVersions(out))
						.getWriter(CrownInfoWriter.class)
						.writeReferenceNotNull(crownInfo);
				out.flush();
			}
			
			if(tmp.renameTo(file))
			{
				plugin.getLogger().info(MessageFormat.format("File {0} saved! length={1}",
						file.getName(),
						file.length()));
			}
			else
			{
				throw new IOException("Failed to replace the old " + file.getName() + " file with the new one.");
			}
		}
		catch(Exception e)
		{
			plugin.getLogger().log(Level.SEVERE, "Failed to create CrownInfo file.", e);
		}
		finally
		{
			FileUtil.deleteTempFile(tmp, plugin.getLogger());
		}
	}
	
	public void load() throws Exception
	{
		if(crownInfo != null)
		{
			throw new Exception("CrownInfo is already loaded.");
		}
		
		final File file = plugin.getDataFileManager().getCrownInfoFile();
		
		if(!file.exists() || file.length() == 0L)
		{
			plugin.getLogger().warning(MessageFormat.format("{0} file is empty or not exists! Loading empty CrownInfo!", file.getName()));
			this.crownInfo = new CrownInfo(plugin);
			return;
		}
		
		try
		(
			FileInputStream fileIn = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fileIn)
		)
		{
			this.crownInfo = Readers.of(plugin, in, readVersions(in))
					.getReader(CrownInfoReader.class)
					.readReference(plugin);
			plugin.getLogger().info(MessageFormat.format("CrownInfo data loaded! crownInfo={0}", crownInfo));
		}
		catch(Exception e)
		{
			throw new Exception("Failed to load CrownInfo!", e);
		}
	}
	
	@Override
	protected ObjectType[] getObjectTypes(byte version) throws InvalidVersionException
	{
		return switch(version)
		{
			case 0 -> OBJECT_TYPES_V0;
			default -> throw new InvalidVersionException(MessageFormat.format(
					"Invalid {0} version: {1}",
					getClass().getSimpleName(),
					version));
		};
	}
}
