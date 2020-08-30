package me.mikro.staffutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mikro.staffutils.Main;
import me.mikro.staffutils.utils.Utils;

public class StaffChatCommand implements CommandExecutor {

	private Main plugin;
	
	public StaffChatCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("staffchat").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender.hasPermission("staffutils.staffchat"))) {
			sender.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
			return true;
		}
		
		if (args.length < 1) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Please use /sc <message> when using the console for staffchat");
				return true;
			}
			Player player = (Player) sender;
			if (plugin.staffchat.contains(player.getUniqueId())) {
				player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFCHAT.disabled")));
				plugin.staffchat.remove(player.getUniqueId());
				return true;
			}
			player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFCHAT.enabled")));
			plugin.staffchat.add(player.getUniqueId());
			return true;
		}
		String message = String.join(" ", args);
		if (!(sender instanceof Player)) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission("staffutils.staffchat") && plugin.data.get(p.getUniqueId()).getConfig().getBoolean("staff.staffchat_view") == true) {
					p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFCHAT.format").replace("%player%", Utils.chat("&cCONSOLE"))).replace("%message%", message));
				}
			}
			return true;
		}
		Player player = (Player) sender;
		if (plugin.data.get(player.getUniqueId()).getConfig().getBoolean("staff.staffchat_view") == false) {
			player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFCHAT.currently_disabled")));
			return true;
		}
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission("staffutils.staffchat") && plugin.data.get(p.getUniqueId()).getConfig().getBoolean("staff.staffchat_view") == true) {
					p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFCHAT.format").replace("%player%", sender.getName()).replace("%message%", message)));
					return true;
				}
			}
		return false;
	}

}
