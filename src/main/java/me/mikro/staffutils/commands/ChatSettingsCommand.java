package me.mikro.staffutils.commands;

import me.mikro.staffutils.Main;
import me.mikro.staffutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ChatSettingsCommand implements CommandExecutor {

    private Main plugin;

    public ChatSettingsCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("chatsettings").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You may only execute this command as a player");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("staffutils.chatsettings")) {
            player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
            return true;
        }
        Inventory inv = Bukkit.createInventory(player, plugin.getConfig().getInt("CHATSETTINGS.size"), Utils.chat("&cChat settings"));
        ItemStack glass;
        if (plugin.ver > 113) {
            glass = new ItemStack(Material.valueOf("LEGACY_GLASS"), 1, (short) 7);
        } else if (plugin.ver == 113) {
            glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        } else {
            glass = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7);
        }

        if (plugin.getConfig().getBoolean("CHATSETTINGS.fill_with_glass")) {
            for (int i = 0; i < 27; i++) {
                inv.setItem(i, glass);
            }
        }

        ItemStack clearchat;
        if (plugin.ver > 113) {
            clearchat = new ItemStack(Material.valueOf("LEGACY_BARRIER"), 1);
        } else {
            clearchat = new ItemStack(Material.BARRIER, 1);
        }
        ItemMeta metaC = clearchat.getItemMeta();
        metaC.setDisplayName(Utils.chat(plugin.getConfig().getString("CHATSETTINGS.names.clearchat.name")));
        ArrayList<String> loreC = new ArrayList<String>();
        for (String s : plugin.getConfig().getStringList("CHATSETTINGS.names.clearchat.lore")) {
            loreC.add(Utils.chat(s));
        }
        metaC.setLore(loreC);
        clearchat.setItemMeta(metaC);
        inv.setItem(plugin.getConfig().getInt("CHATSETTINGS.names.clearchat.slot"), clearchat);

        ItemStack mutechat;
        if (plugin.ver > 113) {
            mutechat = new ItemStack(Material.valueOf("LEGACY_PAPER"), 1);
        } else {
            mutechat = new ItemStack(Material.PAPER, 1);
        }
        ItemMeta metaT = mutechat.getItemMeta();
        if (plugin.mutechat) {
            metaT.setDisplayName(Utils.chat(plugin.getConfig().getString("CHATSETTINGS.names.mutechat.name").replace("%setting%", plugin.getConfig().getString("CHATSETTINGS.off_setting"))));
        } else {
            metaT.setDisplayName(Utils.chat(plugin.getConfig().getString("CHATSETTINGS.names.mutechat.name").replace("%setting%", plugin.getConfig().getString("CHATSETTINGS.on_setting"))));
        }
        ArrayList<String> loreT = new ArrayList<String>();
        for (String s : plugin.getConfig().getStringList("CHATSETTINGS.names.mutechat.lore")) {
            loreT.add(Utils.chat(s));
        }
        metaT.setLore(loreT);
        mutechat.setItemMeta(metaT);
        inv.setItem(plugin.getConfig().getInt("CHATSETTINGS.names.mutechat.slot"), mutechat);

        ItemStack slowchat;
        if (plugin.ver > 113) {
            slowchat = new ItemStack(Material.valueOf("LEGACY_SOUL_SAND"), 1);
        } else {
            slowchat = new ItemStack(Material.SOUL_SAND, 1);
        }
        ItemMeta metaS = slowchat.getItemMeta();
        metaS.setDisplayName(Utils.chat(plugin.getConfig().getString("CHATSETTINGS.names.slowchat.name").replace("%slow%", String.valueOf(plugin.slowchat))));
        ArrayList<String> loreS = new ArrayList<String>();
        for (String s : plugin.getConfig().getStringList("CHATSETTINGS.names.slowchat.lore")) {
            loreS.add(Utils.chat(s));
        }
        metaS.setLore(loreS);
        slowchat.setItemMeta(metaS);
        inv.setItem(plugin.getConfig().getInt("CHATSETTINGS.names.slowchat.slot"), slowchat);

        player.openInventory(inv);
        return false;
    }
}
