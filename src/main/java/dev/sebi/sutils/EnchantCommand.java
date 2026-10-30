package dev.sebi.sutils;

import java.util.Arrays;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/** /enchant <player> <enchantment> [level]: replaces vanilla, any enchantment on any item at any level. Level 0 removes it. */
final class EnchantCommand implements TabExecutor {
    private final Plugin plugin;

    EnchantCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 2 || args.length > 3) {
            return false;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            return fail(sender, "Player " + args[0] + " is not online.");
        }
        NamespacedKey key = NamespacedKey.fromString(args[1].toLowerCase());
        Enchantment enchantment = key == null ? null : Registry.ENCHANTMENT.get(key);
        if (enchantment == null) {
            return fail(sender, "Unknown enchantment " + args[1] + ".");
        }
        int maxLevel = plugin.getConfig().getInt("enchant.max-level", 255);
        int level = args.length == 3 ? parseLevel(args[2], maxLevel) : 1;
        if (level < 0) {
            return fail(sender, "Level must be a number from 0 to " + maxLevel + ".");
        }
        ItemStack item = target.getInventory().getItemInMainHand();
        if (item.isEmpty()) {
            return fail(sender, target.getName() + " is not holding an item.");
        }

        if (level == 0) {
            item.removeEnchantment(enchantment);
        } else {
            item.addUnsafeEnchantment(enchantment, level);
        }
        sender.sendMessage(Component.text("Set " + key.getKey() + " " + level + " on " + target.getName() + "'s item.",
                NamedTextColor.GREEN));
        return true;
    }

    /** Returns the level, or -1 when it is not a number in 0..max. */
    private static int parseLevel(String text, int max) {
        try {
            int level = Integer.parseInt(text);
            return level <= max ? level : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static boolean fail(CommandSender sender, String message) {
        sender.sendMessage(Component.text(message, NamedTextColor.RED));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        String typed = args[args.length - 1].toLowerCase();
        return switch (args.length) {
            case 1 -> Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(n -> n.toLowerCase().startsWith(typed)).toList();
            case 2 -> Registry.ENCHANTMENT.stream().map(e -> e.getKey().getKey()).filter(n -> n.startsWith(typed)).toList();
            default -> List.of();
        };
    }
}
