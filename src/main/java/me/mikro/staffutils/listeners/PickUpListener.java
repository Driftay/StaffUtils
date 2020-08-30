package me.mikro.staffutils.listeners;

import me.mikro.staffutils.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PickUpListener implements Listener {

    private Main plugin;

    public PickUpListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void onPlayerPickUpEvent(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if (plugin.staffmode.contains(player.getUniqueId()) || plugin.frozen.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }

        if (plugin.getConfig().getBoolean("vanish.pickupitems") == false) {
            if (plugin.hiddenplayers.contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}
