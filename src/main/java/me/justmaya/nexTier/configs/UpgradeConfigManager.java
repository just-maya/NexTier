package me.justmaya.nexTier.configs;

import me.justmaya.nexTier.NexTier;
import me.justmaya.nexTier.models.UpgradeData;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class UpgradeConfigManager {

    private final Map<String, UpgradeData> upgrades = new LinkedHashMap<>();

    public UpgradeConfigManager() {
        NexTier plugin = NexTier.getInstance();
        FileConfiguration config = plugin.getConfig();

        if (!config.isConfigurationSection("upgrades")) {
            plugin.getLogger().warning("upgrades section doesn't defined in config.yml");
            return;
        }

        for (String id : config.getConfigurationSection("upgrades").getKeys(false)) {
            String path = "upgrades." + id;
            String enchantName = config.getString(path + ".enchant");
            int maxLevel = config.getInt(path + ".max-level");
            String displayName = config.getString(path + ".display-name");
            int price = config.getInt(path + ".price");

            Enchantment enchantment = Enchantment.getByName(enchantName.toUpperCase());
            if (enchantment == null) {
                plugin.getLogger().warning("upgrade" + id + "contain illegal enchant" + enchantName);
                continue;
            }

            upgrades.put(id, new UpgradeData(id, enchantment, maxLevel, displayName, price));
        }
    }

    public Collection<UpgradeData> getAllUpgrades() {
        return upgrades.values();
    }

    public UpgradeData getUpgradeById(String id) {
        return upgrades.get(id);
    }

    public int getNextLevel(ItemStack item, UpgradeData upgrade) {
        if (item == null || item.getType() == Material.AIR) return -1;

        int currentLevel = item.getEnchantmentLevel(upgrade.getEnchantment());
        if (currentLevel >= upgrade.getMaxLevel()) return -1;

        return currentLevel + 1;
    }

    public boolean canUpgrade(ItemStack item, UpgradeData upgrade) {
        return getNextLevel(item, upgrade) != -1;
    }
}
