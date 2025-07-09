package fr.blackbalrog.quetes.handler;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;

public interface UpdateHandler<E extends Event>
{
	
	/**
	 *
	 * @param l'event correspond à l'évenement qu'éffectue le joueur
	 * @param la section correspond à la configuration de la quête
	 *
	 * @apiNote A utiliser si il y a besoin de faire des conditions avant l'update de la quête
	 */
	boolean postUpdate(E event, ConfigurationSection section);
	
	/**
	 * @param l'event correspond à l'évenement qu'éffectue le joueur
	 * @param la section correspond à la configuration de la quête
	 *
	 * @apiNote A utiliser si il y a besoin de faire de l'éxecution après l'update de la quête
	 */
	void preUpdate(E event, ConfigurationSection section);
}
