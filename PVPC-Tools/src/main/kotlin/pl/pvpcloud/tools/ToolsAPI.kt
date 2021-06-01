package pl.pvpcloud.tools

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendActionBar
import pl.pvpcloud.common.helpers.PlayerHelper
import java.util.*

object ToolsAPI {

    internal lateinit var plugin: ToolsPlugin

    fun findUserByUUID(uuid: UUID) = this.plugin.userManager.findUserByUUID(uuid)
    fun findUserByUUID(player: Player) = this.plugin.userManager.findUserByUUID(player.uniqueId)

    fun addCoins(uuid: UUID, coins: Int) {
        Bukkit.getPlayer(uuid).sendActionBar("&e+${coins}coins'a")
        this.findUserByUUID(uuid).addCoins(coins * if (this.plugin.config.multipleCoins > System.currentTimeMillis()) 2 else 1)
    }

    fun UUID.addKillCoins() {
        when(PlayerHelper.getGroup(this)) {
            "vip" -> addCoins(this, 4)
            "lider" -> addCoins(this,4)
            "zeus" -> addCoins(this,5)
            "youtube" -> addCoins(this, 5)
            else -> addCoins(this,3)
        }
    }

    fun UUID.removeDeathsCoins() { findUserByUUID(this).removeCoins(1) }

    fun getUserByNick(nick: String) = this.plugin.userManager.getUserByNick(nick)

}