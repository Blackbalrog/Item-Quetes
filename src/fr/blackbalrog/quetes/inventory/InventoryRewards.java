package fr.blackbalrog.quetes.inventory;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.blackbalrog.quetes.Quetes;
import fr.blackbalrog.quetes.builder.ItemBuilder;

public class InventoryRewards implements Listener
{
	public void openInventory(Player player, FileConfiguration configuration)
	{
		ConfigurationSection rewardsSection = configuration.getConfigurationSection("Rewards");
		Inventory inventory = Bukkit.createInventory(null, 54, "Récompenses");

		for (String key : rewardsSection.getKeys(false))
		{
			ConfigurationSection section = rewardsSection.getConfigurationSection(key);

			ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(section.getString("material")))
					.setName(section.getString("name").replaceAll("&", "§"))
					.setAmount(section.getInt("count"));
			itemBuilder.build();

			inventory.setItem(Integer.parseInt(key), itemBuilder.getItemStack());
		}
		player.openInventory(inventory);
	}

	@EventHandler
	public void onInventoryClick2(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		
		if (event.getView().getTitle().equals("Récompenses"))
		{
			if (item == null || event.getClickedInventory() == null) return;
			event.setCancelled(true);
		
			player.getInventory().addItem(item);
			
			Inventory inventory = event.getInventory();
			inventory.remove(item);
			
			if (inventory.isEmpty())
			{
				for (int i = 0; i < player.getInventory().getSize(); i++)
				{
					ItemStack itemQuete = player.getInventory().getItem(i);
					if (itemQuete != null && itemQuete.hasItemMeta() && itemQuete.getItemMeta().hasDisplayName())
					{
						File file = new File(Quetes.getInstance().getDataFolder(), "quetes/" + itemQuete.getItemMeta().getDisplayName().replaceAll("§.", "") + ".yml");
						if (file.exists())
						{
							player.getInventory().setItem(i, null);
							break;
						}
					}
				}
			}
			player.closeInventory();
		}
	}
}
