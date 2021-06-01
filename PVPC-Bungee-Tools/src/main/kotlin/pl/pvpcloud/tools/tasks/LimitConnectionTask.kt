package pl.pvpcloud.tools.tasks

import pl.pvpcloud.tools.ToolsPlugin

class LimitConnectionTask(private val plugin: ToolsPlugin) : Runnable {

    var pings = 0
    var isConnect = true

    override fun run() {
        this.pings = 0
        this.isConnect = true
    }
}