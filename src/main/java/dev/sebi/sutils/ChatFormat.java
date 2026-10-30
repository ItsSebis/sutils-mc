package dev.sebi.sutils;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/** Renders chat as "[status] Name: message", with operators' names in pink and & codes in messages. */
final class ChatFormat implements Listener {
    private final Status status;

    ChatFormat(Status status) {
        this.status = status;
    }

    @EventHandler
    void onChat(AsyncChatEvent event) {
        event.renderer(ChatRenderer.viewerUnaware((player, displayName, message) -> {
            NamedTextColor nameColor = player.isOp() ? NamedTextColor.LIGHT_PURPLE : NamedTextColor.GRAY;
            Component body = player.hasPermission("sutils.chat.color") ? withColors(message) : message;
            return status.prefix(player)
                    .append(displayName.color(nameColor))
                    .append(Component.text(": ", NamedTextColor.GRAY))
                    .append(body.colorIfAbsent(NamedTextColor.WHITE));
        }));
    }

    private static Component withColors(Component message) {
        return Status.AMPERSAND.deserialize(PlainTextComponentSerializer.plainText().serialize(message));
    }
}
