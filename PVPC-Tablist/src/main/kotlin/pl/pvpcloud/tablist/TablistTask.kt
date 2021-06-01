package pl.pvpcloud.tablist

import org.bukkit.Bukkit

class TablistTask(private val plugin: TablistPlugin, private val manager: TablistManager) : Runnable {

    override fun run() {
        if (this.manager.tablistIndex == this.plugin.tabConfig.tables.size) {
            this.manager.tablistIndex = 0
        }

        for (player in Bukkit.getOnlinePlayers()) {
            this.manager.updateTablist(player)
        }

        this.manager.tablistIndex++
    }

}