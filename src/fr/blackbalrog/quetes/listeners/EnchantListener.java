package fr.blackbalrog.quetes.listeners;

import fr.blackbalrog.quetes.handler.UpdateHandler;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import fr.blackbalrog.quetes.handler.QueteHandler;
import fr.blackbalrog.quetes.handler.QueteRegisters;
import org.bukkit.event.entity.EntityDeathEvent;

public class EnchantListener implements Listener, QueteHandler, UpdateHandler
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
	
	@EventHandler
	public void onEnchant(EnchantItemEvent event)
	{
		QueteRegisters.register(event, this,this);
	}
	
	@Override
	public void postUpdate(Event event, ConfigurationSection section)
	{
	
	}
	
	@Override
	public void preUpdate(Event event, ConfigurationSection section)
	{
		if (event instanceof EnchantItemEvent  && section.getBoolean("dropItem"))
			((EnchantItemEvent) event).getItem().setType(Material.AIR);
	}
}
