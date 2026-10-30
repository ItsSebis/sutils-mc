package dev.sebi.sutils;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/** Renders chat as "[status] Name: message", with operators' names in pink. */
final class ChatFormat implements Listener {
    private final Status status;

    ChatFormat(Status status) {
        this.status = status;
    }

    @EventHandler
    void onChat(AsyncChatEvent event) {
        event.renderer(ChatRenderer.viewerUnaware((player, displayName, message) -> {
            NamedTextColor nameColor = player.isOp() ? NamedTextColor.LIGHT_PURPLE : NamedTextColor.GRAY;
            return status.prefix(player)
                    .append(displayName.color(nameColor))
                    .append(Component.text(": ", NamedTextColor.GRAY))
                    .append(message.colorIfAbsent(NamedTextColor.WHITE));
        }));
    }
}
