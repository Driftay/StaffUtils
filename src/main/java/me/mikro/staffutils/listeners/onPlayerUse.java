package me.mikro.staffutils.listeners;

import java.util.ArrayList;
import java.util.Random;
import me.mikro.staffutils.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mikro.staffutils.Main;
import me.mikro.staffutils.utils.Utils;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class onPlayerUse implements Listener {

	private Main plugin;

	public onPlayerUse(Main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		ItemStack unvanish;
		if (plugin.ver > 113) {
			unvanish = new ItemStack(Material.LEGACY_INK_SACK, 1, (short) 10);
		} else if (plugin.ver == 113) {
			unvanish = new ItemStack(Material.LIME_DYE, 1);
		} else {
			unvanish = new ItemStack(Material.valueOf("INK_SACK"), 1, (short) 10);
		}

		ItemMeta metaUV = unvanish.getItemMeta();
		metaUV.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFMODE.items.vanish.name_unvanished") + " &coff"));
		ArrayList<String> loreUV  = new ArrayList<String>();
		for (String string : plugin.getConfig().getStringList("STAFFMODE.items.vanish.lore")) {
			loreUV.add(Utils.chat(string));
		}
		metaUV.setLore(loreUV);
		unvanish.setItemMeta(metaUV);

		ItemStack vanish;
		if (plugin.ver > 113) {
			vanish = new ItemStack(Material.valueOf("LEGACY_INK_SACK"), 1, (short) 8);;
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

		ItemStack RandomTP;
		if (plugin.ver > 113) {
			RandomTP = new ItemStack(Material.valueOf("LEGACY_WATCH"), 1);
		} else if (plugin.ver == 113) {
			RandomTP = new ItemStack(Material.CLOCK, 1);
		} else {
			RandomTP = new ItemStack(Material.valueOf("WATCH"));
		}

		ItemMeta metaR = RandomTP.getItemMeta();
		metaR.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFMODE.items.randomtp.name")));
		ArrayList<String> loreR  = new ArrayList<String>();
		for (String s : plugin.getConfig().getStringList("STAFFMODE.items.randomtp.lore")) {
			loreR.add(Utils.chat(s));
		}
		metaR.setLore(loreR);
		RandomTP.setItemMeta(metaR);


		ItemStack stafflist;
		if (plugin.ver > 113) {
			stafflist = new ItemStack(Material.valueOf("LEGACY_SKULL_ITEM"), 1 , (short) 3);
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


		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (player.getItemInHand().equals(vanish)) {
				if (!(player.hasPermission("staffutils.vanish"))) {
					return;
				}
				if (plugin.ver >= 19) {
					if (!event.getHand().equals(EquipmentSlot.HAND)) {
						return;
					}
				}
				player.getInventory().setItem(8, unvanish);
				player.performCommand("vanish");
				return;
			}
			if (player.getItemInHand().equals(unvanish)) {
				if (!(player.hasPermission("staffutils.vanish"))) {
					return;
				}
				if (plugin.ver >= 19) {
					if (!event.getHand().equals(EquipmentSlot.HAND)) {
						return;
					}
				}
				player.getInventory().setItem(8, vanish);
				player.performCommand("vanish");
				return;
			}
			if (player.getItemInHand().equals(RandomTP) && player.hasPermission("staffutils.randomtp")) {
				if (plugin.ver >= 19) {
					if (!event.getHand().equals(EquipmentSlot.HAND)) {
						return;
					}
				}
					ArrayList<Player> players = new ArrayList<Player>();
				if (Bukkit.getOnlinePlayers().size() == 1) {
					player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFMODE.randomtp_error")));
					return;
				}
				for (Player e : Bukkit.getOnlinePlayers()) players.add(e);
				players.remove(player);
				Player randomPlayer = players.get(new Random().nextInt(players.size()));
				player.teleport(randomPlayer.getLocation());
				player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFMODE.randomtp").replace("%random_player%", randomPlayer.getName())));
				return;
			}
			if (player.getItemInHand().equals(stafflist) && player.hasPermission("staffutils.stafflist")) {
				if (plugin.ver >= 19) {
					if (!event.getHand().equals(EquipmentSlot.HAND)) {
						return;
					}
				}
				Inventory inv = Bukkit.createInventory(player, 45, Utils.chat("&3Online staffmembers:"));
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.hasPermission("staffutils.staffmember")) {

						String time = TimeUtil.getFormattedString(System.currentTimeMillis() - plugin.staffplaytime.get(player.getUniqueId()));


						ItemStack playerhead;
						if (plugin.ver > 113) {
							playerhead = new ItemStack(Material.valueOf("LEGACY_SKULL_ITEM"), 1, (short) 3);
						} else if (plugin.ver == 113) {
							playerhead = new ItemStack(Material.PLAYER_HEAD, 1);
						} else {
							playerhead = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
						}

						SkullMeta skull = (SkullMeta) playerhead.getItemMeta();
						skull.setOwner(p.getName());
						playerhead.setItemMeta(skull);
						ItemMeta meta = playerhead.getItemMeta();
						ArrayList<String> lore = new ArrayList<String>();
						for (String string : plugin.messagesFile.getConfig().getStringList("STAFFMODE.stafflist_lore")) {
							lore.add(Utils.chat(string).replace("%player_name%", p.getName()).replace("%loc_x%", String.valueOf(Math.round(p.getLocation().getX()))
							).replace("%loc_y%", String.valueOf(Math.round(p.getLocation().getY()))).replace("%loc_z%", String.valueOf(Math.round(p.getLocation().getZ()))
							).replace("%world_name%", p.getLocation().getWorld().getName()).replace("%online_time%", time));
						}
						meta.setDisplayName(p.getDisplayName());
						meta.setLore(lore);
						playerhead.setItemMeta(meta);

						inv.addItem(playerhead);
					}
				}
				player.openInventory(inv);
				return;
			}

		}
	}
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

		Player player = event.getPlayer();
		if (!(event.getRightClicked() instanceof Player)) {
			return;
		}

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

		ItemStack InvseeBook;
		if (plugin.ver > 113) {
			InvseeBook = new ItemStack(Material.valueOf("LEGACY_BOOK"), 1);
		} else {
			InvseeBook = new ItemStack((Material.valueOf("BOOK")), 1);
		}
		ItemMeta metaI = InvseeBook.getItemMeta();
		metaI.setDisplayName(Utils.chat(plugin.getConfig().getString("STAFFMODE.items.invseebook.name")));
		ArrayList<String> loreI  = new ArrayList<String>();
		for (String s : plugin.getConfig().getStringList("STAFFMODE.items.invseebook.lore")) {
			loreI.add(Utils.chat(s));
		}
		metaI.setLore(loreI);
		InvseeBook.setItemMeta(metaI);

		Player target = (Player) event.getRightClicked();

		if (plugin.ver < 19) {
			if (player.getItemInHand().equals(FreezeTool) && player.hasPermission("staffutils.freeze")) {
				player.performCommand("staffutils:freeze " + target.getName());
				return;
			}
		} else {
			if (event.getHand().equals(EquipmentSlot.HAND) && player.getItemInHand().equals(FreezeTool) && player.hasPermission("staffutils.freeze")) {
				player.performCommand("staffutils:freeze " + target.getName());
				return;
			}
		}
		if (player.getItemInHand().equals(InvseeBook) && player.hasPermission("staffutils.invseetool")) {
			if (plugin.ver >= 19) {
				if (!event.getHand().equals(EquipmentSlot.HAND)) {
					return;
				}
			}
			ItemStack[] contents = target.getInventory().getContents();
			ItemStack helmet = target.getInventory().getHelmet();
			ItemStack chestplate = target.getInventory().getChestplate();
			ItemStack leggings = target.getInventory().getLeggings();
			ItemStack boots = target.getInventory().getBoots();

			ItemStack potions;
			if (plugin.ver > 113) {
				potions = new ItemStack(Material.valueOf("LEGACY_POTION"), 1);
			} else if (plugin.ver == 113) {
				potions = new ItemStack(Material.POTION, 1, (short) 1);
			} else {
				potions = new ItemStack(Material.valueOf("POTION"), 1);
			}
			ArrayList<String> ActiveEffects = new ArrayList<String>();
			ItemMeta potionsmeta = potions.getItemMeta();
			potionsmeta.setDisplayName(Utils.chat("&bActive effects&3:"));
			if (target.getActivePotionEffects().isEmpty()) {
				ActiveEffects.add(Utils.chat("&7No active effects"));
			}
			for (PotionEffect effect : target.getActivePotionEffects()) {
				int dur = effect.getDuration();
				dur = dur/20;
				dur = Math.round(dur);

				ActiveEffects.add(Utils.chat("&7" + effect.getType().getName() + "&2 " + (effect.getAmplifier() + 1) + "&a " + dur + "&7s"));
			}
			potionsmeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			potionsmeta.setLore(ActiveEffects);
			potions.setItemMeta(potionsmeta);

			ItemStack health;
			int phealth = (int) Math.round(target.getHealth());
			if (plugin.ver > 113) {
				health = new ItemStack(Material.valueOf("LEGACY_SPECKLED_MELON"), phealth);
			} else if (plugin.ver == 113) {
				health = new ItemStack(Material.GLISTERING_MELON_SLICE, phealth);
			} else {
				health = new ItemStack(Material.valueOf("SPECKLED_MELON"), phealth);
			}
			ItemMeta metaH = health.getItemMeta();
			metaH.setDisplayName(Utils.chat("&aPlayer Health: &2" + phealth));
			health.setItemMeta(metaH);

			ItemStack hunger;
			int phunger = Math.round(target.getFoodLevel());
			if (plugin.ver > 113) {
				hunger = new ItemStack(Material.valueOf("LEGACY_COOKED_BEEF"), phunger);
			} else {
				hunger = new ItemStack(Material.COOKED_BEEF, phunger);
			}
			ItemMeta metahunger = hunger.getItemMeta();
			metahunger.setDisplayName(Utils.chat("&6Player Food: &e" + phunger));
			hunger.setItemMeta(metahunger);

			Inventory inv = Bukkit.createInventory(target, 45, Utils.chat("&eInventory of " + target.getName()));
			inv.setContents(contents);
			inv.setItem(36, helmet);
			inv.setItem(37, chestplate);
			inv.setItem(38, leggings);
			inv.setItem(39, boots);
			inv.setItem(42, potions);
			inv.setItem(43, hunger);
			inv.setItem(44, health);
			player.openInventory(inv);

			new BukkitRunnable() {
				@Override
				public void run() {
				 	ItemStack[] contents = target.getInventory().getContents();
				 	ItemStack helmet = target.getInventory().getHelmet();
					ItemStack chestplate = target.getInventory().getChestplate();
					ItemStack leggings = target.getInventory().getLeggings();
					ItemStack boots = target.getInventory().getBoots();

					ItemStack potions;
					if (plugin.ver > 113) {
						potions = new ItemStack(Material.valueOf("LEGACY_POTION"), 1);
					} else if (plugin.ver == 113) {
						potions = new ItemStack(Material.POTION, 1, (short) 1);
					} else {
						potions = new ItemStack(Material.valueOf("POTION"), 1);
					}
					ArrayList<String> ActiveEffects = new ArrayList<String>();
					ItemMeta potionsmeta = potions.getItemMeta();
					potionsmeta.setDisplayName(Utils.chat("&bActive effects&3:"));
					if (target.getActivePotionEffects().isEmpty()) {
						ActiveEffects.add(Utils.chat("&7No active effects"));
					}
					for (PotionEffect effect : target.getActivePotionEffects()) {
						int dur = effect.getDuration();
						dur = dur/20;
						dur = Math.round(dur);

						ActiveEffects.add(Utils.chat("&7" + effect.getType().getName() + "&2 " + (effect.getAmplifier() + 1) + "&a " + dur + "&7s"));
					}
					potionsmeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
					potionsmeta.setLore(ActiveEffects);
					potions.setItemMeta(potionsmeta);

					ItemStack health;
					int phealth = (int) Math.round(target.getHealth());
					if (plugin.ver > 113) {
						health = new ItemStack(Material.valueOf("LEGACY_SPECKLED_MELON"), phealth);
					} else if (plugin.ver == 113) {
						health = new ItemStack(Material.GLISTERING_MELON_SLICE, phealth);
					} else {
						health = new ItemStack(Material.valueOf("SPECKLED_MELON"), phealth);
					}
					ItemMeta metaH = health.getItemMeta();
					metaH.setDisplayName(Utils.chat("&aPlayer Health: &2" + phealth));
					health.setItemMeta(metaH);

					int phunger = Math.round(target.getFoodLevel());
					ItemStack hunger;
					if (plugin.ver > 113) {
						hunger = new ItemStack(Material.valueOf("LEGACY_COOKED_BEEF"), phunger);
					} else {
						hunger = new ItemStack(Material.valueOf("COOKED_BEEF"), phunger);
					}
					ItemMeta metahunger = hunger.getItemMeta();
					metahunger.setDisplayName(Utils.chat("&6Player Food: &e" + phunger));
					hunger.setItemMeta(metahunger);

					ItemStack gamemode;
					if (plugin.ver > 113) {
						gamemode = new ItemStack(Material.valueOf("LEGACY_NETHER_STAR"), 1);
					} else {
						gamemode = new ItemStack(Material.NETHER_STAR, 1);
					}
					ItemMeta metaG = gamemode.getItemMeta();
					String gm = target.getGameMode().toString().toUpperCase();
					metaG.setDisplayName(Utils.chat("&3Gamemode:"));
					ArrayList<String> loreGM = new ArrayList<String>();
					if (target.getGameMode() == GameMode.CREATIVE) {
						loreGM.add(Utils.chat("&7Player gamemode: &c" + gm));
					}
					else if (target.getGameMode() == GameMode.SURVIVAL) {
						loreGM.add(Utils.chat("&7Player gamemode: &a" + gm));
					}
					else if (target.getGameMode() == GameMode.ADVENTURE) {
						loreGM.add(Utils.chat("&7Player gamemode: &2" + gm));
					}
					else {
						loreGM.add(Utils.chat("&7Player gamemode: &f" + " SPECTATOR"));
					}
					metaG.setLore(loreGM);
					gamemode.setItemMeta(metaG);

					inv.setContents(contents);
					inv.setItem(36, helmet);
					inv.setItem(37, chestplate);
					inv.setItem(38, leggings);
					inv.setItem(39, boots);
					inv.setItem(42, potions);
					inv.setItem(43, hunger);
					inv.setItem(44, health);
					inv.setItem(41, gamemode);

					boolean viewer = false;

					for (HumanEntity humanEntity : inv.getViewers()) {
						if (humanEntity.getName().equals(player.getName())) {
							viewer = true;
						}
					}
					if (viewer == false) {
						cancel();
					}

 				}
			}.runTaskTimerAsynchronously(plugin, 0L, 10L);
			player.sendMessage(Utils.chat(plugin.messagesFile.getConfig().getString("STAFFMODE.invsee").replace("%TARGET%", target.getName())));
		}
	}
}