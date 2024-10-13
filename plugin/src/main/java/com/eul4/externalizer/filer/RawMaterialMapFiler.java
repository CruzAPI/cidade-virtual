package com.eul4.externalizer.filer;

import com.eul4.Main;
import com.eul4.common.exception.InvalidVersionException;
import com.eul4.common.type.player.CommonObjectType;
import com.eul4.common.type.player.ObjectType;
import com.eul4.common.type.player.Readers;
import com.eul4.common.type.player.Writers;
import com.eul4.common.util.FileUtil;
import com.eul4.externalizer.reader.RawMaterialMapReader;
import com.eul4.externalizer.writer.RawMaterialMapWriter;
import com.eul4.type.player.PluginObjectType;
import com.eul4.wrapper.RawMaterialMap;
import com.google.common.io.ByteStreams;
import lombok.Getter;

import java.io.*;
import java.text.MessageFormat;
import java.util.logging.Level;

public class RawMaterialMapFiler extends PluginFiler
{
	private static final byte VERSION = 0;
	
	private static final ObjectType[]
	
	OBJECT_TYPES_V0 = new ObjectType[]
	{
		CommonObjectType.BIG_DECIMAL,
		CommonObjectType.OBJECT,
		PluginObjectType.CRYPTO_INFO,
		PluginObjectType.RAW_MATERIAL,
		PluginObjectType.RAW_MATERIAL_MAP,
	};
	
	@Getter
	private RawMaterialMap rawMaterialMap;
	
	public RawMaterialMapFiler(Main plugin)
	{
		super(plugin, VERSION);
	}
	
	public void save()
	{
		File tmp = null;
		
		try
		{
			File file = plugin.getDataFileManager().createRawMaterialMapFileIfNotExists();
			tmp = new File(file.getParent(), "." + file.getName() + ".tmp");
			
			try
			(
				FileOutputStream fileOut = new FileOutputStream(tmp);
				BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut, BUFFER_SIZE);
				DataOutputStream out = new DataOutputStream(bufferedOut);
			)
			{
				Writers.of(plugin, out, writeVersions(out))
						.getWriter(RawMaterialMapWriter.class)
						.writeReferenceNotNull(rawMaterialMap);
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
			plugin.getLogger().log(Level.SEVERE, "Failed to create raw_material_map.dat file.", e);
		}
		finally
		{
			FileUtil.deleteTempFile(tmp, plugin.getLogger());
		}
	}
	
	public void load() throws Exception
	{
		if(rawMaterialMap != null)
		{
			throw new Exception("RawMaterialMap is already loaded.");
		}
		
		final File file = plugin.getDataFileManager().getRawMaterialMapFile();
		
		if(!file.exists() || file.length() == 0L)
		{
			plugin.getLogger().warning(MessageFormat.format("{0} file is empty or not exists! Loading empty RawMaterialMap!", file.getName()));
			this.rawMaterialMap = new RawMaterialMap();
			return;
		}
		
		try
		(
			FileInputStream fileInputStream = new FileInputStream(file);
			BufferedInputStream bufferedIn = new BufferedInputStream(fileInputStream);
			DataInputStream in = new DataInputStream(bufferedIn);
		)
		{
			this.rawMaterialMap = Readers.of(plugin, in, readVersions(in))
					.getReader(RawMaterialMapReader.class)
					.readReference();
			plugin.getLogger().info(MessageFormat.format("RawMaterialMap data loaded! size={0}", rawMaterialMap.getMap().size()));
		}
		catch(Exception e)
		{
			throw new Exception("Failed to load RawMaterialMap!", e);
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
