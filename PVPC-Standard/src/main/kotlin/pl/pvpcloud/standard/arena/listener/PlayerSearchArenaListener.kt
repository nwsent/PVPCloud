package pl.pvpcloud.standard.arena.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.standard.StandardPlugin
import pl.pvpcloud.standard.arena.Arena
import pl.pvpcloud.standard.arena.ArenaState
import pl.pvpcloud.standard.helper.BorderHelper
import pl.pvpcloud.standard.user.UserState

class PlayerSearchArenaListener(private val plugin: StandardPlugin) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEvent(event: EntityDamageByEntityEvent) {

        val sender = event.damager as Player
        val senderUser = this.plugin.userManager.findUser(sender.uniqueId)
        if (senderUser.arenaUUID != null) {
            if (senderUser.userState === UserState.FIGHT) return
            val arena = this.plugin.arenaManager.getArena(senderUser.arenaUUID!!)
            if (arena.arenaState !== ArenaState.FIGHTING) {
                event.isCancelled = true
                return
            }
        } else {
            event.isCancelled = true
        }

        val target = event.entity as Player
        val targetUser = this.plugin.userManager.findUser(target.uniqueId)
        if (senderUser.kitType != targetUser.kitType)
            return sender.sendFixedMessage("&4Ups! &fGracz ma inny kit")

        if (targetUser.arenaUUID != null)
            return sender.sendFixedMessage("&4Ups! &fZnajdz sobie inna osobe on juz jest zajęty.")

        if (targetUser.invites.contains(sender.uniqueId))
            return sender.sendFixedMessage("&4Ups! &fZaprosiłeś już tego gracza!")

        if (target.uniqueId in senderUser.invites) {
            target.sendFixedMessage(arrayListOf("", " &7» &aTwoje zaproszenie zostało zakceptowane przez: &f${sender.name}", ""))
            sender.sendFixedMessage(arrayListOf("", " &7» &aZakceptowales zaproszenie od: &f${target.name}"))

            val arena = Arena(this.plugin, arrayListOf(targetUser.uuid, senderUser.uuid), senderUser.kitType)

            targetUser.arenaUUID = arena.arenaUUID
            senderUser.arenaUUID = arena.arenaUUID

            this.plugin.arenaManager.addArena(arena)
            return
        }
        sender.sendFixedMessage(" &7» &aWysłałeś graczowi: &f${target.name} &azaproszenie do walki.")
        target.sendFixedMessage(
                arrayListOf(
                        "",
                        " &7» &eOtrzymales zaproszenie do walki z &f${sender.name}",
                        "     &8* &fAby zakceptować uderz go!",
                        ""
                ))
        targetUser.invites.add(sender.uniqueId)
    }


}