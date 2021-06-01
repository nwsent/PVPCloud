package pl.pvpcloud.fight.tasks

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendActionBar
import pl.pvpcloud.fight.FightPlugin

class FightTask(private val plugin: FightPlugin) : Runnable {

    override fun run() {
        Bukkit.getOnlinePlayers().forEach {
            val fight = this.plugin.fightManager.findFight(it.uniqueId)
            if (fight.lastAttack > System.currentTimeMillis()) {
                it.sendActionBar("&cAntyLogout: &f${((fight.lastAttack - System.currentTimeMillis()) / 1000)}")
            } else if (fight.lastAttack > 1) {
                fight.clear()
                it.sendActionBar(plugin.config.messages.actionBarEndFight)
            }
        }
    }
}