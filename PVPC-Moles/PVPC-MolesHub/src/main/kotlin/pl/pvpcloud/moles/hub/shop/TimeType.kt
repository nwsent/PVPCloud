package pl.pvpcloud.moles.hub.shop

enum class TimeType(val days: Long, val reduction: Double, val daysName: String) {

    ONE(1, 0.0, "&8* &e1 dzień &8*"),
    THREE(3, 0.05, "&8* &e3 dni &8*"),
    SEVEN(7, 0.15, "&8* &e7 dni&8*")
}