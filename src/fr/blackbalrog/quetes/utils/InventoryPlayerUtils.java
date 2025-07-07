package fr.blackbalrog.quetes.utils;

import java.io.File;
import java.util.Optional;
import java.util.stream.IntStream;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.blackbalrog.quetes.Quetes;
import fr.blackbalrog.quetes.files.QueteConfiguration;

public class InventoryPlayerUtils
{
	
	private static QueteConfiguration queteConfiguration = Quetes.getInstance().getQueteConfiguration();
	
	public static boolean inventoryisFull(Player player)
	{
		return player.getInventory().firstEmpty() == -1;
	}
	
	public static void removeItemQuete(Player player)
	{
		ItemStack[] items = player.getInventory().getContents();
		IntStream.range(0, items.length)
				.filter(i -> {
					ItemStack item = items[i];
					if (item == null
							|| !item.hasItemMeta()
							|| !item.getItemMeta().hasDisplayName()) return false;
					
					File file = queteConfiguration.getFile(item.getItemMeta().getDisplayName());
					return file != null && file.exists();
				})
				.findFirst()
				.ifPresent(i -> {
					player.getInventory().setItem(i, null);
					player.closeInventory();
				});
	}

}
