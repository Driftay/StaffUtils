package me.mikro.staffutils.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.mikro.staffutils.Main;

public class DropListener implements Listener {
	
	private Main plugin;
	
	public DropListener(Main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void PlayerDropListener(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (plugin.staffmode.contains(player.getUniqueId())) {
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void ItemDropOnDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (plugin.staffmode.contains(player.getUniqueId())) {
			event.setKeepInventory(true);
			event.setKeepLevel(true);
		}
	}
}

