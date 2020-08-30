package me.mikro.staffutils;

import org.bukkit.entity.Player;

/**
 * StaffUtils - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 8/30/2020
 */
public class StaffUtilsAPI {

    //Returns the current players state of invisibility
    public static boolean isPlayerVanished(Player player) {
        return Main.getInstance().hiddenplayers.contains(player.getUniqueId());
    }

    //Returns the current players state of StaffMode
    public static boolean isPlayerInStaffMode(Player player) {
        return Main.getInstance().staffmode.contains(player.getUniqueId());
    }
}
