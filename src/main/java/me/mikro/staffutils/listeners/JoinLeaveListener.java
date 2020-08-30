package me.mikro.staffutils.listeners;

import me.mikro.staffutils.Main;
import me.mikro.staffutils.utils.FolderYamlFile;
import me.mikro.staffutils.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class JoinLeaveListener implements Listener {

    private Main plugin;

    public JoinLeaveListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (UUID uuid : plugin.hiddenplayers) {
            Player toHide = Bukkit.getPlayer(uuid);
            if (!player.hasPermission("staffutils.vanish.see") || !plugin.data.get(player.getUniqueId()).getConfig().getBoolean("staff.vanish_view_others")) {
                player.hidePlayer(toHide);
            }
        }
        if (player.isOp() && plugin.update_available) {
            player.sendMessage(Utils.chat("&4[&cSTAFFUTILS&4] &cCurrently running on &4" + plugin.getDescription().getVersion() + " &cversion &4" + plugin.update_version + " &cis out"));
            player.sendMessage(Utils.chat("&4[&cSTAFFUTILS&4] &cUpdate StaffUtils at &6https://www.spigotmc.org/resources/staffutils.81847/"));
        }

        if (plugin.getConfig().getBoolean("freeze.blindness") == true) {
            player.removePotionEffect(PotionEffectType.BLINDNESS);
        }
        plugin.staffplaytime.put(player.getUniqueId(), System.currentTimeMillis());

        plugin.data.put(player.getUniqueId(), new FolderYamlFile("players/" + player.getUniqueId().toString(), plugin));

        plugin.data.get(player.getUniqueId()).getConfig().set("last name", player.getName());
        plugin.data.get(player.getUniqueId()).getConfig().set("location.world", player.getWorld().getName());
        plugin.data.get(player.getUniqueId()).getConfig().set("location.x", player.getLocation().getBlockX());
        plugin.data.get(player.getUniqueId()).getConfig().set("location.y", player.getLocation().getBlockY());
        plugin.data.get(player.getUniqueId()).getConfig().set("location.z", player.getLocation().getBlockZ());

        if (player.hasPermission("staffutils.staffmember")) {
            if (plugin.data.get(player.getUniqueId()).getConfig().getBoolean("staff.staffmode_on_join") == true) {
                player.performCommand("staffmode");
            }
            if (plugin.data.get(player.getUniqueId()).getConfig().getBoolean("staff.staffchat_on_join") == true) {
                player.performCommand("staffchat");
            }
        }

        if (player.hasPermission("staffutils.staffmember")) {
            if (!(plugin.data.get(player.getUniqueId()).getConfig().isSet("staff.staffmode_on_join"))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffmode_on_join", false);
            }
            if (!(plugin.data.get(player.getUniqueId()).getConfig().isSet("staff.staffchat_on_join"))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffchat_on_join", false);
            }
            if (!(plugin.data.get(player.getUniqueId()).getConfig().isSet("staff.staffchat_view"))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffchat_view", true);
            }
            if (!(plugin.data.get(player.getUniqueId()).getConfig().isSet("staff.vanish_view_others"))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.vanish_view_others", true);
            }
        } else if (plugin.data.get(player.getUniqueId()).getConfig().isConfigurationSection("staff")) {
            plugin.data.get(player.getUniqueId()).getConfig().set("staff", null);
        }
        plugin.data.get(player.getUniqueId()).save();
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (UUID uuid : plugin.hiddenplayers) {
            Player toShow = Bukkit.getPlayer(uuid);
            player.showPlayer(toShow);
        }
        plugin.hiddenplayers.remove(player.getUniqueId());

        if (plugin.frozen.contains(player.getUniqueId())) {
            TextComponent frozenmessage = new TextComponent(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.logout_message").replace("%player%", player.getName())));
            TextComponent clickban = new TextComponent(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.clicktoban_message")));
            clickban.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.hovertext"))).create()));
            clickban.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getConfig().getString("freeze.click_ban_suggested_command").replace("%player%", player.getName())));
            frozenmessage.addExtra(clickban);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("staffutils.freeze.clickban")) {
                    p.spigot().sendMessage(frozenmessage);
                }
            }
            plugin.frozen.remove(player.getUniqueId());
            if (plugin.getConfig().getBoolean("freeze.blindness") == true) {
                player.removePotionEffect(PotionEffectType.BLINDNESS);
            }
        }

        plugin.staffchat.remove(player.getUniqueId());

        if (plugin.requestmessage.containsKey(player.getUniqueId())) {
            plugin.requestmessage.remove(player.getUniqueId());
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("staffutils.request.reply")) {
                    p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("REQUEST.player_leave").replace("%player%", player.getName())));
                }
            }
        }

        if (plugin.staffmode.contains(player.getUniqueId())) {
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().setContents(plugin.inventory.get(player.getUniqueId()));
            player.getInventory().setArmorContents(plugin.invarmor.get(player.getUniqueId()));
            player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFMODE.switch_off")));

            plugin.inventory.remove(player.getUniqueId());
            plugin.invarmor.remove(player.getUniqueId());
            plugin.staffmode.remove(player.getUniqueId());
        }

        plugin.data.get(player.getUniqueId()).getConfig().set("last name", player.getName());
        plugin.data.get(player.getUniqueId()).getConfig().set("location.world", player.getWorld().getName());
        plugin.data.get(player.getUniqueId()).getConfig().set("location.x", player.getLocation().getBlockX());
        plugin.data.get(player.getUniqueId()).getConfig().set("location.y", player.getLocation().getBlockY());
        plugin.data.get(player.getUniqueId()).getConfig().set("location.z", player.getLocation().getBlockZ());

        if (player.hasPermission("staffutils.staffmember")) {
            if (!(plugin.data.get(player.getUniqueId()).getConfig().isSet("staff.staffmode_on_join"))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffmode_on_join", false);
            }
            if (!(plugin.data.get(player.getUniqueId()).getConfig().isSet("staff.staffchat_on_join"))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffchat_on_join", false);
            }
            if (!(plugin.data.get(player.getUniqueId()).getConfig().isSet("staff.staffchat_view"))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffchat_view", true);
            }
            if (!(plugin.data.get(player.getUniqueId()).getConfig().isSet("staff.vanish_view_others"))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.vanish_view_others", true);
            }
        } else if (plugin.data.get(player.getUniqueId()).getConfig().isConfigurationSection("staff")) {
            plugin.data.get(player.getUniqueId()).getConfig().set("staff", null);
        }
        plugin.data.get(player.getUniqueId()).save();
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent event) {
        Player player = event.getPlayer();
        for (UUID uuid : plugin.hiddenplayers) {
            Player toShow = Bukkit.getPlayer(uuid);
            player.showPlayer(toShow);
        }
        plugin.hiddenplayers.remove(player.getUniqueId());

        if (plugin.frozen.contains(player.getUniqueId())) {
            TextComponent frozenmessage = new TextComponent(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.logout_message").replace("%player%", player.getName())));
            TextComponent clickban = new TextComponent(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.clicktoban_message")));
            clickban.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.hovertext"))).create()));
            clickban.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getConfig().getString("freeze.click_ban_suggested_command").replace("%player%", player.getName())));
            frozenmessage.addExtra(clickban);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("staffutils.freeze.clickban")) {
                    p.spigot().sendMessage(frozenmessage);
                }
            }
            plugin.frozen.remove(player.getUniqueId());
            player.removePotionEffect(PotionEffectType.BLINDNESS);
        }
        if (plugin.requestmessage.containsKey(player.getUniqueId())) {
            plugin.requestmessage.remove(player.getUniqueId());
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("staffutils.request.reply")) {
                    p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("REQUEST.player_leave").replace("%player%", player.getName())));
                }
            }
        }

        plugin.staffchat.remove(player.getUniqueId());

        if (plugin.staffmode.contains(player.getUniqueId())) {
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().setContents(plugin.inventory.get(player.getUniqueId()));
            player.getInventory().setArmorContents(plugin.invarmor.get(player.getUniqueId()));
            player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFMODE.switch_off")));

            plugin.inventory.remove(player.getUniqueId());
            plugin.invarmor.remove(player.getUniqueId());
            plugin.staffmode.remove(player.getUniqueId());
        }

        plugin.data.get(player.getUniqueId()).getConfig().set("last name", player.getName());
        plugin.data.get(player.getUniqueId()).getConfig().set("location.world", player.getWorld().getName());
        plugin.data.get(player.getUniqueId()).getConfig().set("location.x", player.getLocation().getBlockX());
        plugin.data.get(player.getUniqueId()).getConfig().set("location.y", player.getLocation().getBlockY());
        plugin.data.get(player.getUniqueId()).getConfig().set("location.z", player.getLocation().getBlockZ());

        if (player.hasPermission("staffutils.staffmember")) {
            if (!(plugin.data.get(player.getUniqueId()).getConfig().isSet("staff.staffmode_on_join"))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffmode_on_join", false);
            }
            if (!(plugin.data.get(player.getUniqueId()).getConfig().isSet("staff.staffchat_on_join"))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffchat_on_join", false);
            }
            if (!(plugin.data.get(player.getUniqueId()).getConfig().isSet("staff.staffchat_view"))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffchat_view", true);
            }
            if (!(plugin.data.get(player.getUniqueId()).getConfig().isSet("staff.vanish_view_others"))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.vanish_view_others", true);
            }
        } else if (plugin.data.get(player.getUniqueId()).getConfig().isConfigurationSection("staff")) {
            plugin.data.get(player.getUniqueId()).getConfig().set("staff", null);
        }

        plugin.data.get(player.getUniqueId()).save();
    }
}
