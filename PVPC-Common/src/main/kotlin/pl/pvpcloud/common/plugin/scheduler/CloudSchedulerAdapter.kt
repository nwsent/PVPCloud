package pl.pvpcloud.common.plugin.scheduler

import pl.pvpcloud.common.CommonPlugin
import java.util.concurrent.Executor

class CloudSchedulerAdapter(val plugin: CommonPlugin) : AbstractCloudScheduler(), SchedulerAdapter {

    private var sync: Executor

    init {
        sync = Executor { r: Runnable -> this.plugin.server.scheduler.scheduleSyncDelayedTask(this.plugin, r) }
    }

    override fun sync(): Executor {
        return this.sync
    }
}