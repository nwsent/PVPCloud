package pl.pvpcloud.hub.mode

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.hub.HubPlugin
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.hub.PacketTransfer

class ModeManager(private val plugin: HubPlugin) {

    private val modes = HashMap<String, Mode>()

    init {
        loadModes()
    }

    fun getMode(name: String): Mode? {
        return this.modes[name]
    }

    fun getModes(): HashMap<String, Mode> {
        return this.modes
    }

    fun loadModes() {
        this.modes.clear()
        this.plugin.config.modes.forEach { this.modes[it.name] = it }
    }

    fun connect(player: Player, mode: Mode) {
        if (!mode.isAvailable && !player.hasPermission("tools.admin")) {
            player.closeInventory()
            player.sendFixedMessage("&4* &cTryb jest wylaczony powod:&4 ${mode.reason} &8*")
            return
        }

        val cachePlayer = ConnectAPI.getPlayerByUUID(player.uniqueId)
                ?: return player.sendFixedMessage("&cZrób reloga!")

        NetworkAPI.publish { PacketTransfer(player.uniqueId, mode.name) }
        Bukkit.getScheduler().runTaskLater(this.plugin, {
            cachePlayer.connect(mode.servers[0])
        }, 15)
    }

}