package fr.blackbalrog.quetes.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.blackbalrog.quetes.Quetes;
import fr.blackbalrog.quetes.builder.ItemBuilder;

public class CommandQuetes implements CommandExecutor, TabCompleter
{

	private String prefix = Quetes.getInstance().getPrefix();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		
		if (!sender.hasPermission("quetes.command.give"))
		{
			sender.sendMessage(this.prefix + "§7Vous n'avez pas la permission");
			return false;
		}
		
		if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("reload"))
			{
				Quetes.getInstance().getConfigurationManager().reloadConfigurations();
				sender.sendMessage(this.prefix + "§7La configuration a été reload");
				return true;
			}
		}
		
		if (args.length == 4)
		{
			if (args[0].equalsIgnoreCase("give"))
			{
				Player target = Bukkit.getPlayer(args[1]);
				if (target == null)
				{
					sender.sendMessage(this.prefix + "§c" + args[1] + " §7n'est pas en ligne");
					return false;
				}
				
				File file = new File(Quetes.getInstance().getDataFolder(), "quetes/" + args[2] + ".yml");
				if (!file.exists())
				{
					sender.sendMessage(this.prefix + "§7Le parchemin §c" + args[2] + " §7n'éxiste pas");
					return false;
				}
				
				int count = Integer.parseInt(args[3]);
				
				FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
				
				ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(configuration.getString("Item.material")));
				itemBuilder.setBooleanTag("actived", false);
				itemBuilder.setName(configuration.getString("Item.name").replaceAll("&", "§"))
					.setAmount(count)
					.setLores(Arrays.asList("", 
							"§7Cliquez sur le parchemin pour activer §bles quêtes", 
							"", 
							"§eClique gauche: §7pour activer le parchemin",
							"§aClique droit: §7Pour déplacer l'item"))
					.build();
				
				target.getInventory().addItem(itemBuilder.getItemStack());
				target.sendMessage(this.prefix + "§7Vous avez reçu x" + count + " §7parchemin §b" + args[2]);
				return true;
			}
		
		}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
	{
		List<String> list = new ArrayList<>();
		if (args.length == 1)
		{
			list.add("reload");
			list.add("give");
		}
		
		else if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("give"))
			{
				Bukkit.getOnlinePlayers().forEach(player -> list.add(player.getName()));
			}
		}
		
		else if (args.length == 3)
		{
			for (File file : new File(Quetes.getInstance().getDataFolder(), "quetes").listFiles())
			{
				if (file == null)
				{
					sender.sendMessage(this.prefix + "§7Aucun parchemin n'éxiste");
					return new ArrayList<>();
				}
				list.add(file.getName().replace(".yml", ""));
			}
		}
		
		else if (args.length == 4)
		{
			try
			{
				for (int i = 1; i < 10; i++)
				{
					list.add(String.valueOf(i));
				}
			}
			catch(NumberFormatException exeption)
			{
				sender.sendMessage(this.prefix + "§7Seul un nombre est accepter");
			}
		
		}
		return list;
	}

}
