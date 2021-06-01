package pl.pvpcloud.grouptp.hub.adapters

import pl.pvpcloud.grouptp.common.packets.PacketKitItemsRequest
import pl.pvpcloud.grouptp.common.packets.PacketKitItemsResponse
import pl.pvpcloud.grouptp.hub.GroupTpPlugin
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.nats.api.INatsAdapter

class KitItemsAdapter(private val plugin: GroupTpPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketKitItemsRequest) {
            val kit = this.plugin.kitManager.getKit(packet.kitName)
                    ?: return
            val callback = PacketKitItemsResponse(packet.uniqueId, kit.armor, this.plugin.saveManager.getItems(packet.uniqueId, kit))
            callback.callbackId = packet.callbackId

            NetworkAPI.publish(id) { callback }
        }
    }
}