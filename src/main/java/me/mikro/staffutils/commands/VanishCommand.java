package me.mikro.staffutils.commands;

import me.mikro.staffutils.Main;
import me.mikro.staffutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    private Main plugin;

    public VanishCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("vanish").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to execute this command");
            return true;
        }
        if (!(sender.hasPermission("staffutils.vanish"))) {
            sender.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
            return true;
        }

        Player player = (Player) sender;

        if (plugin.hiddenplayers.contains(player.getUniqueId())) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(player);
                if (p.hasPermission("staffutils.vanish.notify")) {
                    if (plugin.getConfig().getBoolean("vanish.notify") == true) {
                        p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("vanish.notify.unvanish").replace("<executor>", player.getName())));
                    } else {
                        p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("vanish.unvanish")));
                    }
                }
            }

            if (!(player.hasPermission("staffutils.vanish.notify"))) {
                player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("vanish.unvanish")));
            }
            plugin.hiddenplayers.remove(player.getUniqueId());
            return true;
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.hasPermission("staffutils.vanish.see") || !plugin.data.get(p.getUniqueId()).getConfig().getBoolean("staff.vanish_view_others")) {
                    p.hidePlayer(player);
                }
                if (p.hasPermission("staffutils.vanish.notify")) {
                    if (plugin.getConfig().getBoolean("vanish.notify") == true) {
                        p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("vanish.notify.vanish").replace("<executor>", player.getName())));
                    } else {
                        p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("vanish.vanish")));
                    }
                }
            }
            if (!(player.hasPermission("staffutils.vanish.notify"))) {
                player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("vanish.vanish")));
            }
            plugin.hiddenplayers.add(player.getUniqueId());
        }
        return false;
    }
}
