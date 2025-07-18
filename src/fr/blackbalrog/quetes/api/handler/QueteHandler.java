package fr.blackbalrog.quetes.api.handler;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public interface QueteHandler<E extends Event>
{
	
	/**
	 * @param event
	 * @return Appel de l'event
	 */
	default E getEvent(E event)
	{
		return event;
	}
	
	/**
	 * @apiNote Vérification si l'event est supporter
	 */
	boolean supports(Event event);
	
	/**
	 * @apiNote Récupère le joueur lié à l'event
	 */
	Player getPlayer(E event);
	
	/**
	 * @apiNote Récupère le material de l'event
	 */
	Material getMaterial(E event);
	
	/**
	 * @apiNote Récupère le type d'entité de l'event
	 */
	EntityType getEntityType(E event);
	
	/**
	 * @apiNote Lié à la configuration du parchemin = Clé "event"
	 */
	String getEventType();
}
