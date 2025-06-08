package me.justmaya.nexTier;

import me.justmaya.nexTier.commands.NexTierCommand;
import me.justmaya.nexTier.configs.UpgradeConfigManager;
import me.justmaya.nexTier.configs.UpgradeableItemsManager;
import me.justmaya.nexTier.events.InventoryClickEvent;
import me.justmaya.nexTier.events.InteractEvent;
import me.justmaya.nexTier.utils.ItemTemplateManager;
import me.justmaya.nexTier.utils.TemplateManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.Objects;

public final class NexTier extends JavaPlugin {
    private static NexTier instance;
    private UpgradeableItemsManager upgradeableItemsManager;
    private UpgradeConfigManager upgradeConfigManager;
    private TemplateManager templateManager;
    private ItemTemplateManager itemTemplateManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveFileIfNotExists("messages.yml");
        saveFileIfNotExists("items.yml");

        this.upgradeableItemsManager = new UpgradeableItemsManager();
        this.upgradeConfigManager = new UpgradeConfigManager();
        this.templateManager = new TemplateManager();
        this.itemTemplateManager = new ItemTemplateManager();

        commands();
        events();
    }

    private void commands() {
        Objects.requireNonNull(getCommand("nextier")).setExecutor(new NexTierCommand());
    }

    private void events() {
        Bukkit.getServer().getPluginManager().registerEvents(new InteractEvent(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickEvent(), this);
    }

    private void saveFileIfNotExists(String name) {
        File file = new File(getDataFolder(), name);
        if (!file.exists()) {
            saveResource(name, false);
        }
    }

    public static NexTier getInstance() {
        return instance;
    }

    public UpgradeableItemsManager getUpgradeableItemsConfig() {
        return upgradeableItemsManager;
    }

    public UpgradeConfigManager getUpgradeManager() {
        return upgradeConfigManager;
    }

    public TemplateManager getTemplateManager() { return templateManager; }

    public ItemTemplateManager getItemTemplateManager() { return itemTemplateManager; }
}
