package fr.blackbalrog.quetes.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import fr.blackbalrog.quetes.handle.QueteHandler;
import fr.blackbalrog.quetes.handle.QueteRegisters;

public class KillListeners implements Listener
{

	private QueteHandler handler = new QueteHandler()
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
	};
	
	
	@EventHandler
	public void onKill(EntityDeathEvent event)
	{
		if (!(event.getEntity().getKiller() instanceof Player)) return;
		
		QueteRegisters.handle(event, handler);
	}
}
