package fr.blackbalrog.quetes.update;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;

import fr.blackbalrog.quetes.Quetes;
import fr.blackbalrog.quetes.builder.ItemBuilder;
import fr.blackbalrog.quetes.configurations.DefaultConfiguration;

public class QueteUpdate
{

	private static DefaultConfiguration configuration = Quetes.getInstance().getConfiguration();

	/**
	 * @param event 
	 * @apiNote Update le Tag de la quête
	 */
	public static void update(Player player, String eventType, FileConfiguration fileConfiguration, ItemBuilder itemBuilder, String materialName, int slot, Event event)
	{

		ConfigurationSection questsSection = fileConfiguration.getConfigurationSection("Quetes");
		if (questsSection == null) return;

		for (String id : questsSection.getKeys(false))
		{
			ConfigurationSection section = questsSection.getConfigurationSection(id);
			if (section == null) continue;

			String questEvent = section.getString("event");
			if (questEvent == null || !questEvent.equalsIgnoreCase(eventType)) continue;

			String type = section.getString("type");
			if (type == null || (!type.equalsIgnoreCase("ALL") && !materialName.equalsIgnoreCase(type))) continue;

			int progress = itemBuilder.getIntTag("quete_" + id);
			int required = section.getInt("count");

			if (progress >= required) continue;

			if (section.isConfigurationSection("age"))
			{
				ConfigurationSection ageSection = section.getConfigurationSection("age");

				if (event instanceof EntityDeathEvent deathEvent)
				{
					if (deathEvent.getEntity() instanceof Ageable ageable)
					{
						String filter = ageSection.getString("filter");
						boolean isAdult = ageable.isAdult();

						if ((filter.equalsIgnoreCase("ADULTE") && !isAdult) || (filter.equalsIgnoreCase("BABY") && isAdult))
						{
							return;
						}
					}
				}
			}
			
			progress++;
			itemBuilder.setIntTag("quete_" + id, progress);

			List<String> newLore = UpdateItemLore.update(itemBuilder, fileConfiguration);
			itemBuilder.setLores(newLore);
			itemBuilder.build();
			
			player.getInventory().setItem(slot, itemBuilder.getItemStack());
			player.updateInventory();
			
			if (progress == required)
			{
				if (configuration.getBoolean("Title.Actived"))
				{
					player.sendTitle("§aFélicitations", "§7Quête terminée", 
							configuration.getInt("Title.Timer.fadeIn"), 
							configuration.getInt("Title.Timer.stay"), 
							configuration.getInt("Title.Timer.fadeOut"));
				}
				
				if (section.contains("reward"))
				{
					ConfigurationSection sectionReward = section.getConfigurationSection("reward");
					ItemBuilder itemReward = new ItemBuilder(Material.valueOf(sectionReward.getString("material")));
					itemReward.setName(sectionReward.getString("name").replaceAll("&", "§"))
						.setAmount(sectionReward.getInt("count"))
						.build();
					player.getInventory().addItem(itemReward.getItemStack());
				}
				
				if (section.contains("rewards"))
				{
					for (String reward : section.getStringList("rewards"))
					{
						String[] blockLine = reward.split(":");
						
						/**
						 * args[0] = material
						 * args[1] = amount
						 * args[2] = name
						 * 
						 * - "STONE:1:&bTest"
						 */
						
						ItemBuilder itemReward = new ItemBuilder(Material.valueOf(blockLine[0]));
						itemReward.setName(blockLine[2].replaceAll("&", "§"))
							.setAmount(Integer.parseInt(blockLine[1]))
							.build();
						player.getInventory().addItem(itemReward.getItemStack());
					}
				}
			}

			if (allQuetesCompleted(itemBuilder, questsSection))
			{
				player.sendTitle("§aFélicitations", "§7Vous avez terminé toutes les quêtes", 
						configuration.getInt("Title.Timer.fadeIn"), 
						configuration.getInt("Title.Timer.stay"), 
						configuration.getInt("Title.Timer.fadeOut"));
			}
			
			if (section.contains("noDrop") && section.getBoolean("noDrop"))
			{
				if (event instanceof BlockBreakEvent) ((BlockBreakEvent) event).setDropItems(false);
				if (event instanceof EnchantItemEvent) ((EnchantItemEvent) event).getItem().setType(Material.AIR);
				if (event instanceof PlayerFishEvent 
						&& ((PlayerFishEvent) event).getState() == PlayerFishEvent.State.CAUGHT_FISH 
						&& ((PlayerFishEvent) event).getCaught() != null) ((PlayerFishEvent) event).getCaught().remove();
				if (event instanceof EntityDeathEvent) ((EntityDeathEvent) event).getDrops().clear();
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
