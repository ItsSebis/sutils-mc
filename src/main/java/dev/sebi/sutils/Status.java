package dev.sebi.sutils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

/** Per-player status text (stored in status.yml) shown in front of the name in the tab list. */
final class Status implements Listener {
    private static final LegacyComponentSerializer AMPERSAND = LegacyComponentSerializer.legacyAmpersand();

    private final Plugin plugin;
    private final File file;
    private final YamlConfiguration data;

    Status(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "status.yml");
        this.data = YamlConfiguration.loadConfiguration(file);
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        apply(event.getPlayer());
    }

    /** Number of visible characters, ignoring color codes. */
    int visibleLength(String text) {
        return PlainTextComponentSerializer.plainText().serialize(AMPERSAND.deserialize(text)).length();
    }

    int maxLength() {
        return plugin.getConfig().getInt("status.max-length", 16);
    }

    boolean colorsAllowed() {
        return plugin.getConfig().getBoolean("status.allow-colors", true);
    }

    /** Sets the status, or removes it when {@code text} is null. */
    void set(Player player, String text) {
        data.set(player.getUniqueId().toString(), text);
        save();
        apply(player);
    }

    private void apply(Player player) {
        boolean hasStatus = data.contains(player.getUniqueId().toString());
        player.playerListName(hasStatus ? prefix(player).append(player.name()) : null);
    }

    /** The formatted status (e.g. "[Builder] "), or an empty component when the player has none. */
    Component prefix(Player player) {
        String text = data.getString(player.getUniqueId().toString());
        if (text == null) {
            return Component.empty();
        }
        String format = plugin.getConfig().getString("status.format", "&8[&r%status%&8] &r");
        return AMPERSAND.deserialize(format.replace("%status%", text));
    }

    private void save() {
        try {
            data.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save status.yml: " + e.getMessage());
        }
    }
}
