package fr.blackbalrog.quetes.listeners;

import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.blackbalrog.quetes.handle.QueteHandler;
import fr.blackbalrog.quetes.handle.QueteRegisters;

public class BreakListener implements Listener
{

	private QueteHandler handler = new QueteHandler()
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

			if (type == Material.WHEAT || type == Material.CARROTS || type == Material.POTATOES || type == Material.BEETROOTS || type == Material.NETHER_WART)
			{
				if (breakEvent.getBlock().getBlockData() instanceof Ageable ageable)
				{
					return ageable.getAge() == ageable.getMaximumAge() ? type : Material.AIR;
				}
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
	};

	@EventHandler
	public void onBreak(BlockBreakEvent event)
	{
		QueteRegisters.handle(event, handler);
	}
}
