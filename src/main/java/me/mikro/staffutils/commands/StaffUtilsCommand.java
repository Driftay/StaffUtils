package me.mikro.staffutils.commands;

import me.mikro.staffutils.Main;
import me.mikro.staffutils.utils.Utils;
import me.mikro.staffutils.utils.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StaffUtilsCommand implements CommandExecutor {

    private Main plugin;

    public StaffUtilsCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("staffutils").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender.hasPermission("staffutils.staffutils"))) {
            sender.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(Utils.chat("&4[&cSTAFFUTILS&4] &cCurrently running version &6") + plugin.getDescription().getVersion());
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(Utils.chat("&8&m&l------------------------------"));
            sender.sendMessage(Utils.chat("&4&l(&c&l!&4&l) &lStaffutils commands:"));
            sender.sendMessage(Utils.chat("&c/staffutils &l<help> <reload>"));
            sender.sendMessage(Utils.chat("&c/staffmode"));
            sender.sendMessage(Utils.chat("&c/staffchat"));
            sender.sendMessage(Utils.chat("&c/staffsettings"));
            sender.sendMessage(Utils.chat("&c/chatsettings"));
            sender.sendMessage(Utils.chat("&c/vanish"));
            sender.sendMessage(Utils.chat("&c/freeze &l<player>"));
            sender.sendMessage(Utils.chat("&c/tp &l<player> <target>"));
            if (!sender.hasPermission("staffutils.request.reply")) {
                sender.sendMessage(Utils.chat("&c/request &l<message>"));
            } else {
                sender.sendMessage(Utils.chat("&c/request &l<player> <message>"));
            }
            sender.sendMessage(Utils.chat("&8&m&l------------------------------"));
            return true;
        } if (args[0].contains("reload")) {
            if (!sender.hasPermission("staffutils.reload")) {
                sender.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
                return true;
            }
            plugin.reloadConfig();
            plugin.saveDefaultConfig();
            plugin.messagesFile = new YamlFile("messages", plugin);
            sender.sendMessage(Utils.chat("&4[&cSTAFFUTILS&4] &cReload complete of config + messages"));
            Bukkit.getConsoleSender().sendMessage(Utils.chat("&4[&cSTAFFUTILS&4] &cReload  complete of config + messages"));
        } else {
            sender.sendMessage(Utils.chat("&4[&cSTAFFUTILS&4] &cCurrently running version &6") + plugin.getDescription().getVersion());
        }
        return true;
    }
}
