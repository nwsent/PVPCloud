package pl.pvpcloud.nats.api

interface INatsCodec {

    fun encode(packet: PacketWrapper): ByteArray
    fun decode(byteArray: ByteArray): PacketWrapper

}

data class PacketWrapper(val packet: Any)
