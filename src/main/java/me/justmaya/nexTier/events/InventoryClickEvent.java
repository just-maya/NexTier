package me.justmaya.nexTier.events;

import me.justmaya.nexTier.NexTier;
import me.justmaya.nexTier.gui.ConfirmUpgradeMenu;
import me.justmaya.nexTier.models.UpgradeData;
import me.justmaya.nexTier.services.UpgradeService;
import me.justmaya.nexTier.utils.ItemTemplateManager;
import me.justmaya.nexTier.utils.TemplateManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.regex.Pattern;

public class InventoryClickEvent implements Listener {
    NexTier plugin = NexTier.getInstance();
    TemplateManager template = plugin.getTemplateManager();

    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        Inventory inv = e.getClickedInventory();
        if (inv == null || e.getView().title() == Component.empty()) return;

        Component title = e.getView().title();
        TemplateManager template = plugin.getTemplateManager();

        Component mainTitle = template.getMenuTitle("MAIN_TITLE");
        String titlePlain = PlainTextComponentSerializer.plainText().serialize(title);
        String invisibleSeparator = "\u2063";

        if (!titlePlain.contains(invisibleSeparator) && !title.equals(mainTitle)) return;

        e.setCancelled(true);

        if (inv.getSize() != 27) return;

        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        if (title.equals(mainTitle)) {
            handleMainUpgradeGui(player, clicked);
        } else {
            handleConfirmGui(player, clicked);
        }
    }

    private void handleMainUpgradeGui(Player player, ItemStack clicked) {
        ItemMeta meta = clicked.getItemMeta();
        if (meta == null) return;

        NamespacedKey key = new NamespacedKey(plugin, "upgrade-id");
        if (!meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) return;

        String upgradeId = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        UpgradeData upgrade = plugin.getUpgradeManager().getUpgradeById(upgradeId);
        if (upgrade == null) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        ConfirmUpgradeMenu.open(player, item, upgrade);
    }

    private void handleConfirmGui(Player player, ItemStack clicked) {
        ItemMeta meta = clicked.getItemMeta();
        if (meta == null) return;

        NamespacedKey typeKey = new NamespacedKey(plugin, "button-type");
        if (!meta.getPersistentDataContainer().has(typeKey, PersistentDataType.STRING)) return;

        String buttonType = meta.getPersistentDataContainer().get(typeKey, PersistentDataType.STRING);

        if ("CANCEL".equalsIgnoreCase(buttonType)) {
            player.closeInventory();
            player.sendMessage(template.getMessageWithPrefix("CANCELLED"));
            return;
        }

        if (!"CONFIRM".equalsIgnoreCase(buttonType)) return;

        NamespacedKey upgradeKey = new NamespacedKey(plugin, "upgrade-id");
        if (!meta.getPersistentDataContainer().has(upgradeKey, PersistentDataType.STRING)) return;

        String upgradeId = meta.getPersistentDataContainer().get(upgradeKey, PersistentDataType.STRING);
        UpgradeData upgrade = plugin.getUpgradeManager().getUpgradeById(upgradeId);
        if (upgrade == null) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        plugin.getServer().getScheduler().runTask(
                plugin,
                () -> {
                    player.closeInventory();
                    UpgradeService.applyUpgrade(player, item, upgrade);
                }
        );
    }

}
