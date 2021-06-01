package pl.pvpcloud.ffa.save

import java.io.Serializable

data class Save(
    val kitName: String,
    val inventory: String
) : Serializable
