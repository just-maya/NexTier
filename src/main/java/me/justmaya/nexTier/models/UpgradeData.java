package me.justmaya.nexTier.models;

import org.bukkit.enchantments.Enchantment;

public class UpgradeData {
    private final String id;
    private final Enchantment enchantment;
    private final int maxLevel;
    private final String displayName;
    private final int price;

    public UpgradeData(String id, Enchantment enchantment, int maxLevel, String displayName, int price) {
        this.id = id;
        this.enchantment = enchantment;
        this.maxLevel = maxLevel;
        this.displayName = displayName;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPrice() {
        return price;
    }
}
