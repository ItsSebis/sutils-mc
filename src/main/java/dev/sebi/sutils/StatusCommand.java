package dev.sebi.sutils;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

/** /status <text|remove>: sets or clears the player's own status. */
final class StatusCommand implements TabExecutor {
    private final Status status;

    StatusCommand(Status status) {
        this.status = status;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return reply(sender, "Only players have a status.", NamedTextColor.RED);
        }
        if (args.length == 0) {
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("remove")) {
            status.set(player, null);
            return reply(player, "Status removed.", NamedTextColor.GREEN);
        }

        String text = String.join(" ", args);
        if (!status.colorsAllowed()) {
            text = text.replace("&", "");
        }
        if (status.visibleLength(text) > status.maxLength()) {
            return reply(player, "Your status may be at most " + status.maxLength() + " characters long.", NamedTextColor.RED);
        }
        status.set(player, text);
        return reply(player, "Status set.", NamedTextColor.GREEN);
    }

    private static boolean reply(CommandSender sender, String message, NamedTextColor color) {
        sender.sendMessage(Component.text(message, color));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return args.length == 1 && "remove".startsWith(args[0].toLowerCase()) ? List.of("remove") : List.of();
    }
}
