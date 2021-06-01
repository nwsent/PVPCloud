package pl.pvpcloud.standard.helper

import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder.EnumWorldBorderAction
import net.minecraft.server.v1_8_R3.WorldBorder
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import pl.pvpcloud.standard.arena.Arena

object BorderHelper {

    fun setBorder(player: Player, center: Location, radius: Int) {
        val border = WorldBorder()
        border.setCenter(center.x, center.z)
        border.size = radius.toDouble()

        val setCenter = PacketPlayOutWorldBorder(border, EnumWorldBorderAction.SET_CENTER)
        val setSize = PacketPlayOutWorldBorder(border, EnumWorldBorderAction.SET_SIZE)

        val craftPlayer = player as CraftPlayer
        craftPlayer.handle.playerConnection.sendPacket(setCenter)
        craftPlayer.handle.playerConnection.sendPacket(setSize)
    }

}