package me.mikro.staffutils.listeners;

import me.mikro.staffutils.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FreezeListener implements Listener {

    private Main plugin;

    public FreezeListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void PlayerMovementListener(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (plugin.frozen.contains(player.getUniqueId())) {
            player.teleport(event.getFrom());
            if (plugin.getConfig().getBoolean("freeze.blindness") == true) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000000, 1));
            }
        }
    }
}
