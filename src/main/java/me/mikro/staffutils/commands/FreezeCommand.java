package me.mikro.staffutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mikro.staffutils.Main;
import me.mikro.staffutils.utils.Utils;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FreezeCommand implements CommandExecutor {

	private Main plugin;
	
	public FreezeCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("freeze").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender.hasPermission("staffutils.freeze"))) {
			sender.sendMessage(plugin.messagesFile.getConfig().getString("noperm"));
			return true;
		}
		if (args.length != 1) {
			sender.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.usage")));
			return true;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.nullplayer")));
			return true;
		}
		
		if (plugin.frozen.contains(target.getUniqueId())) {
			if (plugin.getConfig().getBoolean("freeze.broadcast") == true) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.hasPermission("staffutils.freeze")) {
						p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.unfreeze_broadcast").replace("%player%", sender.getName()).replace("%target%", target.getName())));
					}
				}
			} else {
				sender.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.unfreeze").replace("%target%", target.getName())));
			}
			plugin.frozen.remove(target.getUniqueId());
			if (plugin.getConfig().getBoolean("freeze.blindness") == true) {
				target.removePotionEffect(PotionEffectType.BLINDNESS);
			}
			if (plugin.getConfig().getBoolean("freeze.slowness") == true) {
				target.removePotionEffect(PotionEffectType.SLOW);
			}
			if (!(plugin.messagesFile.getConfig().getString("FREEZE.unfreeze_message").equalsIgnoreCase("none"))) {
				target.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.unfreeze_message")));
			}
			return true;
		}
		if (plugin.getConfig().getBoolean("freeze.broadcast") == true) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission("staffutils.freeze")) {
					p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.freeze_broadcast").replace("%player%", sender.getName()).replace("%target%", target.getName())));
				}
			}
		} else {
			sender.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.freeze").replace("%target%", target.getName())));
		}
		plugin.frozen.add(target.getUniqueId());
		if (plugin.getConfig().getBoolean("freeze.blindness") == true) {
			target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000000, 1));
		}
		if (plugin.getConfig().getBoolean("freeze.slowness") == true) {
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, plugin.getConfig().getInt("freeze.slowness_amplifier")));
		}
		return false;
	}

}
