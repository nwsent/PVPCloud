package pl.pvpcloud.common.plugin.scheduler

import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.concurrent.*

abstract class AbstractCloudScheduler : SchedulerAdapter {

    private val scheduler = Executors.newSingleThreadScheduledExecutor(ThreadFactoryBuilder()
            .setDaemon(true)
            .setNameFormat("pvpcloud-scheduler")
            .build()
    )

    private val schedulerWorkerPool: ErrorReportingExecutor = ErrorReportingExecutor(Executors.newCachedThreadPool(ThreadFactoryBuilder()
            .setDaemon(true)
            .setNameFormat("pvpcloud-scheduler-worker-%d")
            .build()
    ))

    private val worker: ForkJoinPool = ForkJoinPool(32, ForkJoinPool.defaultForkJoinWorkerThreadFactory, Thread.UncaughtExceptionHandler { _, e -> e.printStackTrace() }, false)

    override fun async(): Executor {
        return this.worker
    }

    override fun asyncLater(task: Runnable, delay: Long, unit: TimeUnit): SchedulerTask {
        val future: ScheduledFuture<*> = scheduler.schedule({ schedulerWorkerPool.execute(task) }, delay, unit)
        return object : SchedulerTask {
            override fun cancel() {
                future.cancel(false)
            }
        }
    }

    override fun asyncRepeating(task: Runnable, interval: Long, unit: TimeUnit): SchedulerTask {
        val future: ScheduledFuture<*> = scheduler.scheduleAtFixedRate({ schedulerWorkerPool.execute(task) }, interval, interval, unit)
        return object : SchedulerTask {
            override fun cancel() {
                future.cancel(false)
            }
        }
    }

    override fun shutdownScheduler() {
        scheduler.shutdown()
        try {
            scheduler.awaitTermination(1, TimeUnit.MINUTES)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun shutdownExecutor() {
        schedulerWorkerPool.delegate.shutdown()
        try {
            schedulerWorkerPool.delegate.awaitTermination(1, TimeUnit.MINUTES)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    class ErrorReportingExecutor(val delegate: ExecutorService) : Executor {
        override fun execute(command: Runnable) {
            delegate.execute(ErrorReportingRunnable(command))
        }
    }

    class ErrorReportingRunnable constructor(private val delegate: Runnable) : Runnable {
        override fun run() {
            try {
                delegate.run()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


