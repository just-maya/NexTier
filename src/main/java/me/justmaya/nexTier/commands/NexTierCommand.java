package me.justmaya.nexTier.commands;

import me.justmaya.nexTier.NexTier;
import me.justmaya.nexTier.configs.UpgradeableItemsManager;
import me.justmaya.nexTier.utils.TemplateManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class NexTierCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        TemplateManager template = NexTier.getInstance().getTemplateManager();

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(template.getMessageWithPrefix("PLAYER_ONLY"));
            return true;
        }

        if (strings.length == 1 && strings[0].equalsIgnoreCase("reload")) {
            NexTier plugin = NexTier.getInstance();

            plugin.reloadConfig();

            plugin.getTemplateManager().reload();
            plugin.getItemTemplateManager().reload();

            player.sendMessage(plugin.getTemplateManager().getMessageWithPrefix("RELOADED"));
            return true;
        }

        if (strings.length != 2 || !strings[0].equalsIgnoreCase("item")) {
            player.sendMessage(template.getMessageWithPrefix("COMMAND_USE", Map.of("%COMMAND%", s)));
            return true;
        }

        String action = strings[1].toLowerCase();
        UpgradeableItemsManager config = NexTier.getInstance().getUpgradeableItemsConfig();

        if (action.equals("list")) {
            Set<Material> items = config.getUpgradableItems();
            if (items.isEmpty()) {
                player.sendMessage(template.getMessageWithPrefix("NO_UPGRADABLE_ITEMS"));
            } else {
                player.sendMessage(template.getMessageWithPrefix("UPGRADABLE_ITEMS_HEADER"));
                for (Material mat : items) {
                    player.sendMessage(Component.text(" - " + mat.name()));
                }
            }
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            player.sendMessage(template.getMessageWithPrefix("ITEM_IN_HAND"));
            return true;
        }

        Material material = item.getType();

        switch (action) {
            case "add" -> {
                if (config.addItem(material)) {
                    player.sendMessage(template.getMessageWithPrefix("ADDED_ITEM", Map.of("%MATERIAL%", material.name())));
                } else {
                    player.sendMessage(template.getMessageWithPrefix("ALREADY_IN_LIST"));
                }
            }
            case "remove" -> {
                if (config.removeItem(material)) {
                    player.sendMessage(template.getMessageWithPrefix("REMOVED_ITEM", Map.of("%MATERIAL%", material.name())));
                } else {
                    player.sendMessage(template.getMessageWithPrefix("NOT_IN_LIST"));
                }
            }
            default -> player.sendMessage(template.getMessageWithPrefix("UNFAMILIAR_SUBCOMMAND", Map.of("%ACTION%", action)));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("item", "reload");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("item")) {
            return List.of("add", "remove", "list");
        }

        return List.of();
    }

}
