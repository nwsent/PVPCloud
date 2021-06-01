package pl.pvpcloud.auth.tasks

import pl.pvpcloud.auth.AuthPlugin

class OnlineServersTask(private val plugin: AuthPlugin) : Runnable {

    override fun run() {
        this.plugin.serversHelper.setFreeServers()
    }
}