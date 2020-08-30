package me.mikro.staffutils.listeners;

import me.mikro.staffutils.Main;
import me.mikro.staffutils.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private Main plugin;

    public DamageListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHitListener(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.frozen.contains(player.getUniqueId())) {
                if (event.getDamager() instanceof Player) {
                    Player damager = (Player) event.getDamager();
                    event.setCancelled(true);
                    damager.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.player_currently_frozen").replace("%player%", player.getName())));
                }
            }
        }
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (plugin.staffmode.contains(player.getUniqueId()) || plugin.frozen.contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
            if (plugin.getConfig().getBoolean("vanish.damage_others") == false && plugin.hiddenplayers.contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamageListener(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.frozen.contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}
