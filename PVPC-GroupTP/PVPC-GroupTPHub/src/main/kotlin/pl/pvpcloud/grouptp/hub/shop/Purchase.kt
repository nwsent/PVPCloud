package pl.pvpcloud.grouptp.hub.shop

class Purchase(
        val itemId: Int,
        private val created: Long,
        var time: Long
) {
    val expireTime get() = created + time
    val expired get() = if (time == -1L) false else System.currentTimeMillis() > expireTime
}