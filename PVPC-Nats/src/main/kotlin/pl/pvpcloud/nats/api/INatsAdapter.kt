package pl.pvpcloud.nats.api

interface INatsAdapter {

    fun received(id: String, packet: Any)

}