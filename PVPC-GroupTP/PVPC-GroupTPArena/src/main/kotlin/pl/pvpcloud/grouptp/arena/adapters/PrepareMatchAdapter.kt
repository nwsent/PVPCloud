package pl.pvpcloud.grouptp.arena.adapters

import com.google.common.util.concurrent.ThreadFactoryBuilder
import pl.pvpcloud.grouptp.arena.GroupTpPlugin
import pl.pvpcloud.grouptp.common.packets.PacketPrepareMatch
import pl.pvpcloud.nats.api.INatsAdapter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PrepareMatchAdapter(private val plugin: GroupTpPlugin) : INatsAdapter {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor(ThreadFactoryBuilder().setNameFormat("World loader Executor").build())

    override fun received(id: String, packet: Any) {
        if (packet is PacketPrepareMatch) {
            this.executor.execute {
                this.plugin.matchManager.createMatch(packet)
            }
        }
    }

}