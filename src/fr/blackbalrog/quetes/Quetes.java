package fr.blackbalrog.quetes;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.blackbalrog.quetes.commands.CommandQuetes;
import fr.blackbalrog.quetes.configurations.ConfigurationManager;
import fr.blackbalrog.quetes.configurations.DefaultConfiguration;
import fr.blackbalrog.quetes.inventory.InventoryRewards;
import fr.blackbalrog.quetes.listeners.BreakListener;
import fr.blackbalrog.quetes.listeners.EnchantListener;
import fr.blackbalrog.quetes.listeners.FishingListener;
import fr.blackbalrog.quetes.listeners.HarvestListener;
import fr.blackbalrog.quetes.listeners.KillListeners;
import fr.blackbalrog.quetes.listeners.PlayerInventoryInteractListener;
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
	@SuppressWarnings("unused")
	private DefaultConfiguration configurationDefault;
	private String prefix;
	private Console console;
	
	@Override
	public void onEnable()
	{
		instance = this;
		
		this.configurationManager = new ConfigurationManager();
		this.configurationManager.add(this.configuration = new DefaultConfiguration(this, "Configuration.yml"));
		this.configurationManager.add(this.configurationDefault = new DefaultConfiguration(this, "quetes/default.yml"));
		
		this.prefix = this.configuration.getString("Prefix").replaceAll("&", "§");
		this.console = new Console(this.prefix);
		
		this.onCommands();
		this.onListeners();
		
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
	
		pluginManager.registerEvents(new PlayerInventoryInteractListener(), this);
		pluginManager.registerEvents(new InventoryRewards(), this);
		
		pluginManager.registerEvents(new BreakListener(), this);
		pluginManager.registerEvents(new EnchantListener(), this);
		pluginManager.registerEvents(new FishingListener(), this);
		pluginManager.registerEvents(new HarvestListener(), this);
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
