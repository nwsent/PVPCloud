package pl.pvpcloud.common.plugin

import co.aikar.commands.BaseCommand
import co.aikar.commands.ExceptionHandler
import co.aikar.commands.PaperCommandManager
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import pl.pvpcloud.common.extensions.fixColors
import java.util.*

abstract class CloudPlugin : JavaPlugin() {

    private lateinit var paperCommandManager: PaperCommandManager

    fun initCommands() {
        this.paperCommandManager = PaperCommandManager(this)
        this.paperCommandManager.defaultExceptionHandler = ExceptionHandler { command, _, sender, _, _ ->
            sender.sendMessage("&cWystapil problem z komenda ${command.name}, zglos to jak najszybciej do admina :)".fixColors())
            true
        }
        this.paperCommandManager.addSupportedLanguage(Locale("pl"))
        this.paperCommandManager.locales.defaultLocale = Locale("pl")
    }

    private fun <T> registerCommand(command: T) where T : BaseCommand {
        this.paperCommandManager.registerCommand(command)
    }

    fun <T> registerCommands(vararg commands: T) where T : BaseCommand {
        commands.forEach { registerCommand(it) }
    }

    fun registerListeners(vararg listeners: Listener) {
        listeners.forEach { Bukkit.getPluginManager().registerEvents(it, this) }
    }

}