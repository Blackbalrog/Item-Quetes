package fr.blackbalrog.quetes.listeners;

import fr.blackbalrog.quetes.handler.UpdateHandler;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import fr.blackbalrog.quetes.handler.QueteHandler;
import fr.blackbalrog.quetes.handler.QueteRegisters;

public class FishingListener implements Listener, QueteHandler<PlayerFishEvent>, UpdateHandler<PlayerFishEvent>
{
	
	@Override
	public boolean supports(Event event)
	{
		return event instanceof PlayerFishEvent 
				&& ((PlayerFishEvent) event).getState() == PlayerFishEvent.State.CAUGHT_FISH 
				&& ((PlayerFishEvent) event).getCaught() instanceof Item;
	}

	@Override
	public Player getPlayer(PlayerFishEvent event)
	{
		return event.getPlayer();
	}

	@Override
	public Material getMaterial(PlayerFishEvent event)
	{
		Entity caught = event.getCaught();
		if (!(caught instanceof Item item)) return null;

		Material material = item.getItemStack().getType();
		return (material == Material.AIR) ? null : material;
	}

	@Override
	public EntityType getEntityType(PlayerFishEvent event)
	{
		Entity caught = event.getCaught();
		if (!(caught instanceof Item)) return null;

		ItemStack caughtItemStack = ((Item) caught).getItemStack();
		return this.convertFishType(caughtItemStack);
		
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
	
	@EventHandler
	public void onFishing(PlayerFishEvent event)
	{
		QueteRegisters.register(event, this,this);
	}
	
	@Override
	public boolean postUpdate(PlayerFishEvent event, ConfigurationSection section) {return true;}
	
	@Override
	public void preUpdate(PlayerFishEvent event, ConfigurationSection section)
	{
		if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH
				&& event.getCaught() != null
				&& !section.getBoolean("dropItem"))
		{
			event.getCaught().remove();
		}
	}
}
