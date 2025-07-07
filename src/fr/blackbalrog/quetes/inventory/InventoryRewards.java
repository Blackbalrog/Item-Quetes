package fr.blackbalrog.quetes.inventory;

import java.io.File;
import java.util.Optional;
import java.util.stream.IntStream;

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
import fr.blackbalrog.quetes.files.QueteConfiguration;
import fr.blackbalrog.quetes.utils.InventoryPlayerUtils;

public class InventoryRewards implements Listener
{
	
	private QueteConfiguration queteConfiguration = Quetes.getInstance().getQueteConfiguration();
	private String prefix = Quetes.getInstance().getPrefix();
	
	public void openInventory(Player player, ItemBuilder itemBuilder)
	{
		
		Inventory inventory = Bukkit.createInventory(null, 54, "Récompenses");
		
		FileConfiguration configuration = this.queteConfiguration.getConfiguration(itemBuilder.getName());
		ConfigurationSection rewardsSection = configuration.getConfigurationSection("Rewards.Inventory");
		
		for (String key : rewardsSection.getKeys(false))
		{
			String materialName = itemBuilder.getStringTag("reward_" + key + "_material");
			if (materialName == null) continue;

			Material material;
			try
			{
				material = Material.valueOf(materialName);
			}
			catch (IllegalArgumentException exeption)
			{
				continue;
			}
			
			String name = itemBuilder.getStringTag("reward_" + key + "_name");
			if (name == null) continue;
			
			int count = itemBuilder.getIntTag("reward_" + key + "_count");
			if (count <= 0) continue;
			
			ItemBuilder item = new ItemBuilder(material)
					.setName(name.replaceAll("&", "§"))
					.setAmount(count);
			item.build();
			inventory.setItem(itemBuilder.getIntTag("reward_" + key + "_slot"), item.getItemStack());
		}
		player.openInventory(inventory);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		
		if (event.getView().getTitle().equals("Récompenses"))
		{
			if (item == null || event.getClickedInventory() == null) return;
			event.setCancelled(true);
			
			if (event.getClickedInventory().equals(player.getInventory())) return;
			
			if (InventoryPlayerUtils.inventoryisFull(player))
			{
				player.sendMessage(this.prefix + "§7Vôtre inventaire est plein");
				return;
			}
			
			player.getInventory().addItem(item);
			
			Inventory inventory = event.getInventory();
			inventory.remove(item);
			
			ItemStack[] items = player.getInventory().getContents();
			
			Optional<ItemStack> optional = IntStream.range(0, items.length)
					.mapToObj(i -> items[i])
					.filter(itemQuete -> {
						if (itemQuete == null
								|| !itemQuete.hasItemMeta()
								|| !itemQuete.getItemMeta().hasDisplayName()) return false;
						
						File file = this.queteConfiguration.getFile(itemQuete.getItemMeta().getDisplayName());
						return file != null && file.exists();
					})
					.findFirst();

			ItemStack itemTagQuete = optional.orElse(null);
			if (itemTagQuete == null)
			{
				player.sendMessage(prefix + "§cImpossible de trouver l'item de quête");
				return;
			}
			
			int slot = event.getSlot();
			
			ItemBuilder itemBuilder = new ItemBuilder(itemTagQuete)
					.removeTag("reward_" + slot + "_name")
					.removeTag("reward_" + slot + "_material")
					.removeTag("reward_" + slot + "_count");
			itemBuilder.build();
			
			if (inventory.isEmpty()) InventoryPlayerUtils.removeItemQuete(player);
		}
	}
}
