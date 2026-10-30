package dev.sebi.sutils;

import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/** /upgrade: turns the held tool into its netherite version, keeping all other item data. */
final class UpgradeCommand implements CommandExecutor {
    private static final Set<String> TIERS = Set.of("WOODEN", "STONE", "COPPER", "GOLDEN", "IRON", "DIAMOND");
    // An explicit list, because e.g. IRON_INGOT or DIAMOND_BLOCK would otherwise map to a netherite material too.
    private static final Set<String> TOOLS = Set.of("SWORD", "SPEAR", "PICKAXE", "AXE", "SHOVEL", "HOE");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return reply(sender, "Only players can upgrade items.", NamedTextColor.RED);
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        String[] parts = item.getType().name().split("_", 2);
        if (parts.length == 2 && parts[0].equals("NETHERITE") && TOOLS.contains(parts[1])) {
            return reply(player, "That tool is already netherite.", NamedTextColor.RED);
        }
        if (parts.length < 2 || !TIERS.contains(parts[0]) || !TOOLS.contains(parts[1])) {
            return reply(player, "Hold a wooden, stone, copper, golden, iron or diamond tool.", NamedTextColor.RED);
        }

        // withType keeps all data components (enchantments, name, lore, damage, attributes, ...).
        player.getInventory().setItemInMainHand(item.withType(Material.valueOf("NETHERITE_" + parts[1])));
        return reply(player, "Upgraded to netherite.", NamedTextColor.GREEN);
    }

    private static boolean reply(CommandSender sender, String message, NamedTextColor color) {
        sender.sendMessage(Component.text(message, color));
        return true;
    }
}
