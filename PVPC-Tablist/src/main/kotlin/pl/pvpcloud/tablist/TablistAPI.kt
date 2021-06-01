package pl.pvpcloud.tablist

import org.bukkit.Bukkit
import org.bukkit.event.Listener

object TablistAPI {

    internal lateinit var manager: TablistManager

    fun registerVariable(adapter: TablistAdapter) =
            this.manager.registerVariable(adapter)

    fun registerVariables(vararg adapter: TablistAdapter) =
        adapter.forEach { registerVariable(it) }

}