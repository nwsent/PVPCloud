package pl.pvpcloud.connect.bungee.listener

import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import pl.blazingpack.bpauth.BlazingPackAuthEvent
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.connect.bungee.ConnectPlugin

class PlayerBlazingPackListener(
        private val plugin: ConnectPlugin
) : Listener {

    @EventHandler
    fun onBlazingPackAuth(event: BlazingPackAuthEvent) {
        this.plugin.playerService.updatePlayerComputerId(event.userConnection.uniqueId, event.userConnection.address.address.hostAddress, event.computerUUID)

        val player = ConnectAPI.getPlayerByUUID(event.userConnection.uniqueId)
                ?: return
        val ip = event.userConnection.address.address.hostAddress
        player.ip = ip
        val playersIp = this.plugin.playerRepository.playerMap.values.asSequence().count { it.ip == ip }
        if (playersIp > 2) {
            event.userConnection.disconnect(TextComponent("&cWykryto multi-konto!".fixColors()))
            return
        }
        val computerId = event.computerUUID
                ?: return
        player.computerId = computerId
        val playersComputerId = this.plugin.playerRepository.playerMap.values.asSequence().filter { it.computerId != null }.count { it.computerId!!.contentToString() == computerId.contentToString() }
        if (playersComputerId > 1) {
            event.userConnection.disconnect(TextComponent("&cWykryto multi-konto!".fixColors()))
            return
        }
    }

}