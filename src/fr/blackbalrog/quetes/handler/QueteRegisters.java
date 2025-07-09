package fr.blackbalrog.quetes.handler;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import fr.blackbalrog.quetes.Quetes;
import fr.blackbalrog.quetes.builder.ItemBuilder;
import fr.blackbalrog.quetes.files.QueteConfiguration;
import fr.blackbalrog.quetes.update.QueteUpdate;

public class QueteRegisters
{

	private static QueteConfiguration queteConfiguration = Quetes.getInstance().getQueteConfiguration();
	
	public static <E extends Event> void register(E rawEvent, QueteHandler<E> handler, UpdateHandler<E> updateHandler)
	{
		if (!handler.supports(rawEvent)) return;
		
		E event = handler.getEvent(rawEvent);
		
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

			String itemName = item.getItemMeta().getDisplayName();
			if (queteConfiguration.getFile(itemName) == null) return;
			FileConfiguration configuration = queteConfiguration.getConfiguration(itemName);
			
			String eventMaterialType = (material != null) ? material.name() : (entityType != null ? entityType.name() : null);
			if (eventMaterialType == null) continue;
			
			QueteUpdate update = new QueteUpdate(event, updateHandler, eventType, configuration, itemBuilder, eventMaterialType);
			update.update(player, slot);
		}
	}
}
