package fr.blackbalrog.quetes.files;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.blackbalrog.quetes.Quetes;

public class QueteConfiguration
{
	
	private Map<String, FileConfiguration> configurationMap;
	private Map<String, File> fileMap;
	
	public QueteConfiguration()
	{
		this.configurationMap = new HashMap<>();
		this.fileMap = new HashMap<>();
	}
	
	public void init()
	{
		if (!this.configurationMap.isEmpty()) this.configurationMap.clear();
		if (!fileMap.isEmpty()) fileMap.clear();
		
		File quetesFolder = new File(Quetes.getInstance().getDataFolder(), "quetes");
		if (!quetesFolder.exists())
		{
			quetesFolder.mkdirs();
		}

		File[] files = quetesFolder.listFiles((dir, name) -> name.endsWith(".yml"));
		if (files != null)
		{
			for (File file : files)
			{
				String name = file.getName().replace(".yml", "");
				this.configurationMap.put(name, YamlConfiguration.loadConfiguration(file));
				this.fileMap.put(name, file);
			}
		}
	}
	
	public void reloadAll()
	{
		File quetesFolder = new File(Quetes.getInstance().getDataFolder(), "quetes");
		File[] files = quetesFolder.listFiles((dir, name) -> name.endsWith(".yml"));
		if (files != null)
		{
			for (Entry<String, FileConfiguration> entry : this.configurationMap.entrySet())
			{
				try
				{
					entry.getValue().load(this.fileMap.get(entry.getKey()));
				}
				catch (Exception exeption)
				{
					exeption.printStackTrace();
				}
			}
		}
	}
	
	public FileConfiguration getConfiguration(String name)
	{
		name = name.replaceAll("§.", "");
		return this.configurationMap.containsKey(name) ? this.configurationMap.get(name) : null;
	}
	
	public File getFile(String name)
	{
		name = name.replaceAll("§.", "");
		return this.fileMap.containsKey(name) ? this.fileMap.get(name) : null;
	}
}
