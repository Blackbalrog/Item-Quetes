package fr.blackbalrog.quetes.handler;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;

public interface UpdateHandler
{
	/**
	 *
	 * @param l'event correspond à l'évenement qu'éffectue le joueur
	 * @param la section correspond à la configuration de la quête
	 *
	 * @apiNote A utiliser si il y a besoin de faire des conditions avant l'update de la quête
	 */
	void postUpdate(Event event, ConfigurationSection section);
	
	/**
	 * @param l'event correspond à l'évenement qu'éffectue le joueur
	 * @param la section correspond à la configuration de la quête
	 *
	 * @apiNote A utiliser si il y a besoin de faire des conditions après l'update de la quête
	 */
	void preUpdate(Event event, ConfigurationSection section);
}
