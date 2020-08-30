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

public class StaffSettingsCommand implements CommandExecutor {

    private Main plugin;

    public StaffSettingsCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("staffsettings").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command may only be executed as a player");
            return true;
        }
        Player player = (Player) sender;
        if (!(player.hasPermission("staffutils.staffsettings"))) {
            player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
            return true;
        }

        Inventory inv = Bukkit.createInventory(player, 27, Utils.chat("&2Staff settings"));
        if (plugin.getConfig().getBoolean("STAFFSETTINGS.fill_with_glass") == true) {
            ItemStack glass;
            if (plugin.ver > 113) {
                glass = new ItemStack(Material.valueOf("LEGACY_GLASS"), 1, (short) 7);
            } else if (plugin.ver == 113) {
                glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE,  1);
            } else {
                glass = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7);
            }
            ItemMeta metaG = glass.getItemMeta();
            metaG.setDisplayName(Utils.chat("&7"));
            glass.setItemMeta(metaG);

            for (int i=0; i < 27; i++) {
                inv.setItem(i, glass);
            }
        }
        
        if (plugin.data.get(player.getUniqueId()).getConfig().getBoolean("staff.staffmode_on_join")) {
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

        } if (plugin.data.get(player.getUniqueId()).getConfig().getBoolean("staff.staffmode_on_join") == false) {
            ItemStack staffmode;
            if (plugin.ver > 113) {
                staffmode = new ItemStack(Material.valueOf("LEGACY_WOOL"), 1, (short) 14);
            } else if (plugin.ver == 113) {
                staffmode = new ItemStack(Material.RED_WOOL, 1);
            } else {
                staffmode = new ItemStack(Material.valueOf("WOOL"), 1, (short) 14);
            }
            ItemMeta metaSC = staffmode.getItemMeta();
            metaSC.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFSETTINGS.names.staffmode_on_join.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.off_setting"))));
            ArrayList<String> loreSC = new ArrayList<String>();
            for (String s : plugin.getConfig().getStringList("STAFFSETTINGS.names.staffmode_on_join.lore")) {
                loreSC.add(Utils.chat(s));
            }
            metaSC.setLore(loreSC);
            staffmode.setItemMeta(metaSC);
            inv.setItem(plugin.getConfig().getInt("STAFFSETTINGS.names.staffmode_on_join.slot"), staffmode);
        }
          if (plugin.data.get(player.getUniqueId()).getConfig().getBoolean("staff.staffchat_on_join") == true) {
              ItemStack staffchat;
              if (plugin.ver > 113) {
                  staffchat = new ItemStack(Material.valueOf("lEGACY_WOOL"), 1, (short) 13);
              } else if (plugin.ver == 113) {
                  staffchat = new ItemStack(Material.GREEN_WOOL, 1);
              } else {
                  staffchat = new ItemStack(Material.valueOf("WOOL"), 1, (short) 13);
              }
              ItemMeta metaSC = staffchat.getItemMeta();
              metaSC.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFSETTINGS.names.staffchat_on_join.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.on_setting"))));
              ArrayList<String> loreSC = new ArrayList<String>();
              for (String s : plugin.getConfig().getStringList("STAFFSETTINGS.names.staffchat_on_join.lore")) {
                  loreSC.add(Utils.chat(s));
              }
              metaSC.setLore(loreSC);
              staffchat.setItemMeta(metaSC);
              inv.setItem(plugin.getConfig().getInt("STAFFSETTINGS.names.staffchat_on_join.slot"), staffchat);
        } if (plugin.data.get(player.getUniqueId()).getConfig().getBoolean("staff.staffchat_on_join") == false) {
              ItemStack staffchat;
              if (plugin.ver > 113) {
                  staffchat = new ItemStack(Material.valueOf("LEGACY_WOOL"), 1, (short) 14);
              } else if (plugin.ver == 113) {
                  staffchat = new ItemStack(Material.RED_WOOL, 1);
              } else {
                  staffchat = new ItemStack(Material.valueOf("WOOL"), 1, (short) 14);
              }
              ItemMeta metaSC = staffchat.getItemMeta();
              metaSC.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFSETTINGS.names.staffchat_on_join.name").replace("%setting%", plugin.getConfig().getString("STAFFSETTINGS.off_setting"))));
              ArrayList<String> loreSC = new ArrayList<String>();
              for (String s : plugin.getConfig().getStringList("STAFFSETTINGS.names.staffchat_on_join.lore")) {
                loreSC.add(Utils.chat(s));
              }
              metaSC.setLore(loreSC);
              staffchat.setItemMeta(metaSC);
              inv.setItem(plugin.getConfig().getInt("STAFFSETTINGS.names.staffchat_on_join.slot"), staffchat);
        }
          if (plugin.data.get(player.getUniqueId()).getConfig().getBoolean("staff.staffchat_view") == true) {
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
          }
          if (plugin.data.get(player.getUniqueId()).getConfig().getBoolean("staff.staffchat_view") == false) {
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
        }
          if (plugin.data.get(player.getUniqueId()).getConfig().getBoolean("staff.vanish_view_others") == true) {
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
          }
            if (plugin.data.get(player.getUniqueId()).getConfig().getBoolean("staff.vanish_view_others") == false) {
                ItemStack vanishview;
                if (plugin.ver > 113) {
                    vanishview = new ItemStack(Material.valueOf("LEGACY_WOOL"), 1, (short) 14);
                } else if (plugin.ver == 113) {
                    vanishview = new ItemStack(Material.RED_WOOL, 1);
                }
                else {
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
        }
        player.openInventory(inv);
        return false;
    }
}