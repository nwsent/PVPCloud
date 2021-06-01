package pl.pvpcloud.nats.api.codec

import io.protostuff.MsgpackIOUtil
import io.protostuff.Schema
import io.protostuff.runtime.RuntimeSchema
import pl.pvpcloud.nats.api.INatsCodec
import pl.pvpcloud.nats.api.PacketWrapper

class MsgPackCodec : INatsCodec {

    private val schema: Schema<PacketWrapper> = RuntimeSchema.getSchema(PacketWrapper::class.java)

    override fun encode(packet: PacketWrapper): ByteArray {
        return MsgpackIOUtil.toByteArray(packet, this.schema, false)
    }

    override fun decode(byteArray: ByteArray): PacketWrapper {
        val packet = this.schema.newMessage()
        MsgpackIOUtil.mergeFrom(byteArray, packet, this.schema, false)

        return packet
    }

}