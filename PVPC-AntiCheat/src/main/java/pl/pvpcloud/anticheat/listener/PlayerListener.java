package pl.pvpcloud.anticheat.listener;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import pl.pvpcloud.anticheat.AntiCheatPlugin;
import pl.pvpcloud.anticheat.data.MovingData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        AntiCheatPlugin.getPacketListener().inject(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        AntiCheatPlugin.getPacketListener().unInject(player);

        MovingData.removeData(player);
    }


    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        MovingData data = MovingData.getData(player);
        data.setPacketSend(data.getPacketSend() - 1);
    }

}
