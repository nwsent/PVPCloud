package pl.pvpcloud.xvsx.hub.queues

import pl.pvpcloud.xvsx.hub.XvsXPlugin

class QueueThread(private val plugin: XvsXPlugin) : Thread() {

    init {
        this.name = "PVPCloud-Queue"
        this.start()
    }

    override fun run() {
        while (true) {

            for (queueWait in this.plugin.queueManager.queuesWait.values) {
                try {
                    queueWait.doTick()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            try {
                sleep(1000L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

}