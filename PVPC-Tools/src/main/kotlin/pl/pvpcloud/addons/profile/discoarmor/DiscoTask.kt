package pl.pvpcloud.addons.profile.discoarmor

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.addons.AddonsModule
import pl.pvpcloud.addons.profile.discoarmor.DiscoType.*
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.tools.ToolsAPI

class DiscoTask(private val addonsModule: AddonsModule): BukkitRunnable() {

    init {
        runTaskTimerAsynchronously(this.addonsModule.plugin, 10, 5)
    }

    override fun run() {
        if (this.addonsModule.config.addonsEnabled) {
            for (player in Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("addons.disco")) {
                    continue
                }
                val user = ToolsAPI.findUserByUUID(player)
                if (user.settings.discoType == "-") {
                    continue
                }
                val discoType = valueOf(user.settings.discoType)
                var lastColor = this.addonsModule.discoManager.getPlayerColor(player.uniqueId)
                    ?: continue
                val packets = arrayListOf<PacketContainer>()
                when (discoType) {
                    GRAY -> {
                        this.addonsModule.discoManager.setColor(player.uniqueId, DiscoHelper.nextGrayColor(lastColor))
                        for (i in 1..4) {
                            packets.add(getPacket(player.entityId, i, lastColor))
                        }
                    }
                    ULTRA -> {
                        this.addonsModule.discoManager.setColor(player.uniqueId, DiscoHelper.randomColor())
                        for (i in 1..4) {
                            packets.add(getPacket(player.entityId, i, lastColor))
                            lastColor = DiscoHelper.randomColor()
                        }
                    }
                    RANDOM -> {
                        this.addonsModule.discoManager.setColor(player.uniqueId, DiscoHelper.randomColor())
                        for (i in 1..4) {
                            packets.add(getPacket(player.entityId, i, lastColor))
                        }
                    }
                    SMOOTH -> {
                        this.addonsModule.discoManager.setColor(player.uniqueId, DiscoHelper.nextColor(lastColor))
                        for (i in 1..4) {
                            packets.add(getPacket(player.entityId, i, lastColor))
                        }
                    }
             /*       POLICE -> {
                    this.addonsModule.discoManager.setColor(player.uniqueId, DiscoHelper.policeColor(lastColor))
                            for (i in 1..4) {
                                packets.add(getPacket(player.entityId, i, lastColor))
                            }
                    }
                    FLEX -> {
                        for (i in 1..4) {
                            for (mat in 310..317) {
                                packets.add(getArmor(player.entityId, i, mat))
                            }
                        }
                    }

              */
                }
                for (players in player.world.players) {
                    if (players.location.distance(player.location) <= 225) {
                        if (players.uniqueId != player.uniqueId) {
                            packets.forEach {
                                this.addonsModule.protocolManager.sendServerPacket(players, it)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getPacket(entityId: Int, slot: Int, color: Color): PacketContainer {
        val packetContainer = addonsModule.protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT)
        packetContainer.integers.write(0, entityId).write(1, slot)
        packetContainer.itemModifier.write(0, getItem(color, (297 + slot)))
        return packetContainer
    }

    private fun getArmor(entityId: Int, slot: Int, material: Int): PacketContainer {
        val packetContainer = addonsModule.protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT)
        packetContainer.integers.write(0, entityId).write(1, slot)
        packetContainer.itemModifier.write(0, ItemStack(Material.getMaterial(material)))
        return packetContainer
    }

    private fun getItem(color: Color, slot: Int): ItemStack {
        val itemStack = ItemStack(Material.getMaterial(slot), 1)
        val leatherArmorMeta = itemStack.itemMeta as LeatherArmorMeta
        leatherArmorMeta.color = color
        itemStack.itemMeta = leatherArmorMeta
        return itemStack
    }

}
