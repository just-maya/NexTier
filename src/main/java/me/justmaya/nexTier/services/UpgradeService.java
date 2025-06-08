package me.justmaya.nexTier.services;

import me.justmaya.nexTier.NexTier;
import me.justmaya.nexTier.models.UpgradeData;
import me.justmaya.nexTier.utils.TemplateManager;
import me.justmaya.nexTier.utils.VaultUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class UpgradeService {

    static NexTier plugin = NexTier.getInstance();
    static TemplateManager template = plugin.getTemplateManager();

    public static void applyUpgrade(Player player, ItemStack item, UpgradeData upgrade) {
        int nextLevel = plugin.getUpgradeManager().getNextLevel(item, upgrade);
        if (nextLevel == -1) {
            Component m = template.getMessageWithPrefix("MAX_LEVEL");
            player.sendMessage(m);
            return;
        }

        if (!VaultUtil.hasEnough(player, upgrade.getPrice())) {
            Component m = template.getMessageWithPrefix("NOT_ENOUGH_MONEY", Map.of("%PRICE%", String.valueOf(upgrade.getPrice())));
            player.sendMessage(m);
            return;
        }

        VaultUtil.withdraw(player, upgrade.getPrice());

        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(upgrade.getEnchantment(), nextLevel, true);
        item.setItemMeta(meta);

        Component m = template.getMessageWithPrefix("SUCCESS", Map.of(
                "%UPGRADE%", upgrade.getDisplayName(),
                "%LEVEL%", String.valueOf(nextLevel)
                ));
        player.sendMessage(m);
    }
}
