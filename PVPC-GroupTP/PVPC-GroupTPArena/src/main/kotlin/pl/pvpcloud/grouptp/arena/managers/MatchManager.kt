package pl.pvpcloud.grouptp.arena.managers

import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.common.helpers.InventorySerializerHelper
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.grouptp.arena.GroupTpPlugin
import pl.pvpcloud.grouptp.arena.match.Match
import pl.pvpcloud.grouptp.arena.match.MatchTask
import pl.pvpcloud.grouptp.common.packets.PacketKitItemsRequest
import pl.pvpcloud.grouptp.common.packets.PacketKitItemsResponse
import pl.pvpcloud.grouptp.common.packets.PacketPrepareMatch
import pl.pvpcloud.nats.NetworkAPI
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class MatchManager(private val plugin: GroupTpPlugin) {

    private val matches = ConcurrentHashMap<Int, Match>()

    private val ai = AtomicInteger(0)

    fun getMatch(player: Player): Match? {
        return this.matches.values.singleOrNull { it.players.contains(player.uniqueId)}
    }

    fun createMatch(packet: PacketPrepareMatch) {
        val matchId = ai.getAndIncrement()
        val arena = this.plugin.arenaManager.createNewArena(matchId)
        val match = Match(this.plugin, matchId, arena, packet.players.toHashSet(), packet.kitName)
        this.matches[match.matchId] = match
        MatchTask(match)
    }

    fun sendToHub(player: Player) {
        val connectPlayer = ConnectAPI.getPlayerByUUID(player.uniqueId)
                ?: return
        player.sendTitle("", "&cNie wszyscy dolaczyli do areny", 0, 40, 0)
        connectPlayer.connect("gtp_hub")
    }

    fun deleteMatch(match: Match) {
        this.matches.remove(match.matchId)
    }

    fun giveItems(player: Player) {
        NetworkAPI.requestAndResponse("gtp_hub", { PacketKitItemsRequest(player.uniqueId, this.getMatch(player)!!.kitName) }) { callback ->
            if (callback is PacketKitItemsResponse) {
                player.inventory.armorContents = InventorySerializerHelper.deserializeInventory(callback.armor)
                player.inventory.contents = InventorySerializerHelper.deserializeInventory(callback.inventory)
                player.updateInventory()
            }
        }
    }

}