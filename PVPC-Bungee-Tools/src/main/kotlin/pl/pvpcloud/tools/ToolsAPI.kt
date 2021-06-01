package pl.pvpcloud.tools

object ToolsAPI {

    internal lateinit var plugin: ToolsPlugin

    fun isAllowConnect(): Boolean {
        this.plugin.limitConnectionTask.pings++
        if (this.plugin.limitConnectionTask.pings > 4) {
            this.plugin.limitConnectionTask.isConnect = false
        }
        return this.plugin.limitConnectionTask.isConnect
    }
}