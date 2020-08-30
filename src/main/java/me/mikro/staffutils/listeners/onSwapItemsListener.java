package me.mikro.staffutils.listeners;

import me.mikro.staffutils.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class onSwapItemsListener implements Listener {

    private Main plugin;

    public onSwapItemsListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSwapItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (plugin.staffmode.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
