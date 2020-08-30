package me.mikro.staffutils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import me.mikro.staffutils.commands.*;
import me.mikro.staffutils.listeners.*;
import me.mikro.staffutils.utils.UpdateChecker;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.mikro.staffutils.utils.FolderYamlFile;
import me.mikro.staffutils.utils.Utils;
import me.mikro.staffutils.utils.YamlFile;

public class Main extends JavaPlugin {

	public Set<UUID> hiddenplayers = new HashSet<>();
	public Set<UUID> frozen = new HashSet<>();
	public Set<UUID> staffchat = new HashSet<>();
	public Map<UUID, FolderYamlFile> data = new HashMap<UUID, FolderYamlFile>();
	public Set<UUID> staffmode = new HashSet<>(); 
	public Map<UUID, ItemStack[]> inventory = new HashMap<UUID, ItemStack[]>();
	public Map<UUID, ItemStack[]> invarmor = new HashMap<UUID, ItemStack[]>();
	public Map<UUID, String> requestmessage = new HashMap<UUID, String>();
	public Map<UUID, Long> staffplaytime = new HashMap<UUID, Long>();

	//Chatsettings variables
	public Set<UUID> slowchat_edit = new HashSet<>();
	public Map<UUID, Long> chat_timer = new HashMap<UUID, Long>();
	public double slowchat;
	public Boolean mutechat = false;
	public Boolean update_available = false;
	public String update_version;

	public int ver = StringUtils.countMatches(Bukkit.getBukkitVersion().substring(0, Bukkit.getBukkitVersion().indexOf("-")), ".") == 2 ?
			Integer.parseInt(Bukkit.getBukkitVersion().substring(0, Bukkit.getBukkitVersion().substring(0, Bukkit.getBukkitVersion().indexOf("-")).
					lastIndexOf(".")).replace(".", "")) :
			Integer.parseInt(Bukkit.getBukkitVersion().substring(0, Bukkit.getBukkitVersion().indexOf("-")).replace(".", ""));
	
	public YamlFile messagesFile = new YamlFile("messages", this);
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		messagesFile.save();
		new TeleportCommand(this);
		new JoinLeaveListener(this);
		new VanishCommand(this);
		new StaffChatCommand(this);
		new StaffModeCommand(this); 
		new PickUpListener(this);
		new DropListener(this);
		new PlayerBlockPlace(this);
		new ItemInventoryEvent(this);
		new DamageListener(this);
		new onPlayerUse(this);
		new FreezeListener(this);
		new FreezeCommand(this);
		new RequestCommand(this);
		new ChatListener(this);
		new StaffUtilsCommand(this);
		new StaffSettingsCommand(this);
		new ChatSettingsCommand(this);
		if (ver >= 19) {
			new onSwapItemsListener(this);
		}

		new UpdateChecker(this, 81847).getVersion(version -> {
			Bukkit.getConsoleSender().sendMessage("[StaffUtils] Checking for updates...");
			if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
				Bukkit.getConsoleSender().sendMessage("[StaffUtils] Plugin is up to date");
			} else {
				Bukkit.getConsoleSender().sendMessage("[StaffUtils] Currently running on " + getDescription().getVersion() + " version " + version + " is out");
				Bukkit.getConsoleSender().sendMessage("[StaffUtils] Update StaffUtils at https://www.spigotmc.org/resources/staffutils.81847/");
				update_version = version;
				update_available = true;
			}
		});

		new BukkitRunnable() {
			@Override
			public void run() {
				for (UUID uuid : frozen) {
					Player player = Bukkit.getPlayer(uuid);
					if (getConfig().getBoolean("freeze.freeze_message_space")) {
						for (int i=0; i < 4; i++) {
							player.sendMessage(Utils.chat("&7"));
						}
					}
					for (String string : messagesFile.getConfig().getStringList("FREEZE.message")) {
						player.sendMessage(Utils.chat(string));
					}
				}
			}
		}.runTaskTimerAsynchronously(this, 0L, 60L);
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			data.put(p.getUniqueId(), new FolderYamlFile("players/" + p.getUniqueId().toString(), this));
			staffplaytime.put(p.getUniqueId(), System.currentTimeMillis());
		}

	}
	public void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			for (UUID uuid : hiddenplayers) {
				Player toShow = Bukkit.getPlayer(uuid);
				player.showPlayer(toShow);
			}
		}
		for (UUID uuid : staffchat) {
			Player p = Bukkit.getPlayer(uuid);
			p.sendMessage(Utils.chat(messagesFile.getConfig().getString("STAFFCHAT.disabled_on_reload")));
		}
		for (UUID uuid : staffmode) {
			Player p = Bukkit.getPlayer(uuid);
				
			p.getInventory().clear();
			p.setGameMode(GameMode.SURVIVAL);
			p.getInventory().setContents(inventory.get(p.getUniqueId()));
			p.getInventory().setArmorContents(invarmor.get(p.getUniqueId()));
			p.sendMessage(Utils.chat(messagesFile.getConfig().getString("STAFFMODE.switch_off_reload")));

			inventory.remove(p.getUniqueId());
			invarmor.remove(p.getUniqueId());
		}
	}
}