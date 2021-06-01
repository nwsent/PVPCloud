package pl.pvpcloud.modules.drop.structure

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import pl.pvpcloud.modules.drop.config.DropConfig
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors

class DropManage(private val config: DropConfig) {

    val dropPlayers: Map<UUID, DropPlayer> = ConcurrentHashMap()

    fun getPlayer(uniqueId: UUID): DropPlayer? =
            this.dropPlayers[uniqueId]

    fun getAvailableDrops(player: Player, user: DropPlayer, block: Block): Set<DropItem>? {
        val itemInHand = player.itemInHand

        if (block.type != Material.STONE || !itemInHand.type.name.contains("PICKAXE", true)) {
            return null
        }

        val id = block.typeId

        return this.config.drops.stream()
                .filter { user.disabledDrops.contains(id) }
                .filter { it.height >= player.location.blockY }
                .collect(Collectors.toSet())
    }

}