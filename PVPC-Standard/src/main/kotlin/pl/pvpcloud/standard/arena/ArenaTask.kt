package pl.pvpcloud.standard.arena

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.extensions.sendActionBar
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.standard.arena.ArenaState.*
import pl.pvpcloud.standard.helper.BorderHelper
import pl.pvpcloud.standard.helper.FireworkHelper
import pl.pvpcloud.standard.user.UserState
import java.sql.Time
import java.util.concurrent.TimeUnit

class ArenaTask(private val arena: Arena) : BukkitRunnable() {

    init {
        runTaskTimer(this.arena.plugin, 0, 20)
    }

    override fun run() {
        when (this.arena.arenaState) {
            WAITING -> {
                val playerA = Bukkit.getPlayer(this.arena.players[0])
                val playerB = Bukkit.getPlayer(this.arena.players[1])
                Bukkit.getOnlinePlayers()
                        .asSequence()
                        .filter { playerA.uniqueId != it.uniqueId && playerB.uniqueId != it.uniqueId }
                        .forEach {
                            this.arena.getPlayers()
                                    .forEach { players ->
                                        players.hidePlayer(it)
                                    }
                        }

                this.arena.getPlayers()
                        .forEach {
                            this.arena.plugin.kitManager.giveKit(it, arena.kitType, false)
                            BorderHelper.setBorder(it, playerA.location, 30)
                        }
                this.arena.arenaState = STARTING
            }
            STARTING -> {
                this.arena.getPlayers()
                        .forEach {
                            it.sendTitle("", "&eStart: &f${this.arena.timeToStart}", 0, 30, 0)
                            if (this.arena.timeToStart == 0) {
                                it.sendTitle("", "&aGo! Go! Go!", 0, 20, 0)

                                val user = this.arena.plugin.userManager.findUser(it.uniqueId)
                                user.userState = UserState.FIGHT

                                this.arena.arenaState = FIGHTING
                            }
                        }
                this.arena.timeToStart -= 1
            }
            FIGHTING -> {
                if (this.arena.timeArena == TimeUnit.MINUTES.toMillis(1)) {
                    this.arena.getPlayers()
                            .forEach {
                                it.sendFixedMessage(" &8* &eRozpoczęto dogrywkę... &8(&61min&8) &8*")
                            }
                }
                if (this.arena.timeArena > TimeUnit.MINUTES.toMillis(2)) {
                    this.arena.timeToEnd -= 1
                    if (this.arena.timeToEnd == 0) {
                        this.arena.arenaState = ENDING
                    }
                    this.arena.getPlayers()
                            .forEach {
                                return it.sendActionBar("&8* &eGra zakończy się za: &f${this.arena.timeToEnd} &8*")
                            }
                }
                this.arena.getPlayers()
                        .forEach {
                            it.sendActionBar("&8* &eRozgrywka trwa: &f${DataHelper.parseLong(this.arena.timeArena, true)} &8*")
                        }
                this.arena.timeArena += TimeUnit.SECONDS.toMillis(1)
            }
            ENDING -> {
                this.arena.getPlayers()
                        .forEach {
                            val user = this.arena.plugin.userManager.findUser(it.uniqueId)

                            if (this.arena.timeToEnd == 0) {
                                it.sendTitle("", "&cGra została zakonczona.", 0, 20, 0)
                            } else {
                                it.sendTitle("", "&aWygrałeś!", 0, 20, 0)
                            }
                            it.health = 20.0
                            user.arenaUUID = null
                            user.userState = UserState.WAIT
                            this.arena.plugin.kitManager.giveKit(it, user.kitType, true)
                            FireworkHelper.spawnFirework(it.location)
                            Bukkit.getOnlinePlayers().forEach { all -> it.showPlayer(all) }
                            BorderHelper.setBorder(it, this.arena.plugin.arenaManager.getSpawnLocation(), 100)

                        }
                this.arena.plugin.arenaManager.removeArena(this.arena.arenaUUID)
                this.cancel()
            }
        }

    }

}