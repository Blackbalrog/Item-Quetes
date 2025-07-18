package fr.blackbalrog.quetes.api.update;

import java.util.ArrayList;
import java.util.List;

import fr.blackbalrog.quetes.Quetes;
import fr.blackbalrog.quetes.configurations.DefaultConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import fr.blackbalrog.quetes.builder.ItemBuilder;

public class ItemLore
{
	
	private static DefaultConfiguration messages = Quetes.getInstance().getMessages();
	
	/**
	 * @apiNote Actualise la lore
	 */
	public static List<String> update(ItemBuilder builder, FileConfiguration configuration)
	{
		List<String> lore = new ArrayList<>();
		lore.add("");
		lore.add("§7§nQuetes:");

		boolean foundCurrent = false;

		for (String id : configuration.getConfigurationSection("Quetes").getKeys(false))
		{
			ConfigurationSection section = configuration.getConfigurationSection("Quetes." + id);
			
			if (!builder.isContainsTag("quete_" + id)) builder.setIntTag("quete_" + id, 0);
			
			String name = section.getString("name").replace("&", "§");
			int progress = builder.getIntTag("quete_" + id);
			int max = section.getInt("count");
			
			if (progress >= max)
			{
				lore.add("§7§m" + name + ": " + max);
				continue;
			}
			
			if (!foundCurrent)
			{
				foundCurrent = true;
				lore.add("§a§l" + name + ": §b" + progress);
				
				// Lore Quete
				if (section.contains("lores"))
				{
					for (String keyLore : section.getStringList("lores"))
					{
						lore.add(keyLore.replaceAll("&", "§"));
					}
					
				}
				
			}
			else
			{
				String quete_name = section.getString("name")
						.replaceAll("&", "§");
				String quetes_lock = messages.getString("quetes.lock")
						.replace("%quete_name%", quete_name)
						.replaceAll("&", "§");
				lore.add(quetes_lock);
			}
		}
		
		lore.add("");
		lore.add("");
		lore.add("§eClique gauche:");
		lore.add("§7Une fois toutes les quêtes terminer,");
		lore.add("§7Vous pourrez ouvrir l'inventaire des récompenses");
		lore.add("");
		lore.add("§aClique droit: §7Pour déplacer l'item");
		return lore;
	}
	
}
