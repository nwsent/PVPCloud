package pl.pvpcloud.common.plugin.scheduler

import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

interface SchedulerAdapter {

    fun async(): Executor

    fun sync(): Executor

    fun executeAsync(task: Runnable) {
        async().execute(task)
    }

    fun executeSync(task: Runnable) {
        sync().execute(task)
    }

    fun asyncLater(task: Runnable, delay: Long, unit: TimeUnit): SchedulerTask

    fun asyncRepeating(task: Runnable, interval: Long, unit: TimeUnit): SchedulerTask

    fun shutdownScheduler()

    fun shutdownExecutor()
}