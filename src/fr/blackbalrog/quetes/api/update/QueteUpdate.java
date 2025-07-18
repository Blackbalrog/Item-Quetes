package fr.blackbalrog.quetes.api.update;

import fr.blackbalrog.quetes.api.handler.UpdateHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import fr.blackbalrog.quetes.Quetes;
import fr.blackbalrog.quetes.builder.ItemBuilder;
import fr.blackbalrog.quetes.configurations.DefaultConfiguration;
import fr.blackbalrog.quetes.api.manager.Quete;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class QueteUpdate<E extends Event>
{
	
	private DefaultConfiguration configuration = Quetes.getInstance().getConfiguration();
	private String eventType;
	private FileConfiguration queteConfiguration;
	private ItemBuilder itemQuete;
	private String materialNameQuete;
	private E event;
	private UpdateHandler<E> updateHandler;
	
	
	public QueteUpdate(E event, UpdateHandler<E> updateHandler, String eventType, FileConfiguration queteConfiguration, ItemBuilder itemQuete, String materialNameQuete)
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
			
			if (section.isConfigurationSection("conditions"))
			{
				for (String equipment : section.getConfigurationSection("conditions").getKeys(false))
				{
					ConfigurationSection sectionEquipment = section.getConfigurationSection(equipment.toUpperCase());
					
					String equipmentType = sectionEquipment.getString(equipment);
					
					EquipmentSlot equipmentSlot = this.safeValueOf(EquipmentSlot.class, equipmentType);
					if (equipmentSlot == null) continue;
					
					ItemStack itemEquipment = player.getInventory().getItem(equipmentSlot);
					if (itemEquipment == null || ! itemEquipment.hasItemMeta()) continue;
					
					Material material = safeValueOf(Material.class, sectionEquipment.getString("material", ""));
					if (material == null) continue;
					if (itemEquipment.getType() != material) continue;
					
					String expectedName = ChatColor.translateAlternateColorCodes('&', sectionEquipment.getString("name", ""));
					String actualName = itemEquipment.getItemMeta().getDisplayName();
					if (!expectedName.equals(actualName)) continue;
				}
			}
			
			if (this.updateHandler != null)
			{
				boolean authorized = this.updateHandler.postUpdate(event, section);
				if (!authorized) return;
			}
			
			/*------------------------------------*/
			
			quete.increment(id);
			int progress = quete.getProgress("quete_" + id);
			int required = section.getInt("count");

			this.itemQuete.setLores(ItemLore.update(this.itemQuete, queteConfiguration));
			this.itemQuete.build();
			player.getInventory().setItem(slot, this.itemQuete.getItemStack());
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
	
	private <T extends Enum<T>> T safeValueOf(Class<T> clazz, String name)
	{
		try { return Enum.valueOf(clazz, name); }
		catch (Exception exeption) { return null; }
	}
	
}
