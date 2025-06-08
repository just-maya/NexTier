package me.justmaya.nexTier.utils;

import me.justmaya.nexTier.NexTier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemTemplateManager {

    private YamlConfiguration items;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final NexTier plugin = NexTier.getInstance();
    private final File dataFolder = plugin.getDataFolder();

    public ItemTemplateManager() {
        File itemsFile = new File(dataFolder, "items.yml");
        if (!itemsFile.exists()) Bukkit.getLogger().warning("items.yml not found!");
        this.items = YamlConfiguration.loadConfiguration(itemsFile);
    }

    public ItemStack getItem(String id, Map<String, String> placeholders) {
        ConfigurationSection section = items.getConfigurationSection("buttons." + id);
        if (section == null) return new ItemStack(Material.BARRIER);

        Material mat = Material.matchMaterial(section.getString("material", "BARRIER"));
        if (mat == null) mat = Material.BARRIER;

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String name = section.getString("name", "");
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                name = name.replace(entry.getKey(), entry.getValue());
            }
            meta.displayName(miniMessage.deserialize(name));

            List<String> rawLore = section.getStringList("lore");
            List<Component> lore = rawLore.stream()
                    .map(line -> {
                        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                            line = line.replace(entry.getKey(), entry.getValue());
                        }
                        return miniMessage.deserialize(line);
                    })
                    .collect(Collectors.toList());
            meta.lore(lore);

            if (section.contains("custom-model-data")) {
                meta.setCustomModelData(section.getInt("custom-model-data"));
            }

            NamespacedKey buttonKey = new NamespacedKey(plugin, "button-type");
            meta.getPersistentDataContainer().set(buttonKey, PersistentDataType.STRING, id.toUpperCase());

            item.setItemMeta(meta);
        }
        return item;
    }

    public void reload() {
        File file = new File(dataFolder, "items.yml");
        this.items = YamlConfiguration.loadConfiguration(file);
    }

}

