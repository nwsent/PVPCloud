package pl.pvpcloud.ffa.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.ffa.FFAPlugin

class SpawnCommand(private val plugin: FFAPlugin) : BaseCommand() {

    @CommandAlias("spawn")
    @Syntax("<gracz>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.hub")
    fun onCommand(sender: Player, @Optional @Flags("other") other: Player?) {
        val target = other ?: sender
        this.plugin.arenaManager.getArenaByPlayer(target).handleJoin(target)
        target.sendTitle("", "&8* &cPrzeteleportowano na spawn'a! &8*", 5, 30, 5)
    }

}