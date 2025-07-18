package fr.blackbalrog.quetes.listeners;

import fr.blackbalrog.quetes.api.handler.QueteHandler;
import fr.blackbalrog.quetes.api.handler.QueteRegisters;
import fr.blackbalrog.quetes.api.handler.UpdateHandler;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class EntityPickupItemListener implements Listener, QueteHandler<EntityPickupItemEvent>, UpdateHandler<EntityPickupItemEvent>
{
	@Override
	public boolean supports(Event event)
	{
		return event instanceof EntityPickupItemEvent;
	}
	
	@Override
	public Player getPlayer(EntityPickupItemEvent event)
	{
		return (event.getEntity() instanceof Player) ? ((Player) event.getEntity()).getPlayer() : null;
	}
	
	@Override
	public Material getMaterial(EntityPickupItemEvent event)
	{
		return event.getItem().getItemStack().getType();
	}
	
	@Override
	public EntityType getEntityType(EntityPickupItemEvent event)
	{
		return null;
	}
	
	@Override
	public String getEventType()
	{
		return "PICKUP";
	}
	
	@EventHandler
	public void onPickup(EntityPickupItemEvent event)
	{
		if (event.isCancelled()) return;
		QueteRegisters.register(event, this,this);
	}
	
	@Override
	public boolean postUpdate(EntityPickupItemEvent event, ConfigurationSection section) {return true;}
	
	@Override
	public void preUpdate(EntityPickupItemEvent event, ConfigurationSection section) {}
}
