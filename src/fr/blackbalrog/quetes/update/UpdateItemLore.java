package fr.blackbalrog.quetes.update;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import fr.blackbalrog.quetes.builder.ItemBuilder;

public class UpdateItemLore
{
	
	/**
	 * @apiNote Actualise la lore
	 */
	public static List<String> update(ItemBuilder builder, FileConfiguration config)
	{
		List<String> lore = new ArrayList<>();
		lore.add("");
		lore.add("§7§nQuetes:");

		boolean foundCurrent = false;

		for (String id : config.getConfigurationSection("Quetes").getKeys(false))
		{
			ConfigurationSection section = config.getConfigurationSection("Quetes." + id);
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
			}
			else
			{
				lore.add("§7???");
			}
		}

		lore.add("");
		lore.add("§eClique gauche: §7Pour valider les quêtes");
		lore.add("§aClique droit: §7Pour déplacer l'item");
		return lore;
	}
	
}
