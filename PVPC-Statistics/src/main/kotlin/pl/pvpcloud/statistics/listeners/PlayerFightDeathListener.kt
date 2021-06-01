package pl.pvpcloud.statistics.listeners

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.common.ranking.RankingAlgorithm
import pl.pvpcloud.fight.enums.FightKillerType
import pl.pvpcloud.fight.event.PlayerFightDeathEvent
import pl.pvpcloud.guild.api.GuildsAPI
import pl.pvpcloud.guild.api.packet.PacketGuildUpdate
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.statistics.StatisticsPlugin
import pl.pvpcloud.statistics.StatsAPI
import pl.pvpcloud.tools.ToolsAPI
import pl.pvpcloud.tools.ToolsAPI.addKillCoins
import pl.pvpcloud.tools.ToolsAPI.removeDeathsCoins
import java.util.*
import kotlin.math.roundToInt

class PlayerFightDeathListener(private val plugin: StatisticsPlugin) : Listener {

    private val killerRankingAlgorithm = RankingAlgorithm(86)
    private val victimRankingAlgorithm = RankingAlgorithm(67)

    @EventHandler
    fun onFightDeath(event: PlayerFightDeathEvent) {
        val victim = event.player
        victim.sendTitle("&4R I P", "", 10, 50, 10)
        victim.world.strikeLightningEffect(victim.location)

        val victimStats = StatsAPI.findPlayerStats(victim)

        if (event.killerType == FightKillerType.OTHER) {
            victimStats.deaths++
        } else {
            val killerFightStats = event.fightStats[0]

            val killerStats = StatsAPI.findPlayerStats(killerFightStats.uniqueId)

            val oldKillerPoints: Int = killerStats.points
            val oldVictimPoints: Int = victimStats.points

            val newVictimPoints: Int = this.victimRankingAlgorithm.calculateVictim(oldKillerPoints, oldVictimPoints)
            val newKillerPoints: Int = this.killerRankingAlgorithm.calculateKiller(oldKillerPoints, oldVictimPoints)

            var plus = newKillerPoints - oldKillerPoints
            var minus = oldVictimPoints - newVictimPoints

            if (plus > 36) plus = 35
            if (minus > 31) minus = 30

            if (minus < 6) minus = 5
            if (plus < 11) plus = 10

            killerStats.kills++
            victimStats.deaths++

            victimStats.points -= minus
            killerStats.points += plus

            killerStats.uuid.addKillCoins()
            victimStats.uuid.removeDeathsCoins()


            val killer = Bukkit.getPlayer(killerFightStats.uniqueId)
            var health = 20
            if (killer != null) {
                health = killer.health.roundToInt()
                killer.sendTitle("", "&fZabójstwo&8: &3${victim.name} &8(&a+&f${plus}&a&opkt &f${killerFightStats.totalDamageGiven}&c&odmg&8)", 5, 40, 5)
            }
            val deathMessage = "&8* &e${victimStats.name} &8(&c-&f$minus&8) &7zabity przez&8: &e${killerStats.name} &8(&a+&f$plus&a&opkt &c$health&c❤&8)"
            killer.sendFixedMessage(deathMessage)
            victim.sendFixedMessage(deathMessage)

            val assistMessages = hashMapOf<UUID, String>()

            if (event.fightStats.isNotEmpty()) {
                event.fightStats.filter { it.uniqueId != killerStats.uuid }
                        .forEach {
                            val assistPlus = ((plus * it.percentage) / 100).toInt()
                            val assistStats = StatsAPI.findPlayerStats(it.uniqueId)
                            assistStats.points += assistPlus
                            assistStats.assists++

                            val assistOfflinePlayer = Bukkit.getOfflinePlayer(it.uniqueId)
                            val assistMessage = "    &8* &e${assistOfflinePlayer.name} &8(&a+&f${assistPlus}&a&opkt&8)"
                            assistMessages[assistOfflinePlayer.uniqueId] = assistMessage

                            val assistPlayer = assistOfflinePlayer.player

                            if (assistPlayer != null) {
                                assistPlayer.sendTitle("", "&fAsysta przy&8: &e${victim.name} &8(&a+&f${assistPlus}&a&opkt &f${killerFightStats.totalDamageGiven}&c&odmg&8)", 5, 40, 5)
                                ToolsAPI.addCoins(assistPlayer.uniqueId, 1)
                                assistPlayer.sendFixedMessage(deathMessage)
                                assistPlayer.sendFixedMessage(assistMessage)
                            }
                        }
            }

            killer.world.players.forEach { player ->
                if (player.uniqueId == victimStats.uuid || player.uniqueId == killerStats.uuid || assistMessages.containsKey(player.uniqueId)) {
                    return@forEach
                }

                val user = ToolsAPI.findUserByUUID(player)
                if (!user.settings.ignoreDeathMessage) {
                    player.sendFixedMessage(deathMessage)
                    assistMessages.values.forEach { player.sendFixedMessage(it) }
                }
            }

            if (killerStats.points > 2000) {
                killerStats.points -= 1000
                killerStats.lvls++
                killer.sendTitle("", "&8* &fTwoj &elvl &fzostal zwiekszony! &8*", 10, 60, 10)
                killer.sendFixedMessage(arrayListOf("","&8* &fTwoj &elvl &fzostal zwiekszony! &8*", ""))
                killer.playSound(killer.location, Sound.LEVEL_UP, 1f, 1f)
            }

            GuildsAPI.also {
                it.getGuildByMember(victim.uniqueId).also { guild ->
                    if (guild != null) {
                        var minusGuild = (minus * 0.4).toInt()
                        if (minusGuild > 24)
                            minusGuild = 24

                        guild.points -= minusGuild
                        guild.deaths++
                        NetworkAPI.publish { PacketGuildUpdate(guild) }
                    }
                }

                it.getGuildByMember(killer.uniqueId).also { guild ->
                    if (guild != null) {
                        var plusGuild = (plus * 0.6).toInt()
                        if (plusGuild > 30)
                            plusGuild = 30

                        guild.kills++
                        guild.points += plusGuild
                        NetworkAPI.publish { PacketGuildUpdate(guild) }
                    }
                }
            }

        }
    }

}