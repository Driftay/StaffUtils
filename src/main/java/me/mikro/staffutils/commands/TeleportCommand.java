package me.mikro.staffutils.commands;

import me.mikro.staffutils.Main;
import me.mikro.staffutils.utils.FolderYamlFile;
import me.mikro.staffutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class TeleportCommand implements CommandExecutor {

    private Main plugin;

    public TeleportCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("tp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command may only be executed as a player");
            return true;
        }
        if (!(sender.hasPermission("staffutils.tp"))) {
            sender.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
            return true;
        }

        if (args.length < 1 || args.length > 2) {
            sender.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("tp.usage")));
            return true;
        }

        if (args.length == 1) {
            Player player = (Player) sender;
            OfflinePlayer target = getOfflinePlayer(args[0]);

            if (target == null) {
                player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("tp.nullplayer")));
                return true;
            }

            if (target.isOnline()) {
                Player target2 = (Player) target;
                player.teleport(target2.getLocation());

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("staffutils.tp.notify")) {
                        p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("tp.notify_message").replace("%executor%", player.getName()).replace("%target%", target2.getName())));
                    }
                }
                return true;
            }

            if (!(new File(plugin.getDataFolder().getPath() + "/players/" + target.getUniqueId() + ".yml").exists())) {
                player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("tp.playedbefore_before_plugin").replaceAll("%target%", target.getName())));
                return true;
            }
            if (player.hasPermission("staffutils.tp.offlinetp")) {
                FolderYamlFile targetYamlFile = new FolderYamlFile("/players/" + target.getUniqueId().toString(), plugin);

                String w = targetYamlFile.getConfig().getString("location.world");
                World world = Bukkit.getWorld(w);
                int x = targetYamlFile.getConfig().getInt("location.x");
                int y = targetYamlFile.getConfig().getInt("location.y");
                int z = targetYamlFile.getConfig().getInt("location.z");
                Location location = new Location(world, x, y, z);

                player.teleport(location);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("staffutils.tp.notify")) {
                        p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("tp.notify_message_offline").replace("%executor%", player.getName()).replace("%target%", target.getName())));
                    }
                }
            }
            return true;
        }

        if (args.length == 2) {
            if (!sender.hasPermission("staffutils.tp.others")) {
                sender.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("tp.noperm")));
                return true;
            }
            Player player = (Player) sender;
            Player target1 = Bukkit.getServer().getPlayer(args[0]);
            Player target2 = Bukkit.getServer().getPlayer(args[1]);
            target1.teleport(target2);

            if (target1 == null || target2 == null) {
                sender.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("tp.nullplayer")));
                return true;
            }

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("staffutils.tp.notify")) {
                    p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("tp.notify_message_others").replace("%executor%", player.getName()).replace("%target%", target1.getName()).replace("%target2%", target2.getName())));
                }
            }
            return true;
        }

        return false;
    }

    public OfflinePlayer getOfflinePlayer(String name) {
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (player.getName().equals(name)) return player;
        }
        return null;
    }
}
