package pl.pvpcloud.tablist

import com.keenant.tabbed.Tabbed
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin

class TablistPlugin : CloudPlugin() {

    private lateinit var tabbed: Tabbed
    private lateinit var manager: TablistManager
    lateinit var tabConfig: TablistConfig

    override fun onEnable() {
        this.initCommands()

        this.tabConfig = ConfigLoader.load(this.dataFolder, TablistConfig::class, "config")

        this.tabbed = Tabbed(this)
        this.manager = TablistManager(this.tabbed, this)

        this.registerCommands(TablistCommand(this))

        Bukkit.getPluginManager().registerEvents(TablistListener(this, this.manager), this)

        if (this.tabConfig.isEnabled) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(this, TablistTask(this, this.manager), 0, this.tabConfig.refresh)
        }

        TablistAPI.manager = this.manager
        TablistAPI.registerVariable(object : TablistAdapter("name") {
            override fun replace(player: Player): String {
                return player.name
            }
        })
    }

    fun reload() {
        this.tabConfig = ConfigLoader.load(this.dataFolder, TablistConfig::class, "config")
    }

}