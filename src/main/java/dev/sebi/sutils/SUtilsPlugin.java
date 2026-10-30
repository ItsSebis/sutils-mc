package dev.sebi.sutils;

import org.bukkit.plugin.java.JavaPlugin;

public final class SUtilsPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();

        getCommand("enchant").setExecutor(new EnchantCommand(this));
        getCommand("upgrade").setExecutor(new UpgradeCommand());

        Status status = new Status(this);
        getServer().getPluginManager().registerEvents(status, this);
        if (getConfig().getBoolean("chat.enabled", true)) {
            getServer().getPluginManager().registerEvents(new ChatFormat(status), this);
        }
        getCommand("status").setExecutor(new StatusCommand(status));
    }
}
