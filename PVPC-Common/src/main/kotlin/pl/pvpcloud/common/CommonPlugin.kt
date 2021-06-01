package pl.pvpcloud.common

import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.plugin.java.JavaPlugin
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.helpers.PlayerHelper
import pl.pvpcloud.common.plugin.DisablePlayerData
import pl.pvpcloud.common.plugin.scheduler.CloudSchedulerAdapter
import pl.pvpcloud.common.plugin.scheduler.SchedulerAPI

class CommonPlugin : JavaPlugin() {

    lateinit var luckPermsApi: LuckPerms

    override fun onEnable() {

        DisablePlayerData()

        try {
            val provider = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)
                    ?: throw NullPointerException("LuckPerms not load :O")
            this.luckPermsApi = provider.provider
        } catch (ex: NullPointerException) {
            ex.printStackTrace()
        }

        PlayerHelper.plugin = this

//        SchedulerAPI.schedulerAdapter = CloudSchedulerAdapter(this)
    }

    override fun onDisable() {
//        SchedulerAPI.schedulerAdapter.shutdownExecutor()
//        SchedulerAPI.schedulerAdapter.shutdownScheduler()
    }
}