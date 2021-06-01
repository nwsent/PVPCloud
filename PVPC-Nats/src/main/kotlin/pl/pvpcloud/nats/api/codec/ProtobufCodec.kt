package pl.pvpcloud.nats.api.codec

import io.protostuff.LinkedBuffer
import io.protostuff.MsgpackIOUtil
import io.protostuff.ProtobufIOUtil
import io.protostuff.Schema
import io.protostuff.runtime.RuntimeSchema
import pl.pvpcloud.nats.api.INatsCodec
import pl.pvpcloud.nats.api.PacketWrapper

class ProtobufCodec(bufferSize: Int) : INatsCodec {

    private val schema: Schema<PacketWrapper> = RuntimeSchema.getSchema(PacketWrapper::class.java)
    private val buffer: LinkedBuffer = LinkedBuffer.allocate(bufferSize)

    override fun encode(packet: PacketWrapper): ByteArray {
        try {
            return ProtobufIOUtil.toByteArray(packet, this.schema, this.buffer)
        } finally {
            this.buffer.clear()
        }
    }

    override fun decode(byteArray: ByteArray): PacketWrapper {
        val packet = this.schema.newMessage()
        ProtobufIOUtil.mergeFrom(byteArray, packet, this.schema)

        return packet
    }

}