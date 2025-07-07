package fr.blackbalrog.quetes.update;

import fr.blackbalrog.quetes.handler.UpdateHandler;
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
import fr.blackbalrog.quetes.manager.Quete;

public class QueteUpdate
{
	
	private DefaultConfiguration configuration = Quetes.getInstance().getConfiguration();
	private String eventType;
	private FileConfiguration queteConfiguration;
	private ItemBuilder itemQuete;
	private String materialNameQuete;
	private Event event;
	private UpdateHandler updateHandler;
	
	
	public QueteUpdate(Event event, UpdateHandler updateHandler, String eventType, FileConfiguration queteConfiguration, ItemBuilder itemQuete, String materialNameQuete)
	{
		this.event = event;
		this.updateHandler = updateHandler;
		this.eventType = eventType;
		this.queteConfiguration = queteConfiguration;
		this.itemQuete = itemQuete;
		this.materialNameQuete = materialNameQuete;
	}
	
	/**
	 * @apiNote Update le Tag de la quête
	 */
	public void update(Player player, int slot)
	{
		
		ConfigurationSection sectionQuete = queteConfiguration.getConfigurationSection("Quetes");
		if (sectionQuete == null) return;
		
		for (String id : sectionQuete.getKeys(false))
		{
			ConfigurationSection section = sectionQuete.getConfigurationSection(id);
			if (section == null) continue;
			
			if (!section.getString("event", "").equalsIgnoreCase(eventType)) continue;
			String type = section.getString("type");
			if (type == null || (!type.equalsIgnoreCase("ALL") && !materialNameQuete.equalsIgnoreCase(type))) continue;
			
			Quete quete = new Quete(itemQuete, queteConfiguration);
			if (quete.isQueteComplete(id)) continue;
			
			/*------------------------------------*/
			
			// Post Update
			
			if (this.updateHandler != null)
			{
				this.updateHandler.postUpdate(event, section);
			}
			
			/*------------------------------------*/
			
			quete.increment(id);
			int progress = quete.getProgress("quete_" + id);
			int required = section.getInt("count");

			itemQuete.setLores(UpdateItemLore.update(itemQuete, queteConfiguration));
			itemQuete.build();
			player.getInventory().setItem(slot, itemQuete.getItemStack());
			player.updateInventory();
			
			/*------------------------------------*/
			
			// Pre Update
			
			if (this.updateHandler != null)
			{
				this.updateHandler.preUpdate(event, section);
			}
			
			/*------------------------------------*/
			
			if (progress == required)
			{
				if (configuration.getBoolean("Title.Actived"))
				{
					player.sendTitle("§aFélicitations", "§7Quête terminée",
							this.configuration.getInt("Title.Timer.fadeIn"), 
							this.configuration.getInt("Title.Timer.stay"), 
							this.configuration.getInt("Title.Timer.fadeOut"));
				}
				
				if (section.contains("reward"))
				{
					ConfigurationSection rewardSection = section.getConfigurationSection("reward");
					ItemBuilder reward = new ItemBuilder(Material.valueOf(rewardSection.getString("material")))
							.setName(rewardSection.getString("name").replaceAll("&", "§"))
							.setAmount(rewardSection.getInt("count"));
					reward.build();
					player.getInventory().addItem(reward.getItemStack());
				}
				
				if (section.contains("rewards"))
				{
					for (String rewardStr : section.getStringList("rewards"))
					{
						String[] split = rewardStr.split(":");
						ItemBuilder reward = new ItemBuilder(Material.valueOf(split[0]))
								.setName(split[2].replaceAll("&", "§"))
								.setAmount(Integer.parseInt(split[1]));
						reward.build();
						player.getInventory().addItem(reward.getItemStack());
					}
				}
			}
			
			if (quete.isAllCompleted())
			{
				player.sendTitle("§aFélicitations", "§7Vous avez terminé toutes les quêtes", 
						this.configuration.getInt("Title.Timer.fadeIn"), 
						this.configuration.getInt("Title.Timer.stay"), 
						this.configuration.getInt("Title.Timer.fadeOut"));
			}
			return;
		}
	}
}
