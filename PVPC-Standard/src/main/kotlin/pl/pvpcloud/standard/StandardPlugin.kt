package pl.pvpcloud.standard

import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.scoreboard.ScoreboardAPI
import pl.pvpcloud.standard.arena.ArenaManager
import pl.pvpcloud.standard.arena.listener.PlayerArenaGameListener
import pl.pvpcloud.standard.arena.listener.PlayerProtectingArenaListener
import pl.pvpcloud.standard.arena.listener.PlayerSearchArenaListener
import pl.pvpcloud.standard.kit.KitListener
import pl.pvpcloud.standard.kit.KitManager
import pl.pvpcloud.standard.scoreboard.EventScoreboardProvider
import pl.pvpcloud.standard.user.UserListener
import pl.pvpcloud.standard.user.UserManager
import pl.pvpcloud.standard.user.UserRemoveInvitesTask

class StandardPlugin : CloudPlugin() {

    lateinit var kitManager: KitManager
    lateinit var userManager: UserManager
    lateinit var arenaManager: ArenaManager

    override fun onEnable() {

        this.kitManager = KitManager(this)
        this.userManager = UserManager(this)
        this.arenaManager = ArenaManager(this)

        this.registerListeners(
                KitListener(this),
                UserListener(this),
                PlayerArenaGameListener(this),
                PlayerProtectingArenaListener(this),
                PlayerSearchArenaListener(this)
        )
        ScoreboardAPI.setDefaultScoreboardProvider(EventScoreboardProvider(this))
        UserRemoveInvitesTask(this)
    }

}