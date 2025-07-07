package fr.blackbalrog.quetes.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;

public class DeathLog4jFilter extends AbstractFilter
{

	@Override
	public Result filter(LogEvent event)
	{
		return event.getMessage().getFormattedMessage().contains("died:") ? Result.DENY : Result.NEUTRAL;
	}

	public static void apply()
	{
		Logger logger = (Logger) LogManager.getRootLogger();
		logger.addFilter(new DeathLog4jFilter());
	}
}