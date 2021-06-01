package pl.pvpcloud.anticheat.data;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MovingData {

    private static final Map<UUID, MovingData> DATA_MAP = new ConcurrentHashMap<>();

    public static MovingData getData(Player player) {
        DATA_MAP.putIfAbsent(player.getUniqueId(), new MovingData());
        return DATA_MAP.get(player.getUniqueId());
    }

    public static void removeData(Player player) {
        DATA_MAP.remove(player.getUniqueId());
    }

    private long joinTime = 0;
    private long packetSend = 0;
    private int warns = 0;

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    public long getPacketSend() {
        return packetSend;
    }

    public void setPacketSend(long packetSend) {
        this.packetSend = packetSend;
    }

    public int getWarns() {
        return warns;
    }

    public void setWarns(int warns) {
        this.warns = warns;
    }
}
