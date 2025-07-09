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

public class KillListeners implements Listener, QueteHandler<EntityDeathEvent>, UpdateHandler<EntityDeathEvent>
{
	
	@Override
	public boolean supports(Event event)
	{
		return event instanceof EntityDeathEvent;
	}
	
	@Override
	public Player getPlayer(EntityDeathEvent event)
	{
		return event.getEntity().getKiller();
	}
	
	@Override
	public Material getMaterial(EntityDeathEvent event)
	{
		return null;
	}
	
	@Override
	public EntityType getEntityType(EntityDeathEvent event)
	{
		return event.getEntityType();
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
	public boolean postUpdate(EntityDeathEvent event, ConfigurationSection section)
	{
		if (section.contains("age"))
		{
			if (event.getEntity() instanceof Ageable ageable)
			{
				String filter = section.getConfigurationSection("age").getString("filter");
				boolean isAdult = ageable.isAdult();
				if ((filter.equalsIgnoreCase("ADULTE") && !isAdult)
						|| (filter.equalsIgnoreCase("BABY")  &&  isAdult))
				{
					return false;
				}
			}
		}
		
		if (section.contains("customName"))
		{
			String customName   = event.getEntity().getCustomName();
			String expectedName = section.getString("customName").replaceAll("&", "§");
			if (customName == null || !customName.equals(expectedName))
			{
				return false;
			}
			
		}
		return true;
	}
	
	@Override
	public void preUpdate(EntityDeathEvent event, ConfigurationSection section)
	{
		if (!section.getBoolean("dropItem"))
		{
			event.getDrops().clear();
		}
	}
}
