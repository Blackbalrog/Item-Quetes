package fr.blackbalrog.quetes.manager;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import fr.blackbalrog.quetes.builder.ItemBuilder;

public class Quete
{
	
	private ItemBuilder itemQuete;
	private FileConfiguration configuration;
	private String queteID;
	
	public Quete(ItemBuilder itemQuete, FileConfiguration configuration)
	{
		this.itemQuete = itemQuete;
		this.configuration = configuration;
	}
	
	public void setQuete(String queteID)
	{
		this.queteID = queteID;
	}
	
	public String getQuete()
	{
		return this.queteID;
	}
	
	public void setProgress(String quete, int progress)
	{
		this.itemQuete.setIntTag(quete, progress);
	}
	
	public int getProgress(String queteID)
	{
		return this.itemQuete.getIntTag(queteID);
	}
	
	public boolean isQueteComplete(String id)
	{
		int progress = itemQuete.getIntTag("quete_" + id);
		int required = this.configuration.getInt("Quetes." + id + ".count");
		return progress >= required;
	}
	
	public void increment(String id)
	{
		int progress = itemQuete.getIntTag("quete_" + id);
		itemQuete.setIntTag("quete_" + id, progress + 1);
	}
	
	public boolean isAllCompleted()
	{
		ConfigurationSection quetesSection = this.configuration.getConfigurationSection("Quetes");
		if (quetesSection == null) return true;
		return quetesSection.getKeys(false).stream()
				.allMatch(this::isQueteComplete);
	}
	
	public Map<String, Integer> getQuetes()
	{
		return itemQuete.getTags().entrySet().stream()
				.filter(entry -> entry.getKey().startsWith("quete_"))
				.collect(Collectors.toMap(
						key -> key.getKey().replaceFirst("quete_", ""), 
						value -> (Integer) value.getValue()
				));
	}
	
	public ItemBuilder getItemQuete()
	{
		return this.itemQuete;
	}
	
}
