package fr.blackbalrog.quetes.listeners.interact;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.blackbalrog.quetes.Quetes;
import fr.blackbalrog.quetes.builder.ItemBuilder;
import fr.blackbalrog.quetes.configurations.DefaultConfiguration;
import fr.blackbalrog.quetes.files.QueteConfiguration;
import fr.blackbalrog.quetes.inventory.InventoryRewards;
import fr.blackbalrog.quetes.utils.InventoryPlayerUtils;

public class PlayerInteractInventoryListener implements Listener
{

	private String prefix = Quetes.getInstance().getPrefix();
	private DefaultConfiguration messages = Quetes.getInstance().getMessages();
	private QueteConfiguration queteConfiguration = Quetes.getInstance().getQueteConfiguration();
	
	@EventHandler
	public void onInteract(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();

		if (item == null || !item.hasItemMeta() || event.getClickedInventory() == null) return;

		if (!event.getClickedInventory().equals(player.getInventory())) return;
		
		String itemName = item.getItemMeta().getDisplayName();
		if (this.queteConfiguration.getFile(itemName) == null) return;
		
		FileConfiguration configuration = this.queteConfiguration.getConfiguration(itemName);
		
		if (item.getType().equals(Material.valueOf(configuration.getString("Item.material"))) && item.getItemMeta().getDisplayName().equals(configuration.getString("Item.name").replaceAll("&", "§")))
		{
			if (player.getGameMode() == GameMode.CREATIVE)
			{
				player.sendMessage(this.prefix + "§7Impossible d'intéragir avec l'item en §cCréatif");
				return;
			}
			
			if (event.isLeftClick())
			{
				event.setCancelled(true);
	
				List<String> lores = new ArrayList<>();
				lores.add("");
				lores.add("§7§nQuetes:");
	
				List<String> listQuetes = new ArrayList<>();
				List<String> loreFinal = new ArrayList<>();
				
				ItemBuilder itemBuilder = new ItemBuilder(item);
				
				if (itemBuilder.getBooleanTag("actived"))
				{
					listQuetes.clear();
					loreFinal.clear();
					
					int countQueteFinish = 0;
					
					for (String id : configuration.getConfigurationSection("Quetes").getKeys(false))
					{
						if (itemBuilder.getIntTag("quete_" + id) == configuration.getInt("Quetes." + id + ".count"))
						{
							countQueteFinish++;
						}
					}
					
					if (countQueteFinish == configuration.getConfigurationSection("Quetes").getKeys(false).size())
					{
						ConfigurationSection section = configuration.getConfigurationSection("Rewards");
						if (section.contains("Inventory"))
						{
							Bukkit.getScheduler().runTaskLater(Quetes.getInstance(), () -> {
								new InventoryRewards().openInventory(player, itemBuilder);
							}, 1L);
						}
						if (section.contains("Commands"))
						{
							if (InventoryPlayerUtils.inventoryisFull(player))
							{
								player.sendMessage(this.prefix + "§7Vôtre inventaire est plein");
								return;
							}
							
							Random random = new Random();
							for (String id : section.getConfigurationSection("Commands").getKeys(false))
							{
								ConfigurationSection sectionId = section.getConfigurationSection("Commands." + id);
								if (sectionId.contains("pourcent") && random.nextInt(100) + 1 > sectionId.getInt("pourcent")) continue;
								
								String command = sectionId.getString("command")
										.toLowerCase()
										.replace("%player%", player.getName());
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
							}
							InventoryPlayerUtils.removeItemQuete(player);
						}
						
					}
					else
					{
						player.sendMessage(this.prefix + "§7Vous n'avez pas terminer les quêtes");
					}
				}
				else
				{
					int queteIndex = 0;
					for (String id : configuration.getConfigurationSection("Quetes").getKeys(false))
					{
						ConfigurationSection section = configuration.getConfigurationSection("Quetes." + id);
						if (queteIndex == 0)
						{
							listQuetes.add("§a§l" + section.getString("name").replaceAll("&", "§") + ": §b0");
						}
						else
						{
							String quete_name = section.getString("name")
									.replaceAll("&", "§");
							String quetes_lock = this.messages.getString("quetes.lock")
									.replace("%quete_name%", quete_name)
									.replaceAll("&", "§");
							listQuetes.add(quetes_lock);
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
