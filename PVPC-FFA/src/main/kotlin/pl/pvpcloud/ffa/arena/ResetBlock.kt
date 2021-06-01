package pl.pvpcloud.ffa.arena

import org.bukkit.Material
import java.util.concurrent.TimeUnit

data class ResetBlock(
    val mat: Material,
    val data: Byte,
    val time: Long = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10)
)