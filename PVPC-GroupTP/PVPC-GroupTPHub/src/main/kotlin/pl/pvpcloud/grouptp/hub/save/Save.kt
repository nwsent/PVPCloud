package pl.pvpcloud.grouptp.hub.save

import java.io.Serializable

data class Save(
    val kitName: String,
    val inventory: String
) : Serializable
