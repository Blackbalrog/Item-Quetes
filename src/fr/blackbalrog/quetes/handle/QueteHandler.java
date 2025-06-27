package fr.blackbalrog.quetes.handle;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public interface QueteHandler
{
	/**
	 * @apiNote Vérification si l'event est supporter
	 */
	public boolean supports(Event event);
	
	/**
	 * @apiNote Récupère le joueur lié à l'event
	 */
	public Player getPlayer(Event event);
	
	/**
	 * @apiNote Récupère le material de l'event
	 */
	public Material getMaterial(Event event);
	
	/**
	 * @apiNote Récupère le type d'entité de l'event
	 */
	public EntityType getEntityType(Event event);
	
	/**
	 * @apiNote Lié à la configuration du parchemin = Clé "event"
	 */
	public String getEventType();
}
