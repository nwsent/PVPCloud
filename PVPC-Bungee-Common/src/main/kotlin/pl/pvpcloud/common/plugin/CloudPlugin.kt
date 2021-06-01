package pl.pvpcloud.common.plugin

import co.aikar.commands.BaseCommand
import co.aikar.commands.BungeeCommandManager
import co.aikar.commands.ExceptionHandler
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import pl.pvpcloud.common.extensions.fixColors
import java.util.*

abstract class CloudPlugin : Plugin() {

    private lateinit var bungeeCommandManager: BungeeCommandManager

    fun initCommands() {
        this.bungeeCommandManager = BungeeCommandManager(this)
        this.bungeeCommandManager.defaultExceptionHandler = ExceptionHandler { command, _, sender, _, _ ->
            sender.sendMessage("&cWystapil problem z komenda ${command.name}, zglos to jak najszybciej do admina :)".fixColors())
            true
        }
        this.bungeeCommandManager.addSupportedLanguage(Locale("pl"))
        this.bungeeCommandManager.locales.defaultLocale = Locale("pl")
    }

    private fun <T> registerCommand(command: T) where T : BaseCommand {
        this.bungeeCommandManager.registerCommand(command)
    }

    fun <T> registerCommands(vararg commands: T) where T : BaseCommand {
        commands.forEach { registerCommand(it) }
    }

    fun registerListeners(vararg listeners: Listener) {
        listeners.forEach { this.proxy.pluginManager.registerListener(this, it) }
    }
}