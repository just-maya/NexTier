package me.justmaya.nexTier.events;

import me.justmaya.nexTier.NexTier;
import me.justmaya.nexTier.configs.UpgradeableItemsManager;
import me.justmaya.nexTier.gui.UpgradeMenu;
import me.justmaya.nexTier.utils.TemplateManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class InteractEvent implements Listener {
    boolean isSneaking;

    @EventHandler
    public void onSneakEvent(PlayerToggleSneakEvent e) {
        isSneaking = e.isSneaking();
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        boolean isClicking = e.getAction().isRightClick();

        if (!e.hasItem()) return;

        int amount = Objects.requireNonNull(e.getItem()).getAmount();

        if (isClicking && isSneaking) {

            ItemStack item = e.getItem();
            UpgradeableItemsManager upgradeableItemsManager = NexTier.getInstance().getUpgradeableItemsConfig();
            TemplateManager template = NexTier.getInstance().getTemplateManager();
            if (upgradeableItemsManager.isUpgradable(item.getType())) {
                e.setCancelled(true);

                if (amount > 1)  {
                    p.sendMessage(template.getMessageWithPrefix("NOT_MORE_THAN_ONE"));
                    return;
                }

                UpgradeMenu.open(p, item);
            }
        }
    }
}
