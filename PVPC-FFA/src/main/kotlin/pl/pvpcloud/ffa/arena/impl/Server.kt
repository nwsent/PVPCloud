package pl.pvpcloud.ffa.arena.impl

import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.common.helpers.LocationHelper
import pl.pvpcloud.ffa.arena.Arena
import pl.pvpcloud.ffa.save.SaveAPI

class Server : Arena(
    "Server",
    "server",
    true,
    true
) {

    init {
        this.spawnLocation = LocationHelper(0.0, 75.0, 0.0).toBukkitLocation()
        this.cuboidLocation = LocationHelper(81.0, 80.0, -53.0).toBukkitLocation()
    }

    override fun handleJoin(player: Player) {
        player.teleport(this.spawnLocation)
        player.sendTitle("", "&eWitaj na&8: &fFFA", 10, 50, 10)
        player.health = 20.0
        player.activePotionEffects.forEach { player.removePotionEffect(it.type) }
        SaveAPI.giveItems(player, this.kitName)
    }

    override fun handleDeath(player: Player) {
        SaveAPI.giveItems(player, this.kitName)
    }

}