package fr.blackbalrog.quetes.listeners;

import fr.blackbalrog.quetes.handler.UpdateHandler;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import fr.blackbalrog.quetes.handler.QueteHandler;
import fr.blackbalrog.quetes.handler.QueteRegisters;

public class KillListeners implements Listener, QueteHandler, UpdateHandler
{
	@Override
	public boolean supports(Event event)
	{
		return event instanceof EntityDeathEvent;
	}


	@Override
	public Player getPlayer(Event event)
	{
		return ((EntityDeathEvent) event).getEntity().getKiller();
	}


	@Override
	public Material getMaterial(Event event)
	{
		return null;
	}


	@Override
	public EntityType getEntityType(Event event)
	{
		return ((EntityDeathEvent) event).getEntityType();
	}


	@Override
	public String getEventType()
	{
		return "KILL";
	}
	
	@EventHandler
	public void onKill(EntityDeathEvent event)
	{
		if (!(event.getEntity().getKiller() instanceof Player)) return;
		
		QueteRegisters.register(event, this,this);
	}
	
	@Override
	public void postUpdate(Event event, ConfigurationSection section)
	{
		EntityDeathEvent deathEvent = (EntityDeathEvent) event;
		if (section.contains("age"))
		{
			if (deathEvent.getEntity() instanceof Ageable ageable)
			{
				String filter = section.getConfigurationSection("age").getString("filter");
				boolean isAdult = ageable.isAdult();
				if ((filter.equalsIgnoreCase("ADULTE") && !isAdult) || (filter.equalsIgnoreCase("BABY") && isAdult)) return;
			}
		}
		else if (section.contains("customName"))
		{
			String customName = deathEvent.getEntity().getCustomName();
			String expectedName = section.getString("customName").replaceAll("&", "§");
			if (customName == null || !customName.equals(expectedName)) return;
		}
	}
	
	@Override
	public void preUpdate(Event event, ConfigurationSection section)
	{
		if (event instanceof EntityDeathEvent && section.getBoolean("dropItem"))
			((EntityDeathEvent) event).getDrops().clear();
	}
}
