package pl.pvpcloud.auth.helpers

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.config.ServerInfo
import pl.pvpcloud.auth.AuthPlugin
import java.util.concurrent.ThreadLocalRandom

class ServersHelper(private val plugin: AuthPlugin) {

    private val onlineServers = arrayListOf<ServerInfo>()

    init {
        setFreeServers()
    }

    fun setFreeServers() {
        onlineServers.clear()
        val servers = ProxyServer.getInstance().servers.values
        for (info in servers) {
            if (!info.name.contains(this.plugin.config.serversHubName)) continue
            info.ping { _, e ->
                if (e == null) {
                    onlineServers.add(info)
                }
            }
        }
    }

    fun getRandomAuthServers(): ServerInfo {
        val list = onlineServers
        val random = ThreadLocalRandom.current()
        return list[random.nextInt(onlineServers.size)]
    }

    fun getOnlineServers(): List<ServerInfo> {
        return onlineServers
    }
}