package pl.pvpcloud.anticheat.config;

import org.bukkit.configuration.file.FileConfiguration;

public class AntiCheatConfig {

    private boolean isEnable;
    private int packetToCheck;

    public void read(FileConfiguration config) {
        isEnable = config.getBoolean("isEnable", isEnable);
        packetToCheck = config.getInt("packetToCheck", packetToCheck);
    }

    public boolean isEnable() {
        return isEnable;
    }

    public int getPacketToCheck() {
        return packetToCheck;
    }

}
