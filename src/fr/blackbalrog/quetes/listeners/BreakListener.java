package fr.blackbalrog.quetes.listeners;

import fr.blackbalrog.quetes.Quetes;
import fr.blackbalrog.quetes.handler.UpdateHandler;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.blackbalrog.quetes.handler.QueteHandler;
import fr.blackbalrog.quetes.handler.QueteRegisters;

public class BreakListener implements Listener, QueteHandler, UpdateHandler
{

	@Override
	public boolean supports(Event event)
	{
		return event instanceof BlockBreakEvent;
	}

	@Override
	public Player getPlayer(Event event)
	{
		return ((BlockBreakEvent) event).getPlayer();
	}

	@Override
	public Material getMaterial(Event event)
	{
		BlockBreakEvent breakEvent = (BlockBreakEvent) event;
		Material type = breakEvent.getBlock().getType();

		if (breakEvent.getBlock().getBlockData() instanceof Ageable ageable)
		{
			return ageable.getAge() == ageable.getMaximumAge() ? type : Material.AIR;
		}
		if (type == Material.SUGAR_CANE || type == Material.CACTUS)
		{
			return breakEvent.getBlock().getRelative(0, 1, 0).getType() == type ? type : Material.AIR;
		}
		return type;
	}

	@Override
	public EntityType getEntityType(Event event)
	{
		return null;
	}

	@Override
	public String getEventType()
	{
		return "BREAK";
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event)
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
		if (event instanceof BlockBreakEvent)
		{
			((BlockBreakEvent) event).setDropItems(section.getBoolean("dropItem"));
		}
	}
}
