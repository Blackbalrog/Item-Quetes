package fr.blackbalrog.quetes.message;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class Console
{
	
	private ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	private String prefix;
	
	public Console(String prefix)
	{
		this.prefix = prefix;
	}
	
	public void setError(String message)
	{
		console.sendMessage(this.prefix + "§c" + message);
	}
	
	public void setMessage(String message)
	{
		console.sendMessage(this.prefix + "§r" + message);
	}
	
}
