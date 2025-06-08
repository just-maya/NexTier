package me.justmaya.nexTier.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VaultUtil {
    private static final Economy econ = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();

    public static boolean hasEnough(Player player, double amount) {
        return econ.has(player, amount);
    }

    public static void withdraw(Player player, double amount) {
        econ.withdrawPlayer(player, amount);
    }

    public static void deposit(Player player, double amount) {
        econ.depositPlayer(player, amount);
    }

    public static double getBalance(Player player) {
        return econ.getBalance(player);
    }
}
