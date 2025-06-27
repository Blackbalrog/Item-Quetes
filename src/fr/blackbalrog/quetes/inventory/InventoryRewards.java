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

	private String prefix = Quetes.getInstance().getPrefix();
	
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
	public void onInventoryClick(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();

		if (!event.getView().getTitle().equals("Récompenses")) return;

		if (event.getClickedInventory() != null && event.getClickedInventory().equals(event.getView().getTopInventory()))
		{
			if (event.getAction().toString().contains("PLACE") || event.getAction().toString().contains("SWAP"))
			{
				event.setCancelled(true);
			}
		}

		Bukkit.getScheduler().runTaskLater(Quetes.getInstance(), () -> {
			Inventory inv = event.getView().getTopInventory();
			boolean isEmpty = true;

			for (ItemStack item : inv.getContents())
			{
				if (item != null && item.getType() != Material.AIR)
				{
					isEmpty = false;
					break;
				}
			}

			if (isEmpty)
			{
				for (int i = 0; i < player.getInventory().getSize(); i++)
				{
					ItemStack item = player.getInventory().getItem(i);
					if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName())
					{
						String name = item.getItemMeta().getDisplayName().replaceAll("§.", "");
						File file = new File(Quetes.getInstance().getDataFolder(), "quetes/" + name + ".yml");
						if (file.exists())
						{
							player.getInventory().setItem(i, null);
							break;
						}
					}
				}
				player.closeInventory();
				player.sendMessage(this.prefix + "§7Tu as récupéré toutes tes récompenses !");
			}
		}, 1L);
	}
}
