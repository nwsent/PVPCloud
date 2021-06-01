package pl.pvpcloud.auth.tasks

import pl.pvpcloud.auth.AuthPlugin
import pl.pvpcloud.common.extensions.sendFixedMessage

class StatusLoginTask(private val plugin: AuthPlugin) : Runnable {

    override fun run() {
        for (serverInfo in plugin.proxy.servers.values.filter { it.name.equals(this.plugin.config.serversHubName, ignoreCase = true) }) {
            for (player in serverInfo.players) {
                val authPlayer = plugin.authRepository.getAuthPlayer(player.name)
                        ?: continue
                when {
                    !authPlayer.isRegistered() -> player.sendFixedMessage("&c/register (haslo) (haslo)")
                    !authPlayer.isLogged -> player.sendFixedMessage("&c/login (haslo)")
                }
            }
        }
    }
}