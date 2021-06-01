package pl.pvpcloud.guild.impl.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.guild.api.packet.PacketGuildUpdate
import pl.pvpcloud.guild.api.structure.GuildFactory
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.guild.impl.GuildsPlugin
import pl.pvpcloud.guild.impl.structure.GuildImpl
import pl.pvpcloud.guild.impl.structure.GuildMemberImpl
import pl.pvpcloud.nats.NetworkAPI
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

@CommandAlias("ga")
@CommandPermission("guild.command.ga")
class GuildAdminCommands(private val guildRepository: GuildRepository, private val guildFactory: GuildFactory, private val plugin: GuildsPlugin) : BaseCommand() {

    @Subcommand("usun")
    @Syntax("<tag>")
    fun onRemove(sender: Player, tag: String) {
        val guild = this.guildRepository.getGuildByTag(tag)
                ?: return sender.sendFixedMessage("&4Ups! &fNie ma takiej gildii.")

        if (!this.guildFactory.removeGuild(guild.guildId, guild.leaderUUID))
            return sender.sendFixedMessage("&cCos poszlo nie tak, podczas wyrzucania gracza z gildii!")

    }

    /*
    @Subcommand("przedluzrekru")
    @Syntax("<tag>")
    fun onPrzedluzRekru(sender: CommandSender, tag: String, int: Int) {
        val guild = this.guildRepository.getGuildByTag(tag)
                ?: return sender.sendFixedMessage("&4Ups! &fNie ma takiej gildii.")
        if (!this.guildRepository.guildRekru.containsKey(guild.guildId)) {
            this.guildRepository.guildRekru[guild.guildId] = guild
        }
        guild.timeRecruitment += TimeUnit.DAYS.toMillis(int.toLong())
    }


     */

    @Subcommand("fix")
    fun onFixGuilds(sender: Player) {
        for (guild in this.guildRepository.guildsMap.values) {
            guild.coins = 100
            guild.timeRecruitment = 0L
            guild.messageRecruitment = ""
            (guild as GuildImpl).updateEntity()
        }
    }

    @Subcommand("szukaj")
    @Syntax("<player>")
    fun onSearchPlayer(sender: Player, target: String) {
        val guild = this.guildRepository.getGuildBy { it.members.singleOrNull { it.name == target } != null }
                ?: return sender.sendFixedMessage("&4Ups! &fNie ma takiej osoby.")

        sender.sendFixedMessage("&7» &fOsoba: &e${target} &fjest w gildi: ${guild.tag}")
    }

    @Subcommand("list")
    fun onList(sender: Player) {

        sender.sendFixedMessage("&7» &fGildie &8(&c${this.guildRepository.guildsMap.size}&8): " +
                "&f${this.guildRepository.guildsMap.values.asSequence().joinToString("") { "${it.tag}&8, &f" }} ")

    }

    @CommandAlias("stat")
    @Syntax("<typ> <tag> <rodzaj> <wartosc>")
    @CommandPermission("tools.command.astatistic")
    fun onAStats(sender: Player, typ: String, tag: String, type: String, value: Int) {

        val guild = this.guildRepository.getGuildByTag(tag)
                ?: return sender.sendFixedMessage("&4&lUpsik! &fNie ma takiej gildii.")

        guild.also {
            when (typ) {
                "set" ->
                    when (type) {
                        "kills" ->
                            it.kills = value
                        "deaths" ->
                            it.deaths = value
                        "points" ->
                            it.points = value
                    else ->
                        return sender.sendFixedMessage("&4Upsik! &fNie ma takiego typu")
                }
                "remove" ->
                    when (type) {
                        "kills" ->
                            it.kills -= value
                        "deaths" ->
                            it.deaths -= value
                        "points" ->
                            it.points -= value
                    else ->
                        return sender.sendFixedMessage("&4Upsik! &fNie ma takiego typu")
                }
                "add" ->
                    when (type) {
                        "kills" ->
                            it.kills += value
                        "deaths" ->
                            it.deaths += value
                        "points" ->
                            it.points += value
                        else ->
                            return sender.sendFixedMessage("&4Upsik! &fNie ma takiego typu")
                }
                else ->
                    return sender.sendFixedMessage("kurwa mordo jest tylko add/remove/set")
            }
            NetworkAPI.publish { PacketGuildUpdate(it) }
        }


    }

    @Subcommand("help|pomoc")
    @CatchUnknown
    @Default
    fun onHelp(sender: CommandSender) =
            sender.sendFixedMessage(
                    "&8* &c/ga list/usun/stat/szukaj"
            )

}
