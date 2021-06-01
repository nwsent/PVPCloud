package pl.pvpcloud.grouptp.hub.basic

import pl.pvpcloud.common.helpers.LocationHelper
import pl.pvpcloud.grouptp.common.packets.PacketPrepareMatch
import pl.pvpcloud.nats.NetworkAPI
import java.util.*
import java.util.concurrent.TimeUnit

class Teleport(
        val location: LocationHelper,
        val kitName: String,
        val teleportType: TeleportType
) {
        var lastTimeUse: Long = System.currentTimeMillis()

        fun sendPlayers(players: Set<UUID>) {
                this.lastTimeUse = System.currentTimeMillis() + 500L
                NetworkAPI.publish("gtp_1") { PacketPrepareMatch(players, this.kitName) }
        }
}