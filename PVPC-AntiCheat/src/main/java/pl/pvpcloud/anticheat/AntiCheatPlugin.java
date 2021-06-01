package pl.pvpcloud.anticheat;

import pl.pvpcloud.anticheat.commands.AntiCheatCommand;
import pl.pvpcloud.anticheat.config.AntiCheatConfig;
import pl.pvpcloud.anticheat.listener.PlayerListener;
import pl.pvpcloud.anticheat.listener.packet.PacketListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiCheatPlugin extends JavaPlugin {

    private static Plugin thisPlugin;
    private static PacketListener packetListener;
    private static AntiCheatConfig antiCheatConfig = new AntiCheatConfig();

    public void onEnable() {
        thisPlugin = this;

        saveDefaultConfig();

        antiCheatConfig.read(getConfig());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        packetListener = new PacketListener();

        getCommand("anticheat").setExecutor(new AntiCheatCommand());
    }

    public static Plugin getPlugin() {
        return thisPlugin;
    }

    public static PacketListener getPacketListener() {
        return packetListener;
    }

    public static AntiCheatConfig getAntiCheatConfig() {
        return antiCheatConfig;
    }

}
