package me.mikro.staffutils.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.mikro.staffutils.Main;

public class PlayerBlockPlace implements Listener {
	
	private Main plugin;
	
	public PlayerBlockPlace(Main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		
		if (plugin.staffmode.contains(player.getUniqueId()) || plugin.frozen.contains(player.getUniqueId())) {
			event.setCancelled(true);
		}
		if (plugin.getConfig().getBoolean("vanish.build_break") == false && plugin.hiddenplayers.contains(player.getUniqueId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		
		if (plugin.staffmode.contains(player.getUniqueId()) || plugin.frozen.contains(player.getUniqueId())) {
			event.setCancelled(true);
		}
		if (plugin.getConfig().getBoolean("vanish.build_break") == false && plugin.hiddenplayers.contains(player.getUniqueId())) {
			event.setCancelled(true);
		}
	}
}
