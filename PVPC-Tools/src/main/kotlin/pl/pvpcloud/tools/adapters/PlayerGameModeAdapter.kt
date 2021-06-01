package pl.pvpcloud.tools.adapters

import org.bukkit.Bukkit
import org.bukkit.GameMode
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketPlayerGameMode

class PlayerGameModeAdapter(private val plugin: ToolsPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketPlayerGameMode) {
            val player = Bukkit.getPlayer(packet.uuid) ?: return
            val gameMode = GameMode.getByValue(packet.mode)
            player.gameMode = gameMode
            player.sendFixedMessage("&8» &fTwoj tryb gry zostal zmieniony na&8: &e${gameMode.name} &8(&e${gameMode.ordinal}&8)")
        }
    }
}