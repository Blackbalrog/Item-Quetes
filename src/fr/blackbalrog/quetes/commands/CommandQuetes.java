package fr.blackbalrog.quetes.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.blackbalrog.quetes.Quetes;
import fr.blackbalrog.quetes.builder.ItemBuilder;
import fr.blackbalrog.quetes.message.Console;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandQuetes implements CommandExecutor, TabCompleter
{

	private Console console = Quetes.getInstance().getConsole();
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
				Quetes.getInstance().getQueteConfiguration().reloadAll();
				Quetes.getInstance().getQueteConfiguration().init();
				sender.sendMessage(this.prefix + "§7Les configurations ont été recharger");
				return true;
			}
		}
		/*
		if (args.length == 3)
		{
			if (args[0].equalsIgnoreCase("give"))
			{
				ItemStack itemStack = new ItemStack(Material.valueOf(args[1]));
				ItemMeta meta = itemStack.getItemMeta();
				meta.setDisplayName(args[2].replaceAll("&", "§"));
				itemStack.setItemMeta(meta);
				((Player) sender).getInventory().addItem(itemStack);
				return true;
			}
		}
		*/
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
							"§aClique droit: §7Pour déplacer l'item"));
				
				int slot = 0;
				if (configuration.contains("Rewards.Inventory"))
				{
					Random random = new Random();
					
					for (String key : configuration.getConfigurationSection("Rewards.Inventory").getKeys(false))
					{
						ConfigurationSection section = configuration.getConfigurationSection("Rewards.Inventory." + key);
						
						String name = section.getString("name");
						
						if (name == null || name.isEmpty())
						{
							name = "default";
							this.console.setError("Le nom du slot Rewards '" + key + "' du fichier '" + configuration.getName() + ".yml' n'éxiste pas");
						}
						
						Material material = Material.matchMaterial(section.getString("material"));
						if (material == null)
						{
							material = Material.PINK_PETALS;
							this.console.setError("Le material du slot Rewards '" + key + "' du fichier '" + configuration.getName() + ".yml' n'éxiste pas");
						}
						
						int amount = section.getInt("count");
						if (amount <= -1)
						{
							amount = 1;
						}
						
						if (section.contains("pourcent"))
						{
							if (random.nextInt(100) + 1 > section.getInt("pourcent")) continue;
						}
						
						itemBuilder.setIntTag("reward_" + slot + "_slot", Integer.parseInt(key))
							.setStringTag("reward_" + slot + "_name", name)
							.setStringTag("reward_" + slot + "_material", material.name())
							.setIntTag("reward_" + slot + "_count", amount);
						slot++;
					}
				}
				itemBuilder.build();
				
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
