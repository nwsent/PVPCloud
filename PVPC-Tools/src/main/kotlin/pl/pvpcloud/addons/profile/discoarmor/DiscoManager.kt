package pl.pvpcloud.addons.profile.discoarmor

import org.bukkit.Color
import pl.pvpcloud.addons.AddonsModule
import pl.pvpcloud.addons.profile.discoarmor.DiscoType
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class DiscoManager(private val addonsModule: AddonsModule) {

    private val playersColor = ConcurrentHashMap<UUID, Color>()

    fun getPlayerColor(uniqueId: UUID): Color? {
        return this.playersColor[uniqueId]
    }

    fun setColor(uniqueId: UUID, color: Color) {
        this.playersColor[uniqueId] = color
    }

    fun removeColor(uniqueId: UUID) {
        this.playersColor.remove(uniqueId)
    }

    fun init(uniqueId: UUID, discoType: DiscoType) {
        var color = Color.fromBGR(0, 0, 0)
        if (discoType == DiscoType.GRAY || discoType == DiscoType.SMOOTH) {
            color = Color.fromRGB(255, 0, 0)
        }
        this.playersColor[uniqueId] = color
    }
}