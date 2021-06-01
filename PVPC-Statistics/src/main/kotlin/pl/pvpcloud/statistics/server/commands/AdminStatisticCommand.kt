package pl.pvpcloud.statistics.server.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.database.api.DatabaseEntity
import pl.pvpcloud.statistics.StatisticsPlugin
import pl.pvpcloud.statistics.StatsAPI

class AdminStatisticCommand(private val plugin: StatisticsPlugin) : BaseCommand() {

    @CatchUnknown
    @Default
    fun doHelp(sender: CommandSender) {
        sender.sendFixedMessage(arrayListOf(
                "&8* &c/astatistic <typ> <gracz> <rodzaj> <wartosc>"
        ))
    }

    @CommandAlias("fakepointsXD")
    @CommandPermission("tools.command.astatts")
    fun XDDD(sender: Player) {
        StatsAPI.findPlayerStats(sender.player).fakePoints.toString()
    }

    @CommandAlias("astatistic")
    @Syntax("<typ> <gracz> <rodzaj> <wartosc>")
    @CommandPermission("tools.command.astatistic")
    fun onAStats(sender: Player, typ: String, name: String, type: String, value: Int) {
        val rank = this.plugin.statsRepository.getPlayerStatsByNick(name)
                ?: return sender.sendFixedMessage("&4Ups! &fNie ma takiego gracza w bazie danych")


        rank.also {
            when (typ) {
                "set" ->
                    when (type) {
                        "kills" ->
                            it.kills = value
                        "deaths" ->
                            it.deaths = value
                        "points" ->
                            it.points = value
                        "lvl" ->
                            it.lvls = value
                        else ->
                            return sender.sendFixedMessage("&4Ups! &fNie ma takiego typu")
                    }
                "remove" ->
                    when (type) {
                        "kills" ->
                            it.kills -= value
                        "deaths" ->
                            it.deaths -= value
                        "points" ->
                            it.points -= value
                        "lvl" ->
                            it.lvls -= value
                        else ->
                            return sender.sendFixedMessage("&4Ups! &fNie ma takiego typu")
                    }
                "add" ->
                    when (type) {
                        "kills" ->
                            it.kills += value
                        "deaths" ->
                            it.deaths += value
                        "points" ->
                            it.points += value
                        "lvl" ->
                            it.lvls += value
                        else ->
                            return sender.sendFixedMessage("&4Ups! &fNie ma takiego typu")
                    }
                else ->
                    return sender.sendFixedMessage("kurwa mordo jest tylko add/remove/set")
            }
            rank.updateEntity()
        }

    }

}