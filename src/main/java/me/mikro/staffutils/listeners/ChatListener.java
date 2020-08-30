package me.mikro.staffutils.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.mikro.staffutils.Main;
import me.mikro.staffutils.utils.Utils;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatListener implements Listener {

	private Main plugin;

	public ChatListener(Main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();

		//mutechat
		if (plugin.mutechat) {
			if (!player.hasPermission("staffutils.mutechat.bypass")) {
				event.setCancelled(true);
				player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("CHATSETTINGS.mutechat.message")));
			}
		}

		//Slowchat
		if (!player.hasPermission("staffutils.slowchat.bypass")) {
			if (plugin.mutechat) {
				return;
			}
			if (!plugin.chat_timer.containsKey(player.getUniqueId())) {
				plugin.chat_timer.put(player.getUniqueId(), System.currentTimeMillis());
				return;
			}
			if (System.currentTimeMillis() < plugin.chat_timer.get(player.getUniqueId()) + plugin.slowchat * 1000) {
				event.setCancelled(true);
				player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("CHATSETTINGS.slowchat.message_denied").replace("%slow%", String.valueOf(plugin.slowchat))));
			} else {
				plugin.chat_timer.put(player.getUniqueId(), System.currentTimeMillis());
			}
		}

		//Setting a slowchat
		if (plugin.slowchat_edit.contains(player.getUniqueId())) {
			try {
				plugin.slowchat = Double.parseDouble(message);
				event.setCancelled(true);
				player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("CHATSETTINGS.slowchat.slowchat_updated").replace("%slow%", String.valueOf(plugin.slowchat))));
				plugin.slowchat_edit.remove(player.getUniqueId());
				return;
			} catch (NumberFormatException e) {
				player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("CHATSETTINGS.slowchat.invalid_time")));
				event.setCancelled(true);
				return;
			}
		}

		//Staffchat
		if (plugin.staffchat.contains(player.getUniqueId())) {
			event.setCancelled(true);

			if (!plugin.data.get(player.getUniqueId()).getConfig().getBoolean("staff.staffchat_view")) {
				player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFCHAT.currently_disabled")));
				return;
			}
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission("staffutils.staffchat") && plugin.data.get(player.getUniqueId()).getConfig().getBoolean("staff.staffchat_view") == true) {
					p.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFCHAT.format").replace("%player%", player.getName()).replace("%message%", message)));
				}
			}
			Bukkit.getConsoleSender().sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFCHAT.format").replace("%player%", player.getName()).replace("%message%", message)));
		}
	}
	@EventHandler
	public void onPlayerCommandEvent(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (plugin.frozen.contains(player.getUniqueId())) {
			if (!(player.hasPermission("staffutils.staffmember"))) {
				for (String s : plugin.getConfig().getStringList("freeze.allowed_commands")) {
					if (event.getMessage().contains(s)) {
						return;
					}
				}
				player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("FREEZE.currently_frozen_message")));
				event.setCancelled(true);
			}
		}
	}
}
