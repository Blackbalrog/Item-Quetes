package fr.blackbalrog.quetes.configurations;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DefaultConfiguration extends YamlConfiguration
{
	private File fileConfiguration;
	private String file_name;

	/**
	 * @apiNote Permet de créé des configuration par default, c'est le même délire que Spigot, mais en mode beaucoup plus simple :p
	 * Faut juste pas oublier de mettre dans le pom.xml l'include du fichier
	 */
	public DefaultConfiguration(JavaPlugin plugin, String file_name)
	{
		this.file_name = file_name;

		this.fileConfiguration = new File(plugin.getDataFolder(), file_name);
		if (!this.fileConfiguration.exists())
		{
			plugin.saveResource(file_name, false);
		}

		this.reload();
	}

	public void reload()
	{
		try
		{
			super.load(this.fileConfiguration);
		}
		catch (Exception exeption)
		{
			exeption.printStackTrace();
		}
	}

	@Override
	public void save(File file) throws IOException
	{
		try
		{
			super.save(this.fileConfiguration);
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}

	public String getFileName()
	{
		return this.file_name;
	}

	public File getFile()
	{
		return this.fileConfiguration;
	}
}