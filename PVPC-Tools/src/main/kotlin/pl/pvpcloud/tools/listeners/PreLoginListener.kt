package pl.pvpcloud.tools.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.helpers.PlayerHelper
import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.chat.PacketGlobalMessage
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.basic.User
import pl.pvpcloud.tools.extensions.sendUpdate

class PreLoginListener(private val plugin: ToolsPlugin) : Listener {

    @EventHandler
    fun onPreLogin(event: AsyncPlayerPreLoginEvent) {
        if (event.loginResult != AsyncPlayerPreLoginEvent.Result.ALLOWED) return
        DatabaseAPI.loadBySelector<User>("tools-users", event.uniqueId.toString(), "uuid") {
            if (it == null) {
                this.plugin.userManager.createUser(event.uniqueId, event.name)
                print("Create user ${event.name}")
            } else {
                if (this.plugin.userManager.getUserByUUID(event.uniqueId) == null) {
                    if (it.name != event.name) {
                        it.name = event.name
                    }
                    this.plugin.userManager.addUser(it)
                    it.sendUpdate()
                    //Dałem to tutaj bo jak nie ma rangi to zwracam event i może się rozjebać :]
                    val message = this.plugin.config.welcomeMessage[PlayerHelper.getGroup(event.uniqueId)]
                            ?: return@loadBySelector
                    NetworkAPI.publish { PacketGlobalMessage(message.rep("%name%", event.name).fixColors(), "-") }
                }
            }
        }
    }

}