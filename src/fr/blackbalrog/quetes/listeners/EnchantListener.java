package fr.blackbalrog.quetes.listeners;

import fr.blackbalrog.quetes.api.handler.UpdateHandler;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import fr.blackbalrog.quetes.api.handler.QueteHandler;
import fr.blackbalrog.quetes.api.handler.QueteRegisters;

public class EnchantListener implements Listener, QueteHandler<EnchantItemEvent>, UpdateHandler<EnchantItemEvent>
{
	
	@Override
	public boolean supports(Event event)
	{
		return event instanceof EnchantItemEvent;
	}
	
	@Override
	public Player getPlayer(EnchantItemEvent event)
	{
		return event.getEnchanter();
	}

	@Override
	public Material getMaterial(EnchantItemEvent event)
	{
		return event.getItem().getType();
	}

	@Override
	public EntityType getEntityType(EnchantItemEvent event)
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
		if (event.isCancelled()) return;
		QueteRegisters.register(event, this,this);
	}
	
	@Override
	public boolean postUpdate(EnchantItemEvent event, ConfigurationSection section) {return true;}
	
	@Override
	public void preUpdate(EnchantItemEvent event, ConfigurationSection section)
	{
		event.getItem().setType(section.getBoolean("dropItem")  ? event.getItem().getType() : Material.AIR);
	}
}
