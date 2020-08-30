package me.mikro.staffutils.listeners;

import me.mikro.staffutils.Main;
import me.mikro.staffutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.UUID;

public class ItemInventoryEvent implements Listener {

    private Main plugin;

    public ItemInventoryEvent(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryModify(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (plugin.staffmode.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getView().getTitle().equalsIgnoreCase(Utils.chat("&cChat settings"))) {
            event.setCancelled(true);
        }
        if (event.getInventory().getHolder() instanceof Player && event.getView().getTitle().equalsIgnoreCase(Utils.chat("&cChat settings"))) {
            if (event.isShiftClick()) {
                event.setCancelled(true);
            }
        }

        if (event.getView().getTitle().equalsIgnoreCase(Utils.chat("&3Online staffmembers:"))) {
            event.setCancelled(true);
            if (!(player.hasPermission("staffutils.stafflist.tp"))) {
                player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
                return;
            }
            SkullMeta skull = (SkullMeta) event.getCurrentItem().getItemMeta();
            if (skull == null) {
                return;
            }
            Player target = Bukkit.getPlayer(skull.getOwner());
            player.performCommand("tp " + target.getName());
            return;
        }
        if (event.getInventory().getHolder() instanceof Player && event.getView().getTitle().equalsIgnoreCase(Utils.chat("&2Staff settings"))) {
            if (event.isShiftClick()) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getView().getTitle().equalsIgnoreCase(Utils.chat("&2Staff settings"))) {
            event.setCancelled(true);
            player.updateInventory();
            if (!(player.hasPermission("staffutils.staffsettings.edit"))) {
                player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
                return;
            }
            if (!(event.getCurrentItem().hasItemMeta())) {
                return;
            }

            Inventory inv = event.getInventory();
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat(plugin.getConfig().getString
                    ("STAFFSETTINGS.names.staffmode_on_join.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.on_setting"))))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffmode_on_join", false);
                plugin.data.get(player.getUniqueId()).save();
                ItemStack staffmode;
                if (plugin.ver > 113) {
                    staffmode = new ItemStack(Material.valueOf("LEGACY_WOOL"), 1, (short) 14);
                } else if (plugin.ver == 113) {
                    staffmode = new ItemStack(Material.RED_WOOL, 1);
                } else {
                    staffmode = new ItemStack(Material.valueOf("WOOL"), 1, (short) 14);
                }
                ItemMeta metaSM = staffmode.getItemMeta();
                metaSM.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFSETTINGS.names.staffmode_on_join.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.off_setting"))));
                ArrayList<String> loreSC = new ArrayList<String>();
                for (String s : plugin.getConfig().getStringList("STAFFSETTINGS.names.staffmode_on_join.lore")) {
                    loreSC.add(Utils.chat(s));
                }
                metaSM.setLore(loreSC);
                staffmode.setItemMeta(metaSM);
                inv.setItem(plugin.getConfig().getInt("STAFFSETTINGS.names.staffmode_on_join.slot"), staffmode);
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat(plugin.getConfig().getString
                    ("STAFFSETTINGS.names.staffmode_on_join.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.off_setting"))))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffmode_on_join", true);
                plugin.data.get(player.getUniqueId()).save();
                ItemStack staffmode;
                if (plugin.ver > 113) {
                    staffmode = new ItemStack(Material.valueOf("LEGACY_WOOL"), 1, (short) 13);
                } else if (plugin.ver == 113) {
                    staffmode = new ItemStack(Material.GREEN_WOOL, 1);
                } else {
                    staffmode = new ItemStack(Material.valueOf("WOOL"), 1, (short) 13);
                }
                ItemMeta metaSM = staffmode.getItemMeta();
                metaSM.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFSETTINGS.names.staffmode_on_join.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.on_setting"))));
                ArrayList<String> loreSC = new ArrayList<String>();
                for (String s : plugin.getConfig().getStringList("STAFFSETTINGS.names.staffmode_on_join.lore")) {
                    loreSC.add(Utils.chat(s));
                }
                metaSM.setLore(loreSC);
                staffmode.setItemMeta(metaSM);
                inv.setItem(plugin.getConfig().getInt("STAFFSETTINGS.names.staffmode_on_join.slot"), staffmode);
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat(plugin.getConfig().getString
                    ("STAFFSETTINGS.names.staffchat_on_join.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.on_setting"))))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffchat_on_join", false);
                plugin.data.get(player.getUniqueId()).save();
                ItemStack staffchat;
                if (plugin.ver > 113) {
                    staffchat = new ItemStack(Material.valueOf("LEGACY_WOOL"), 1, (short) 14);
                } else if (plugin.ver == 113) {
                    staffchat = new ItemStack(Material.RED_WOOL, 1);
                } else {
                    staffchat = new ItemStack(Material.valueOf("WOOL"), 1, (short) 14);
                }
                ItemMeta metaSM = staffchat.getItemMeta();
                metaSM.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFSETTINGS.names.staffchat_on_join.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.off_setting"))));
                ArrayList<String> loreSC = new ArrayList<String>();
                for (String s : plugin.getConfig().getStringList("STAFFSETTINGS.names.staffchat_on_join.lore")) {
                    loreSC.add(Utils.chat(s));
                }
                metaSM.setLore(loreSC);
                staffchat.setItemMeta(metaSM);
                inv.setItem(plugin.getConfig().getInt("STAFFSETTINGS.names.staffchat_on_join.slot"), staffchat);
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat(plugin.getConfig().getString
                    ("STAFFSETTINGS.names.staffchat_on_join.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.off_setting"))))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffchat_on_join", true);
                plugin.data.get(player.getUniqueId()).save();
                ItemStack staffchat;
                if (plugin.ver > 113) {
                    staffchat = new ItemStack(Material.valueOf("LEGACY_WOOL"), 1, (short) 13);
                } else if (plugin.ver == 113) {
                    staffchat = new ItemStack(Material.GREEN_WOOL, 1);
                } else {
                    staffchat = new ItemStack(Material.valueOf("WOOL"), 1, (short) 13);
                }
                ItemMeta metaSM = staffchat.getItemMeta();
                metaSM.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFSETTINGS.names.staffchat_on_join.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.on_setting"))));
                ArrayList<String> loreSC = new ArrayList<String>();
                for (String s : plugin.getConfig().getStringList("STAFFSETTINGS.names.staffchat_on_join.lore")) {
                    loreSC.add(Utils.chat(s));
                }
                metaSM.setLore(loreSC);
                staffchat.setItemMeta(metaSM);
                inv.setItem(plugin.getConfig().getInt("STAFFSETTINGS.names.staffchat_on_join.slot"), staffchat);
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat(plugin.getConfig().getString
                    ("STAFFSETTINGS.names.staffchat_view.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.on_setting"))))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffchat_view", false);
                plugin.data.get(player.getUniqueId()).save();

                ItemStack staffchatview;
                if (plugin.ver > 113) {
                    staffchatview = new ItemStack(Material.valueOf("LEGACY_WOOL"), 1, (short) 14);
                } else if (plugin.ver == 113) {
                    staffchatview = new ItemStack(Material.RED_WOOL, 1);
                } else {
                    staffchatview = new ItemStack(Material.valueOf("WOOL"), 1, (short) 14);
                }
                ItemMeta metaSCV = staffchatview.getItemMeta();
                metaSCV.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFSETTINGS.names.staffchat_view.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.off_setting"))));
                ArrayList<String> loreSCV = new ArrayList<String>();
                for (String s : plugin.getConfig().getStringList("STAFFSETTINGS.names.staffchat_view.lore")) {
                    loreSCV.add(Utils.chat(s));
                }
                metaSCV.setLore(loreSCV);
                staffchatview.setItemMeta(metaSCV);
                inv.setItem(plugin.getConfig().getInt("STAFFSETTINGS.names.staffchat_view.slot"), staffchatview);
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat(plugin.getConfig().getString
                    ("STAFFSETTINGS.names.staffchat_view.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.off_setting"))))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.staffchat_view", true);
                plugin.data.get(player.getUniqueId()).save();
                ItemStack staffchatview;
                if (plugin.ver > 113) {
                    staffchatview = new ItemStack(Material.valueOf("LEGACY_WOOL"), 1, (short) 13);
                } else if (plugin.ver == 113) {
                    staffchatview = new ItemStack(Material.GREEN_WOOL, 1);
                } else {
                    staffchatview = new ItemStack(Material.valueOf("WOOL"), 1, (short) 13);
                }
                ItemMeta metaSCV = staffchatview.getItemMeta();
                metaSCV.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFSETTINGS.names.staffchat_view.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.on_setting"))));
                ArrayList<String> loreSCV = new ArrayList<String>();
                for (String s : plugin.getConfig().getStringList("STAFFSETTINGS.names.staffchat_view.lore")) {
                    loreSCV.add(Utils.chat(s));
                }
                metaSCV.setLore(loreSCV);
                staffchatview.setItemMeta(metaSCV);
                inv.setItem(plugin.getConfig().getInt("STAFFSETTINGS.names.staffchat_view.slot"), staffchatview);
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat(plugin.getConfig().getString
                    ("STAFFSETTINGS.names.vanish_view_others.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.on_setting"))))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.vanish_view_others", false);
                plugin.data.get(player.getUniqueId()).save();
                if (!player.hasPermission("staffutils.vanish.see")) {
                    return;
                }
                for (UUID uuid : plugin.hiddenplayers) {
                    Player toHide = Bukkit.getPlayer(uuid);
                    player.hidePlayer(toHide);
                }
                player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("vanish.vanished_players_off")));

                ItemStack vanishview;
                if (plugin.ver > 113) {
                    vanishview = new ItemStack(Material.valueOf("LEGACY_WOOL"), 1, (short) 14);
                } else if (plugin.ver == 113) {
                    vanishview = new ItemStack(Material.RED_WOOL, 1);
                } else {
                    vanishview = new ItemStack(Material.valueOf("WOOL"), 1, (short) 14);
                }
                ItemMeta metaVV = vanishview.getItemMeta();
                metaVV.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFSETTINGS.names.vanish_view_others.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.off_setting"))));
                ArrayList<String> loreVV = new ArrayList<String>();
                for (String s : plugin.getConfig().getStringList("STAFFSETTINGS.names.vanish_view_others.lore")) {
                    loreVV.add(Utils.chat(s));
                }
                metaVV.setLore(loreVV);
                vanishview.setItemMeta(metaVV);
                inv.setItem(plugin.getConfig().getInt("STAFFSETTINGS.names.vanish_view_others.slot"), vanishview);
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat(plugin.getConfig().getString
                    ("STAFFSETTINGS.names.vanish_view_others.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.off_setting"))))) {
                plugin.data.get(player.getUniqueId()).getConfig().set("staff.vanish_view_others", true);
                plugin.data.get(player.getUniqueId()).save();
                if (!player.hasPermission("staffutils.vanish.see")) {
                    return;
                }
                for (UUID uuid : plugin.hiddenplayers) {
                    Player toShow = Bukkit.getPlayer(uuid);
                    player.showPlayer(toShow);
                }
                player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("vanish.vanished_players_on")));

                ItemStack vanishview;
                if (plugin.ver > 113) {
                    vanishview = new ItemStack(Material.valueOf("LEGACY_WOOL"), 1, (short) 13);
                } else if (plugin.ver == 113) {
                    vanishview = new ItemStack(Material.GREEN_WOOL, 1);
                } else {
                    vanishview = new ItemStack(Material.valueOf("WOOL"), 1, (short) 13);
                }
                ItemMeta metaVV = vanishview.getItemMeta();
                metaVV.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFSETTINGS.names.vanish_view_others.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.on_setting"))));
                ArrayList<String> loreVV = new ArrayList<String>();
                for (String s : plugin.getConfig().getStringList("STAFFSETTINGS.names.vanish_view_others.lore")) {
                    loreVV.add(Utils.chat(s));
                }
                metaVV.setLore(loreVV);
                vanishview.setItemMeta(metaVV);
                inv.setItem(plugin.getConfig().getInt("STAFFSETTINGS.names.vanish_view_others.slot"), vanishview);
                return;
            }
        }
        if (event.getView().getTitle().equalsIgnoreCase(Utils.chat("&cChat settings"))) {
            if (!event.getCurrentItem().hasItemMeta()) {
                return;
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat(plugin.getConfig().getString("CHATSETTINGS.names.clearchat.name")))) {
                if (!player.hasPermission("staffutils.clearchat")) {
                    player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
                    return;
                }
                for (int i = 1; i < 300; i++) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.hasPermission("staffutils.clearchat.bypass")) {
                            p.sendMessage(Utils.chat("&7"));
                        }
                    }
                }

                for (Player p : Bukkit.getOnlinePlayers()) {
                    for (String s : plugin.messagesFile.getConfig().getStringList("CHATSETTINGS.clearchat.message")) {
                        p.sendMessage(Utils.chat(s).replace("%executor%", player.getName()));
                    }
                }
                player.closeInventory();
                return;
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat(plugin.getConfig().getString("CHATSETTINGS.names.mutechat.name")
                    .replace("%setting%", plugin.getConfig().getString("CHATSETTINGS.on_setting"))))) {
                if (!player.hasPermission("staffutils.mutechat")) {
                    player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
                    return;
                }
                plugin.mutechat = true;
                ItemMeta metaM = event.getCurrentItem().getItemMeta();
                metaM.setDisplayName(Utils.chat(plugin.getConfig().getString("CHATSETTINGS.names.mutechat.name").replace("%setting%", plugin.getConfig().getString("CHATSETTINGS.off_setting"))));
                event.getCurrentItem().setItemMeta(metaM);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    for (String s : plugin.messagesFile.getConfig().getStringList("CHATSETTINGS.mutechat.chat_muted")) {
                        p.sendMessage(Utils.chat(s).replace("%executor%", player.getName()));
                    }
                }
                player.closeInventory();
                return;
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat(plugin.getConfig().getString("CHATSETTINGS.names.mutechat.name")
                    .replace("%setting%", plugin.getConfig().getString("CHATSETTINGS.off_setting"))))) {
                if (!player.hasPermission("staffutils.mutechat")) {
                    player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
                    return;
                }
                plugin.mutechat = false;
                ItemMeta metaM = event.getCurrentItem().getItemMeta();
                metaM.setDisplayName(Utils.chat(plugin.getConfig().getString("CHATSETTINGS.names.mutechat.name").replace("%setting%", plugin.getConfig().getString("CHATSETTINGS.on_setting"))));
                event.getCurrentItem().setItemMeta(metaM);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    for (String s : plugin.messagesFile.getConfig().getStringList("CHATSETTINGS.mutechat.chat_unmuted")) {
                        p.sendMessage(Utils.chat(s).replace("%executor%", player.getName()));
                    }
                }
                player.closeInventory();
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat(plugin.getConfig().getString("CHATSETTINGS.names.slowchat.name").
                    replace("%slow%", String.valueOf(plugin.slowchat))))) {
                if (!player.hasPermission("staffutils.slowchat")) {
                    player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
                    return;
                }
                plugin.slowchat_edit.add(player.getUniqueId());
                player.sendMessage("Test");

                for (String s : plugin.messagesFile.getConfig().getStringList("CHATSETTINGS.slowchat.message_input")) {
                    player.sendMessage(Utils.chat(s));
                }
                player.closeInventory();
                return;
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(Utils.chat("&2Staff settings"))) {
            event.setCancelled(true);
        }
        if (event.getView().getTitle().equalsIgnoreCase(Utils.chat("&cChat settings"))) {
            event.setCancelled(true);
        }
    }
}