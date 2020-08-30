package me.mikro.staffutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mikro.staffutils.Main;
import me.mikro.staffutils.utils.Utils;

import java.util.ArrayList;

public class StaffModeCommand implements CommandExecutor {

	private Main plugin;
	
	public StaffModeCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("staffmode").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This commnand may only be executed as a player");
			return true;
		}
		if (!(sender.hasPermission("staffutils.staffmode"))) {
			sender.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("noperm")));
			return true;
		}
		Player player = (Player) sender;

		if (plugin.staffmode.contains(player.getUniqueId())) {
			player.getInventory().clear();
			player.setGameMode(GameMode.SURVIVAL);
			player.getInventory().setContents(plugin.inventory.get(player.getUniqueId()));
			player.getInventory().setArmorContents(plugin.invarmor.get(player.getUniqueId()));
			player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFMODE.switch_off")));

			plugin.inventory.remove(player.getUniqueId());
			plugin.invarmor.remove(player.getUniqueId()); 
			plugin.staffmode.remove(player.getUniqueId());
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.showPlayer(player);
			}
			plugin.hiddenplayers.remove(player.getUniqueId());
			return true;
		}
			plugin.inventory.put(player.getUniqueId(), player.getInventory().getContents()); 
			plugin.invarmor.put(player.getUniqueId(), player.getInventory().getArmorContents());
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.setGameMode(GameMode.CREATIVE);
			player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFMODE.switch_on")));
			plugin.staffmode.add(player.getUniqueId());

			if (plugin.getConfig().getBoolean("STAFFMODE.items.compass.enabled") == true) {
				ItemStack PhaseCompass;
				if (plugin.ver > 113) {
					PhaseCompass = new ItemStack(Material.valueOf("LEGACY_COMPASS"), 1);
				} else {
					PhaseCompass = new ItemStack(Material.COMPASS, 1);
				}
				ItemMeta metaP = PhaseCompass.getItemMeta();
				metaP.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFMODE.items.compass.name")));
				ArrayList<String> loreP  = new ArrayList<String>();
				for (String s : plugin.getConfig().getStringList("STAFFMODE.items.compass.lore")) {
					loreP.add(Utils.chat(s));
				}
				metaP.setLore(loreP);
				PhaseCompass.setItemMeta(metaP);
				player.getInventory().setItem(plugin.getConfig().getInt("STAFFMODE.items.compass.slot"), PhaseCompass);
			}

			if (plugin.getConfig().getBoolean("STAFFMODE.items.randomtp.enabled") == true) {
				ItemStack RandomTP;
				if (plugin.ver > 113) {
					RandomTP = new ItemStack(Material.valueOf("LEGACY_WATCH"), 1);
				} else if (plugin.ver == 113) {
					RandomTP = new ItemStack(Material.CLOCK, 1);
				} else {
					RandomTP = new ItemStack(Material.valueOf("WATCH"), 1);
				}
				ItemMeta metaR = RandomTP.getItemMeta();
				metaR.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFMODE.items.randomtp.name")));
				ArrayList<String> loreR  = new ArrayList<String>();
				for (String s : plugin.getConfig().getStringList("STAFFMODE.items.randomtp.lore")) {
					loreR.add(Utils.chat(s));
				}
				metaR.setLore(loreR);
				RandomTP.setItemMeta(metaR);
				player.getInventory().setItem(plugin.getConfig().getInt("STAFFMODE.items.randomtp.slot"), RandomTP);
			}
			if (plugin.getConfig().getBoolean("STAFFMODE.items.invseebook.enabled") == true) {
				ItemStack InspectBook;
				if (plugin.ver > 113) {
					InspectBook = new ItemStack(Material.valueOf("LEGACY_BOOK"), 1);
				} else {
					InspectBook = new ItemStack(Material.BOOK, 1);
				}
				ItemMeta metaI = InspectBook.getItemMeta();
				metaI.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFMODE.items.invseebook.name")));
				ArrayList<String> loreI  = new ArrayList<String>();
				for (String s : plugin.getConfig().getStringList("STAFFMODE.items.invseebook.lore")) {
					loreI.add(Utils.chat(s));
				}
				metaI.setLore(loreI);
				InspectBook.setItemMeta(metaI);
				player.getInventory().setItem(plugin.getConfig().getInt("STAFFMODE.items.invseebook.slot"), InspectBook);
			}
			if (plugin.getConfig().getBoolean("STAFFMODE.items.freezetool.enabled") == true) {
				ItemStack FreezeTool;
				if (plugin.ver > 113) {
					FreezeTool = new ItemStack(Material.valueOf("LEGACY_ICE"), 1);
				} else {
					FreezeTool = new ItemStack(Material.ICE, 1);
				}
				ItemMeta metaF = FreezeTool.getItemMeta();
				metaF.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFMODE.items.freezetool.name")));
				ArrayList<String> loreF  = new ArrayList<String>();
				for (String s : plugin.getConfig().getStringList("STAFFMODE.items.freezetool.lore")) {
					loreF.add(Utils.chat(s));
				}
				metaF.setLore(loreF);
				FreezeTool.setItemMeta(metaF);
				player.getInventory().setItem(plugin.getConfig().getInt("STAFFMODE.items.freezetool.slot"), FreezeTool);
			}
			if (plugin.getConfig().getBoolean("STAFFMODE.items.stafflist.enabled") == true) {
				ItemStack stafflist;
				if (plugin.ver > 113) {
					stafflist = new ItemStack(Material.valueOf("LEGACY_SKULL_ITEM"), 1, (short) 3);
				} else if (plugin.ver == 113) {
					stafflist = new ItemStack(Material.PLAYER_HEAD, 1);
				} else {
					stafflist = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
				}
				ItemMeta metaS = stafflist.getItemMeta();
				metaS.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFMODE.items.stafflist.name")));
				ArrayList<String> loreS  = new ArrayList<String>();
				for (String s : plugin.getConfig().getStringList("STAFFMODE.items.stafflist.lore")) {
					loreS.add(Utils.chat(s));
				}
				metaS.setLore(loreS);
				stafflist.setItemMeta(metaS);
				player.getInventory().setItem(plugin.getConfig().getInt("STAFFMODE.items.stafflist.slot"), stafflist);
			}

			if (plugin.getConfig().getBoolean("STAFFMODE.items.vanish.enabled") == true) {
				ItemStack vanish;
				if (plugin.ver > 113) {
					vanish = new ItemStack(Material.valueOf("LEGACY_INK_SACK"), 1, (short) 8);
				} else if (plugin.ver == 113) {
					vanish = new ItemStack(Material.GRAY_DYE, 1);
				} else {
					vanish = new ItemStack(Material.valueOf("INK_SACK"), 1, (short) 8);
				}
				ItemMeta metaV = vanish.getItemMeta();
				metaV.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFMODE.items.vanish.name") + " &aon"));
				ArrayList<String> loreV  = new ArrayList<String>();
				for (String string : plugin.getConfig().getStringList("STAFFMODE.items.vanish.lore")) {
					loreV.add(Utils.chat(string));
				}
				metaV.setLore(loreV);
				vanish.setItemMeta(metaV);
				player.getInventory().setItem(plugin.getConfig().getInt("STAFFMODE.items.vanish.slot"), vanish);
			}
			if (plugin.getConfig().getBoolean("STAFFMODE.items.wand.enabled") == true) {
				ItemStack vanish;
				if (plugin.ver > 113) {
					vanish = new ItemStack(Material.valueOf("LEGACY_WOOD_AXE"), 1);
				} else if (plugin.ver == 113) {
					vanish = new ItemStack(Material.WOODEN_AXE, 1);
				} else {
					vanish = new ItemStack(Material.valueOf("WOOD_AXE"), 1);
				}
				ItemMeta metaV = vanish.getItemMeta();
				metaV.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFMODE.items.wand.name")));
				ArrayList<String> loreV  = new ArrayList<String>();
				for (String string : plugin.getConfig().getStringList("STAFFMODE.items.wand.lore")) {
					loreV.add(Utils.chat(string));
				}
				metaV.setLore(loreV);
				vanish.setItemMeta(metaV);
				player.getInventory().setItem(plugin.getConfig().getInt("STAFFMODE.items.wand.slot"), vanish);
		}
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (!p.hasPermission("staffutils.vanish.see") || !plugin.data.get(p.getUniqueId()).getConfig().getBoolean("staff.vanish_view_others")) {
					p.hidePlayer(player);
				}
			}
			plugin.hiddenplayers.add(player.getUniqueId());
			return false;
	}
}