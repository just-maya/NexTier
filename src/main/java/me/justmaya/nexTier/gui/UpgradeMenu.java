package me.justmaya.nexTier.gui;

import me.justmaya.nexTier.NexTier;
import me.justmaya.nexTier.configs.UpgradeConfigManager;
import me.justmaya.nexTier.models.UpgradeData;
import me.justmaya.nexTier.utils.ItemTemplateManager;
import me.justmaya.nexTier.utils.TemplateManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UpgradeMenu {
    static NexTier plugin = NexTier.getInstance();

    public static void open(Player player, ItemStack heldItem) {
        TemplateManager template = plugin.getTemplateManager();
        UpgradeConfigManager manager = plugin.getUpgradeManager();
        Iterator<UpgradeData> upgrades = manager.getAllUpgrades().iterator();

        Inventory gui = Bukkit.createInventory(null, 27, template.getMenuTitle("MAIN_TITLE"));

        // get defined upgrades
        UpgradeData left = upgrades.hasNext() ? upgrades.next() : null;
        UpgradeData right = upgrades.hasNext() ? upgrades.next() : null;

        if (left != null) {
            gui.setItem(11, createUpgradeIcon(heldItem, left));
        }

        gui.setItem(13, heldItem.clone());

        if (right != null) {
            gui.setItem(15, createUpgradeIcon(heldItem, right));
        }

        player.openInventory(gui);
    }

    private static ItemStack createUpgradeIcon(ItemStack item, UpgradeData upgrade) {
        var template = plugin.getItemTemplateManager();
        var upgradeManager = plugin.getUpgradeManager();

        int nextLevel = upgradeManager.getNextLevel(item, upgrade);
        ItemStack displayItem;

        if (nextLevel == -1) {
            displayItem = template.getItem("LOCKED", Map.of(
                    "%UPGRADE%", upgrade.getDisplayName()
            ));
        } else {
            displayItem = template.getItem("UPGRADE", Map.of(
                    "%UPGRADE%", upgrade.getDisplayName(),
                    "%LEVEL%", String.valueOf(nextLevel)
            ));
        }

        ItemMeta meta = displayItem.getItemMeta();
        if (meta != null) {
            NamespacedKey key = new NamespacedKey(plugin, "upgrade-id");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, upgrade.getId());
            displayItem.setItemMeta(meta);
        }

        return displayItem;
    }

}
