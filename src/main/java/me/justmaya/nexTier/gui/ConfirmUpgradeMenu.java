package me.justmaya.nexTier.gui;

import me.justmaya.nexTier.NexTier;
import me.justmaya.nexTier.models.UpgradeData;
import me.justmaya.nexTier.utils.ItemTemplateManager;
import me.justmaya.nexTier.utils.TemplateManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;

public class ConfirmUpgradeMenu {
    static NexTier plugin = NexTier.getInstance();
    static ItemTemplateManager itemTemplate = plugin.getItemTemplateManager();
    static TemplateManager template = plugin.getTemplateManager();


    public static void open(Player player, ItemStack heldItem, UpgradeData upgrade) {
        String invisibleSeparator = "\u2063";
        Component title = template.getMenuTitle("CONFIRM_TITLE", Map.of("%UPGRADE%", upgrade.getDisplayName()))
                .append(Component.text(invisibleSeparator));
        Inventory gui = Bukkit.createInventory(null, 27, title);

        // disabled item
        ItemStack cancel = itemTemplate.getItem("CANCEL", Map.of("%PRICE%", String.valueOf(upgrade.getPrice())));
        gui.setItem(11, cancel);

        // item to upgrade
        gui.setItem(13, heldItem.clone());

        // confirm item
        ItemStack confirm = itemTemplate.getItem("CONFIRM", Map.of("%PRICE%", String.valueOf(upgrade.getPrice())));

        ItemMeta meta = confirm.getItemMeta();
        if (meta != null) {
            NamespacedKey key = new NamespacedKey(plugin, "upgrade-id");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, upgrade.getId());
            confirm.setItemMeta(meta);
        }

        gui.setItem(15, confirm);

        player.openInventory(gui);
    }
}
