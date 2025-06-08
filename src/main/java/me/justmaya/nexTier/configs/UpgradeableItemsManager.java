package me.justmaya.nexTier.configs;

import me.justmaya.nexTier.NexTier;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UpgradeableItemsManager {
    private final NexTier plugin = NexTier.getInstance();
    private File file;
    private FileConfiguration config;

    private final Set<Material> upgradableItems = new HashSet<>();

    public UpgradeableItemsManager() {
        createConfig();
        loadItems();
    }

    private void createConfig() {
        file = new File(plugin.getDataFolder(), "upgradeable_items.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("upgradeable_items.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    private void loadItems() {
        upgradableItems.clear();
        List<String> itemList = config.getStringList("items");
        for (String itemName : itemList) {
            try {
                Material material = Material.valueOf(itemName);
                upgradableItems.add(material);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid material in upgradeable_items.yml: " + itemName);
            }
        }
    }

    public boolean addItem(Material material) {
        if (upgradableItems.contains(material)) return false;

        upgradableItems.add(material);
        saveItems();
        return true;
    }

    public boolean removeItem(Material material) {
        if (!upgradableItems.contains(material)) return false;

        upgradableItems.remove(material);
        saveItems();
        return true;
    }

    public boolean isUpgradable(Material material) {
        return upgradableItems.contains(material);
    }

    public Set<Material> getUpgradableItems() {
        return new HashSet<>(upgradableItems);
    }

    private void saveItems() {
        List<String> itemNames = upgradableItems.stream()
                .map(Enum::name)
                .toList();

        config.set("items", itemNames);
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save upgradeable_items.yml: " + e.getMessage());
        }
    }
}
