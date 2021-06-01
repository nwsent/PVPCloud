package pl.pvpcloud.statistics.server.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.statistics.StatisticsPlugin
import pl.pvpcloud.statistics.server.repository.StatsRepositoryServer
import pl.pvpcloud.tools.ToolsAPI

class ResetStatsCommand(private val plugin: StatisticsPlugin) : BaseCommand() {

    @CommandAlias("resetujranking")
    @CommandPermission("command.statistic.ranking")
    fun onCommandRestartStats(sender: Player) {
        val rank = plugin.statsRepository.getPlayerStatsByUUID(sender.uniqueId)
                ?: return sender.sendFixedMessage("&4Ups! &fNie ma takiego gracza w bazie danych")

        val user = ToolsAPI.findUserByUUID(sender)
        if (user.coins < 250)
            return sender.sendFixedMessage("&4Ups! &fNie masz 250 monet &8(&e${250 - user.coins}&8)")

        user.removeCoins(250)
        (this.plugin.statsRepository as StatsRepositoryServer).resetPlayerStats(sender)
        sender.sendFixedMessage("&aGratulacje! &fZresetowałeś swój ranking")
    }
}