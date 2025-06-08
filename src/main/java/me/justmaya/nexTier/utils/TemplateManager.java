package me.justmaya.nexTier.utils;

import me.justmaya.nexTier.NexTier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Collections;
import java.util.Map;

public class TemplateManager {

    private FileConfiguration messages;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final NexTier plugin = NexTier.getInstance();
    private final File dataFolder = plugin.getDataFolder();

    public TemplateManager() {
        File messagesFile = new File(dataFolder, "messages.yml");
        File itemsFile = new File(dataFolder, "items.yml");

        if (!messagesFile.exists()) Bukkit.getLogger().warning("messages.yml not found!");
        if (!itemsFile.exists()) Bukkit.getLogger().warning("items.yml not found!");

        this.messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public Component getMessage(String key, Map<String, String> placeholders) {
        String raw = messages.getString("messages." + key);
        if (raw == null) return Component.empty();

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            raw = raw.replace(entry.getKey(), entry.getValue());
        }

        return miniMessage.deserialize(raw);
    }

    public Component getMessage(String key) {
        return getMessage(key, Collections.emptyMap());
    }

    public Component getMessageWithPrefix(String key, Map<String, String> placeholders) {
        String raw = messages.getString("messages." + key);
        if (raw == null) {
            return Component.empty();
        }

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            raw = raw.replace(entry.getKey(), entry.getValue());
        }

        return miniMessage.deserialize(getRawPrefix() + raw);
    }

    public Component getMessageWithPrefix(String key) {
        return getMessageWithPrefix(key, Collections.emptyMap());
    }

    public String getRawPrefix() {
        return messages.getString("prefix", "");
    }

    public Component getMenuTitle(String key, Map<String, String> placeholders) {
        String raw = messages.getString("menu." + key, "");
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            raw = raw.replace(entry.getKey(), entry.getValue());
        }
        return miniMessage.deserialize(raw);
    }

    public Component getMenuTitle(String key) {
        return getMenuTitle(key, Collections.emptyMap());
    }

    public void reload() {
        File file = new File(dataFolder, "messages.yml");
        this.messages = YamlConfiguration.loadConfiguration(file);
    }


    public Component debugMessage() {
        ConfigurationSection section = messages.getConfigurationSection("messages");
        if (section == null) {
            return Component.text("messages section not found." + messages);
        }

        Component result = Component.empty();
        for (String key : section.getKeys(false)) {
            Object value = section.get(key);
            String line = key + ": " + (value != null ? value.toString() : "null");
            result = result.append(Component.text(line)).append(Component.newline());
        }

        return result;
    }

}

