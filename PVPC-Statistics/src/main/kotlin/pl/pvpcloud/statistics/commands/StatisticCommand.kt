package pl.pvpcloud.statistics.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.statistics.StatisticsPlugin

class StatisticCommand(private val plugin: StatisticsPlugin) : BaseCommand() {

    @CommandAlias("stats|ranking|gracz")
    @Syntax("<gracz>")
    @CommandPermission("command.statistic.ranking")
    fun onCommandRankingInfo(sender: Player, @Optional @Flags("other") other: String?) {
        val rank = (if (other != null) plugin.statsRepository.getPlayerStatsByNick(other) else plugin.statsRepository.getPlayerStatsByUUID(sender.uniqueId))
                ?: return sender.sendFixedMessage("&4Ups! &fNie ma takiego gracza w bazie danych")

        val messages = arrayListOf(
                " &8)&m-------------&8&l|&7»  &eStatystki  &7«&8&l|&8&m-------------&r&8(",
                " ",
                " &8* &7Nick: &f${rank.name} &8(&e${rank.lvls}lvl&8)",
                " &8* &7Punkty: &f${rank.points}",
                " &8* &7Smierci: &f${rank.deaths}",
                " &8* &7Asysty: &f${rank.assists}",
                " &8* &7Zabojstwa: &f${rank.kills}",
                *plugin.config.playerStatsInfo.toTypedArray(),
                " &8)&m-------------&8&l|&7»  &eStatystki  &7«&8&l|&8&m-------------&r&8("
        )

        plugin.config.replaceCustomStatsName.forEach { (t, u) ->
            messages.replaceAll {
                it.rep(t, rank.customStats[u]?.value.toString())
            }
        }
        sender.sendFixedMessage(messages.toTypedArray())
    }

    @CommandAlias("lvlup")
    @CommandPermission("command.statistic.lvlup")
    fun onCommandLvlUp(sender: Player) {
        val rank = plugin.statsRepository.getPlayerStatsByUUID(sender.uniqueId)
                ?: return sender.sendFixedMessage("&cNie mozna zaladowac twoich statystyk!")

        if (rank.points >= 2000) {
            rank.points -= 1000
            rank.lvls++
            sender.sendTitle("", "&8* &fTwoj &elvl &fzostal zwiekszony! &8*", 10, 60, 10)
        } else {
            sender.sendFixedMessage("&7» &fTwoj &elvl &fnie zostal zwieszkony, poniewaz nie posiadasz 2000 punktow!")
        }
    }

}