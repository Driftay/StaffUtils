package me.mikro.staffutils.commands;

import me.mikro.staffutils.Main;
import me.mikro.staffutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RequestCommand implements CommandExecutor {

    private Main plugin;

    public RequestCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("request").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command may only be executed as a player, why would you use it through the console?");
            return true;
        }
        Player player = (Player) sender;
        if (!(player.hasPermission("staffutils.request"))) {
            player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
            return true;
        }

        if (!(player.hasPermission("staffutils.request.reply"))) {
            if (args.length < 1) {
                player.sendMessage(Utils.chat(plugin.getConfig().getString("REQUEST.usage")));
                return true;
            }
            if (plugin.requestmessage.containsKey(player.getUniqueId())) {
                player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("REQUEST.active_reply")));
                return true;
            }
            String message = String.join(" ", args);
            for (String s : plugin.messagesFile.getConfig().getStringList("REQUEST.player_message")) {
                player.sendMessage(Utils.chat(s).replace("%message%", message));
            }
            plugin.requestmessage.put(player.getUniqueId(), message);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("staffutils.request.reply")) {
                    for (String string : plugin.messagesFile.getConfig().getStringList("REQUEST.message")) {
                        p.sendMessage(Utils.chat(string).replace("%player%", player.getName()).replace("%message%", message));
                    }

                }
            }
            return true;
        }
        if (args.length < 2) {
            player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("REQUEST.usage_staff")));
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("REQUEST.nullplayer")));
            return true;
        }
        if (!(plugin.requestmessage.containsKey(target.getUniqueId()))) {
            player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("REQUEST.no_request")));
            return true;
        }
        StringBuilder reply = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reply.append(args[i]).append(" ");
        }
        for (String string : plugin.messagesFile.getConfig().getStringList("REQUEST.reply_message_player")) {
            target.sendMessage(Utils.chat(string).replace("%reply%", reply.toString()).replace("%message%", plugin.requestmessage.get(target.getUniqueId())).replace("%replier%", player.getName()));
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("staffutils.request.reply")) {
                p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("REQUEST.answered").replace("%player%", player.getName())).replace("%target%", target.getName()));
            }
        }
        plugin.requestmessage.remove(target.getUniqueId());
        return false;
    }
}
