package fr.blackbalrog.quetes.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import fr.blackbalrog.quetes.handle.QueteHandler;
import fr.blackbalrog.quetes.handle.QueteRegisters;

public class EnchantListener implements Listener
{

	private QueteHandler handler = new QueteHandler()
	{
		@Override
		public boolean supports(Event event)
		{
			return event instanceof EnchantItemEvent;
		}

		@Override
		public Player getPlayer(Event event)
		{
			return ((EnchantItemEvent) event).getEnchanter();
		}

		@Override
		public Material getMaterial(Event event)
		{
			return ((EnchantItemEvent) event).getItem().getType();
		}

		@Override
		public EntityType getEntityType(Event event)
		{
			return null;
		}
		
		@Override
		public String getEventType()
		{
			return "ENCHANT";
		}
	};

	@EventHandler
	public void onEnchant(EnchantItemEvent event)
	{
		QueteRegisters.handle(event, handler);
	}
}
