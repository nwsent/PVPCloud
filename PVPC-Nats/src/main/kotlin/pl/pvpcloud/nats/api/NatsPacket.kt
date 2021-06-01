package pl.pvpcloud.nats.api

import java.util.concurrent.ThreadLocalRandom

open class NatsPacket {

    var callbackId: Long = ThreadLocalRandom.current().nextLong(Long.MIN_VALUE, Long.MAX_VALUE)

}