package pl.pvpcloud.standard.user

import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.standard.StandardPlugin

class UserRemoveInvitesTask(private val plugin: StandardPlugin) : BukkitRunnable() {

    init {
        runTaskTimer(this.plugin, 0, 240)
    }

    override fun run() {
        this.plugin.userManager.getUsers().values.forEach {
            it.invites.clear()
        }
    }

}