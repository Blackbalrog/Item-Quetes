package fr.blackbalrog.quetes.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.blackbalrog.quetes.Quetes;
import fr.blackbalrog.quetes.builder.ItemBuilder;
import fr.blackbalrog.quetes.inventory.InventoryRewards;

public class PlayerInventoryInteractListener implements Listener
{

	private String prefix = Quetes.getInstance().getPrefix();

	@EventHandler
	public void onInteract(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();

		if (item == null || !item.hasItemMeta() || event.getClickedInventory() == null) return;

		if (!event.getClickedInventory().equals(player.getInventory())) return;

		File file = new File(Quetes.getInstance().getDataFolder(), "quetes/" + item.getItemMeta().getDisplayName().replaceAll("§.", "") + ".yml");
		if (!file.exists()) return;
		FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

		if (item.getType().equals(Material.valueOf(configuration.getString("Item.material"))) && item.getItemMeta().getDisplayName().equals(configuration.getString("Item.name").replaceAll("&", "§")))
		{
			if (event.getClick() == ClickType.LEFT)
			{
				event.setCancelled(true);
	
				ItemBuilder itemBuilder = new ItemBuilder(item);
	
				List<String> lores = new ArrayList<>();
				lores.add("");
				lores.add("§7§nQuetes:");
	
				List<String> listQuetes = new ArrayList<>();
				List<String> loreFinal = new ArrayList<>();
	
				if (itemBuilder.getBooleanTag("actived"))
				{
					listQuetes.clear();
					loreFinal.clear();
					boolean foundCurrent = false;
	
					for (String id : configuration.getConfigurationSection("Quetes").getKeys(false))
					{
						if (itemBuilder.getIntTag("quete_" + id) == configuration.getInt("Quetes." + id + ".count"))
						{
							new InventoryRewards().openInventory(player, configuration);
							return;
						}
						
						ConfigurationSection section = configuration.getConfigurationSection("Quetes." + id);
						int count = section.getInt("count");
						int progress = itemBuilder.getIntTag("quete_" + id);
						String name = section.getString("name").replaceAll("&", "§");
	
						if (progress >= count)
						{
							listQuetes.add("§7§m" + name + ": " + count);
							continue;
						}
	
						if (!foundCurrent)
						{
							foundCurrent = true;
	
							if (progress == count)
							{
								listQuetes.add("§7§m" + name + ": " + count);
	
								try
								{
									int nextId = Integer.parseInt(id) + 1;
									if (configuration.contains("Quetes." + nextId))
									{
										itemBuilder.setIntTag("quete_" + nextId, 0);
									}
								}
								catch (NumberFormatException exception)
								{
									exception.printStackTrace();
								}
							}
							else
							{
								listQuetes.add("§a§l" + name + ": §b" + progress);
							}
						}
						else
						{
							listQuetes.add("§7???");
						}
					}
	
					loreFinal.addAll(lores);
					loreFinal.addAll(listQuetes);
					loreFinal.add("");
					loreFinal.add("");
					loreFinal.add("§eClique gauche:");
					loreFinal.add("§7Une fois toutes les quêtes terminer,");
					loreFinal.add("§7Vous pourrez ouvrir l'inventaire des récompenses");
					loreFinal.add("");
					loreFinal.add("§aClique droit: §7Pour déplacer l'item");
	
					itemBuilder.setLores(loreFinal).build();
					event.getClickedInventory().setItem(event.getSlot(), itemBuilder.getItemStack());
					return;
				}
				else
				{
					int queteIndex = 0;
					for (String key : configuration.getConfigurationSection("Quetes").getKeys(false))
					{
						ConfigurationSection section = configuration.getConfigurationSection("Quetes." + key);
						if (queteIndex == 0)
						{
							String name = section.getString("name").replaceAll("&", "§");
							listQuetes.add("§a§l" + name + ": §b0");
						}
						else
						{
							listQuetes.add("§7???");
						}
						queteIndex++;
					}
	
					loreFinal.addAll(lores);
					loreFinal.addAll(listQuetes);
					loreFinal.add("");
					loreFinal.add("");
					loreFinal.add("§eClique gauche:");
					loreFinal.add("§7Une fois toutes les quêtes terminer,");
					loreFinal.add("§7Vous pourrez ouvrir l'inventaire des récompenses");
					loreFinal.add("");
					loreFinal.add("§aClique droit: §7Pour déplacer l'item");
	
					itemBuilder.setBooleanTag("actived", true);
	
					for (String id : configuration.getConfigurationSection("Quetes").getKeys(false))
					{
						itemBuilder.setIntTag("quete_" + id, 0);
					}
	
					itemBuilder.setName(item.getItemMeta().getDisplayName()).setLores(loreFinal).build();
	
					event.getClickedInventory().setItem(event.getSlot(), itemBuilder.getItemStack());
					player.sendMessage(this.prefix + "§7Activation du parchemin");
				}
			}
		}
	}
}
