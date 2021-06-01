package pl.pvpcloud.anticheat.listener.packet;

import io.netty.channel.*;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import pl.pvpcloud.anticheat.AntiCheatPlugin;
import pl.pvpcloud.anticheat.data.MovingData;
import org.bukkit.entity.Player;

public class PacketListener {

    private void resetSend(MovingData movingData){
        movingData.setJoinTime(System.currentTimeMillis());
        movingData.setPacketSend(0);
    }

    public void handle(Player player) {
        MovingData data = MovingData.getData(player);

        data.setPacketSend(data.getPacketSend() + 1);
        if (data.getJoinTime() == 0) {
            if (data.getPacketSend() >= 50) {
                resetSend(data);
            }
            return;
        }
        if (data.getPacketSend() > AntiCheatPlugin.getAntiCheatConfig().getPacketToCheck()) {
            long max = ((System.currentTimeMillis() - data.getJoinTime()) / 50);
            long diff = data.getPacketSend() - max;
            if (diff > 5) {
                int ping = ((CraftPlayer) player).getHandle().ping;
                data.setWarns(data.getWarns() + 1);
                Bukkit.broadcast(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "AntiCheat" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getName() + " TIMER (" + diff + ") [" + ping + "] (" + data.getWarns() + ")", "ac.notify");
                resetSend(data);
                return;
            }
            resetSend(data);
        }
    }

    public void inject(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                if (packet instanceof PacketPlayInFlying) {
                    if (AntiCheatPlugin.getAntiCheatConfig().isEnable()) {
                        handle(player);
                    }
                }
                super.channelRead(channelHandlerContext, packet);
            }
        };

        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
    }

    public void unInject(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

}
