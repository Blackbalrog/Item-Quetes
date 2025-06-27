package fr.blackbalrog.quetes.update;

import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.blackbalrog.quetes.Quetes;
import fr.blackbalrog.quetes.builder.ItemBuilder;
import fr.blackbalrog.quetes.configurations.DefaultConfiguration;

public class QueteUpdate
{

	private static DefaultConfiguration configuration = Quetes.getInstance().getConfiguration();

	/**
	 * @apiNote Update le Tag de la quête
	 */
	public static void update(Player player, String event, FileConfiguration fileConfiguration, ItemBuilder itemBuilder, String materialName, int slot)
	{

		ConfigurationSection questsSection = fileConfiguration.getConfigurationSection("Quetes");
		if (questsSection == null) return;

		for (String id : questsSection.getKeys(false))
		{
			ConfigurationSection section = questsSection.getConfigurationSection(id);
			if (section == null) continue;

			String questEvent = section.getString("event");
			if (questEvent == null || !questEvent.equalsIgnoreCase(event)) continue;

			String type = section.getString("type");
			if (type == null || (!type.equalsIgnoreCase("ALL") && !materialName.equalsIgnoreCase(type))) continue;

			int progress = itemBuilder.getIntTag("quete_" + id);
			int required = section.getInt("count");

			if (progress >= required) continue;

			progress++;
			itemBuilder.setIntTag("quete_" + id, progress);

			List<String> newLore = UpdateItemLore.update(itemBuilder, fileConfiguration);
			itemBuilder.setLores(newLore);
			itemBuilder.build();

			player.getInventory().setItem(slot, itemBuilder.getItemStack());
			player.updateInventory();
			
			if (progress == required && configuration.getBoolean("Title.Actived"))
			{
				player.sendTitle("§aFélicitations", "§7Quête terminée", 
						configuration.getInt("Title.Timer.fadeIn"), 
						configuration.getInt("Title.Timer.stay"), 
						configuration.getInt("Title.Timer.fadeOut"));
			}

			if (allQuetesCompleted(itemBuilder, questsSection))
			{
				player.sendTitle("§aFélicitations", "§7Vous avez terminé toutes les quêtes", 
						configuration.getInt("Title.Timer.fadeIn"), 
						configuration.getInt("Title.Timer.stay"), 
						configuration.getInt("Title.Timer.fadeOut"));
			}
			return;
		}
	}
	
	private static boolean allQuetesCompleted(ItemBuilder itemBuilder, ConfigurationSection questsSection)
	{
		Set<String> allQuests = questsSection.getKeys(false);
		for (String questId : allQuests)
		{
			ConfigurationSection quest = questsSection.getConfigurationSection(questId);
			if (quest == null) continue;
			int required = quest.getInt("count");
			int progress = itemBuilder.getIntTag("quete_" + questId);
			if (progress < required) return false;
		}
		return true;
	}
}
