package pl.pvpcloud.xvsx.arena.adapters

import com.google.common.util.concurrent.ThreadFactoryBuilder
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.xvsx.arena.XvsXPlugin
import pl.pvpcloud.xvsx.common.packets.PacketPrepareMatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PrepareMatchAdapter(private val plugin: XvsXPlugin) : INatsAdapter {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor(ThreadFactoryBuilder().setNameFormat("Match Creator Executor").build())

    override fun received(id: String, packet: Any) {
        if (packet is PacketPrepareMatch) {
            this.executor.execute {
                this.plugin.matchManager.createMatch(packet)
            }
        }
    }

}