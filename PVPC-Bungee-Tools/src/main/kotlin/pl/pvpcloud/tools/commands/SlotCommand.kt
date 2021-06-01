package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import net.md_5.bungee.api.CommandSender
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsPlugin

@CommandAlias("slot")
@CommandPermission("tools.command.slot")
class SlotCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CatchUnknown
    @Default
    fun doHelp(sender: CommandSender) {
        sender.sendFixedMessage(arrayListOf(
                "&8* &c/slot show",
                "&8* &c/slot online"
        ))
    }

    @Subcommand("show")
    fun onMaxShow(sender: CommandSender, maxShow: Int) {
        this.plugin.config.slotMaxShow = maxShow
        this.plugin.saveConfig()
        this.plugin.proxySync()
        sender.sendFixedMessage(plugin.config.messages.slotMaxShowSet.rep("%show%", maxShow.toString()))
    }

    @Subcommand("online")
    fun onMaxOnline(sender: CommandSender, maxOnline: Int) {
        this.plugin.config.slotMaxOnline = maxOnline
        this.plugin.saveConfig()
        this.plugin.proxySync()
        sender.sendFixedMessage(plugin.config.messages.slotMaxOnlineSet.rep("%online%", maxOnline.toString()))
    }
}