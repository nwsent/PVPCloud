package pl.pvpcloud.hub.parkour

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.extensions.sendActionBar
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.common.helpers.LocBukkitHelper.isInLocationY
import pl.pvpcloud.common.helpers.LocationHelper
import pl.pvpcloud.tools.ToolsAPI
import java.util.concurrent.TimeUnit

class ParkourTask(private val parkour: Parkour) : BukkitRunnable() {

    init {
        runTaskTimer(parkour.plugin, 0, 20)
    }

    override fun run() {
        val arena = parkour.plugin.parkourConfig.easy

        val player = Bukkit.getPlayer(parkour.player) ?: return canceled(Bukkit.getPlayer(parkour.player))

        val user = parkour
        arena.blocks
                .filter { user.state < it.first }
                .forEach {
                    if (isInLocationY(player.location, 1, it.second.toBukkitLocation())) {
                        user.state = it.first
                        player.playSound(player.location, Sound.LEVEL_UP, 1f, 1f)
                    }
                }

        if (player.location.subtract(0.0, 1.0, 0.0).block.type != Material.QUARTZ_BLOCK) {
            if (player.location.subtract(0.0, 1.0,0.0).block.type != Material.AIR) {
                getLossOrWin(player, false)
            }
        }
        user.time += TimeUnit.SECONDS.toMillis(1)
        player.sendActionBar("&eParkour &f${user.state}&7/&f175 &8| &f${DataHelper.parseLong(user.time, false)}") // *${arena.blocks.size} TODO Do wyjebania to jest bikos dodaje mi +1
        if (user.state == 175)
            getLossOrWin(player, true)

    }

    private fun getLossOrWin(player: Player, boolean: Boolean) {
        if (boolean) {
            player.sendTitle("&8* &aGratulacje! &8*" ,"" , 20 , 30,20)
        } else {
            player.sendTitle("","&8* &cNie udało ci się :[ &8*",20 ,30,20)
            player.teleport(LocationHelper("world", 8.3, 63.0, -4.0, 174f, 4.7f).toBukkitLocation())
        }
        this.canceled(player)
    }

    private fun canceled(player: Player) {
        this.cancel()
        player.walkSpeed = 0.4f
        parkour.plugin.parkourManager.deleteUser(player.uniqueId)
    }

}