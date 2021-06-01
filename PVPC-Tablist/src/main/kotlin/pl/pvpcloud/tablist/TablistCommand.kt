package pl.pvpcloud.tablist

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage

class TablistCommand(private val plugin: TablistPlugin) : BaseCommand() {

    @CommandAlias("tabreload")
    @CommandPermission("tablist.reload")
    fun onPlayerReloadTablist(player: Player) {
        this.plugin.reload()
        player.sendFixedMessage("&aPrzeladowales tabliste!")
    }

}