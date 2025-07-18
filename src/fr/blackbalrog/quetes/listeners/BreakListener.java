package fr.blackbalrog.quetes.listeners;

import fr.blackbalrog.quetes.api.handler.UpdateHandler;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.blackbalrog.quetes.api.handler.QueteHandler;
import fr.blackbalrog.quetes.api.handler.QueteRegisters;

public class BreakListener implements Listener, QueteHandler<BlockBreakEvent>, UpdateHandler<BlockBreakEvent>
{
	
	@Override
	public boolean supports(Event event)
	{
		return event instanceof BlockBreakEvent;
	}
	
	@Override
	public Player getPlayer(BlockBreakEvent event)
	{
		return event.getPlayer();
	}

	@Override
	public Material getMaterial(BlockBreakEvent event)
	{
		Material type = event.getBlock().getType();

		if (event.getBlock().getBlockData() instanceof Ageable ageable)
		{
			return ageable.getAge() == ageable.getMaximumAge() ? type : Material.AIR;
		}
		if (type == Material.SUGAR_CANE || type == Material.CACTUS)
		{
			return event.getBlock().getRelative(0, 1, 0).getType() == type ? type : Material.AIR;
		}
		return type;
	}

	@Override
	public EntityType getEntityType(BlockBreakEvent event)
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
		if (event.isCancelled()) return;
		QueteRegisters.register(event,this,this);
	}
	
	@Override
	public boolean postUpdate(BlockBreakEvent event, ConfigurationSection section)
	{
		return true;
	}
	
	@Override
	public void preUpdate(BlockBreakEvent event, ConfigurationSection section)
	{
		event.setDropItems(section.getBoolean("dropItem"));
	}
}
