package fr.blackbalrog.quetes.handle;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import fr.blackbalrog.quetes.Quetes;
import fr.blackbalrog.quetes.builder.ItemBuilder;
import fr.blackbalrog.quetes.update.QueteUpdate;

public class QueteRegisters
{

	public static void handle(Event event, QueteHandler handler)
	{
		if (!handler.supports(event)) return;

		Player player = handler.getPlayer(event);
		Material material = handler.getMaterial(event);
		EntityType entityType = handler.getEntityType(event);
		String eventType = handler.getEventType();

		if (player == null) return;

		for (int slot = 0; slot < player.getInventory().getSize(); slot++)
		{
			ItemStack item = player.getInventory().getItem(slot);
			if (item == null || !item.hasItemMeta()) continue;

			ItemBuilder itemBuilder = new ItemBuilder(item);
			if (!itemBuilder.getBooleanTag("actived")) continue;

			File file = new File(Quetes.getInstance().getDataFolder(), "quetes/" + item.getItemMeta().getDisplayName().replaceAll("§.", "") + ".yml");
			if (!file.exists()) continue;

			FileConfiguration config = YamlConfiguration.loadConfiguration(file);

			QueteUpdate.update(player, eventType, config, itemBuilder, (material != null ? material.name() : entityType.name()), slot, event);
		}
	}
}
