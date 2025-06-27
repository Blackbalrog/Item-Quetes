package fr.blackbalrog.quetes.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import fr.blackbalrog.quetes.handle.QueteHandler;
import fr.blackbalrog.quetes.handle.QueteRegisters;

public class FishingListener implements Listener
{

	private final QueteHandler handler = new QueteHandler()
	{

		@Override
		public boolean supports(Event event)
		{
			return event instanceof PlayerFishEvent 
					&& ((PlayerFishEvent) event).getState() == PlayerFishEvent.State.CAUGHT_FISH 
					&& ((PlayerFishEvent) event).getCaught() instanceof Item;
		}

		@Override
		public Player getPlayer(Event event)
		{
			return ((PlayerFishEvent) event).getPlayer();
		}

		@Override
		public Material getMaterial(Event event)
		{
			return null;
		}

		@Override
		public EntityType getEntityType(Event event)
		{
			Entity caught = ((PlayerFishEvent) event).getCaught();
			if (!(caught instanceof Item)) return null;

			ItemStack caughtItemStack = ((Item) caught).getItemStack();
			return convertFishType(caughtItemStack);
		}
		
		@Override
		public String getEventType()
		{
			return "FISHING";
		}
		
		private EntityType convertFishType(ItemStack item)
		{
			Material material = item.getType();

			if (material == Material.COD) return EntityType.COD;
			if (material == Material.SALMON) return EntityType.SALMON;
			if (material == Material.TROPICAL_FISH) return EntityType.TROPICAL_FISH;
			if (material == Material.PUFFERFISH) return EntityType.PUFFERFISH;

			return null;
		}
	};
	
	@EventHandler
	public void onFishing(PlayerFishEvent event)
	{
		QueteRegisters.handle(event, handler);
	}
	
}
