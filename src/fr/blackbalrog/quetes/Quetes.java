package fr.blackbalrog.quetes;

import fr.blackbalrog.quetes.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.blackbalrog.quetes.commands.CommandQuetes;
import fr.blackbalrog.quetes.configurations.ConfigurationManager;
import fr.blackbalrog.quetes.configurations.DefaultConfiguration;
import fr.blackbalrog.quetes.files.QueteConfiguration;
import fr.blackbalrog.quetes.inventory.InventoryRewards;
import fr.blackbalrog.quetes.listeners.interact.PlayerInteractInventoryListener;
import fr.blackbalrog.quetes.logger.DeathLog4jFilter;
import fr.blackbalrog.quetes.message.Console;

/**
 * @author Blackbalrog
 * @version 1.0.0
 */
public class Quetes extends JavaPlugin
{
	
	private static Quetes instance;
	private ConfigurationManager configurationManager;
	private DefaultConfiguration configuration;
	private DefaultConfiguration messages;
	protected DefaultConfiguration defaultConfiguration;
	private QueteConfiguration queteConfiguration;
	private String prefix;
	private Console console;
	
	@Override
	public void onEnable()
	{
		instance = this;
		
		this.configurationManager = new ConfigurationManager();
		this.configurationManager.add(this.configuration = new DefaultConfiguration(this, "Configuration.yml"));
		this.configurationManager.add(this.messages = new DefaultConfiguration(this, "Messages.yml"));
		// Default Load
		this.configurationManager.add(this.defaultConfiguration = new DefaultConfiguration(this, "quetes/default.yml"));
		
		this.queteConfiguration = new QueteConfiguration();
		this.queteConfiguration.init();
		
		this.prefix = this.configuration.getString("Prefix").replaceAll("&", "§");
		this.console = new Console(this.prefix);
		
		this.onCommands();
		this.onListeners();
		
		// Bloque le message de mort de l'entité
		DeathLog4jFilter.apply();
		
		this.console.setMessage("§aAllumer");
	}
	
	@Override
	public void onDisable()
	{
		this.console.setMessage("§cEteint");
	}

	private void onCommands()
	{
		this.getCommand("quetes").setExecutor(new CommandQuetes());
	}
	
	private void onListeners()
	{
		var pluginManager = Bukkit.getPluginManager();
	
		pluginManager.registerEvents(new PlayerInteractInventoryListener(), this);
		pluginManager.registerEvents(new InventoryRewards(), this);
		
		/**
		 * Voir pour faire un pluginManagerBuilder et faire une boucle for pour lister les listener enregistrer
		 * peut servire pour l'api
		 */
		
		pluginManager.registerEvents(new BreakListener(), this);
		pluginManager.registerEvents(new EnchantListener(), this);
		pluginManager.registerEvents(new FishingListener(), this);
		pluginManager.registerEvents(new EntityPickupItemListener(), this);
		pluginManager.registerEvents(new KillListeners(), this);
	}
	
	public ConfigurationManager getConfigurationManager()
	{
		return this.configurationManager;
	}
	
	public DefaultConfiguration getConfiguration()
	{
		return this.configuration;
	}
	
	public DefaultConfiguration getMessages()
	{
		return this.messages;
	}
	
	public QueteConfiguration getQueteConfiguration()
	{
		return this.queteConfiguration;
	}
	
	public String getPrefix()
	{
		return this.prefix;
	}
	
	public Console getConsole()
	{
		return this.console;
	}
	
	public static Quetes getInstance()
	{
		return instance;
	}
	
}
