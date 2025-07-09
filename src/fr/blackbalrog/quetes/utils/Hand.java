package fr.blackbalrog.quetes.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum Hand
{
	MAIN_HAND,
	OFF_HAND;
	
	public ItemStack getItem(Player player)
	{
		return this == MAIN_HAND
				? player.getInventory().getItemInMainHand()
				: player.getInventory().getItemInOffHand();
	}
}

