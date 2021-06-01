package pl.pvpcloud.common.extensions

import net.md_5.bungee.api.CommandSender

fun CommandSender.sendFixedMessage(message: String) {
    this.sendMessage(message.fixColors())
}

fun CommandSender.sendFixedMessage(vararg messages: String) {
    messages.forEach { message ->
        this.sendMessage(message.fixColors())
    }
}

fun CommandSender.sendFixedMessage(messages: ArrayList<String>) {
    messages.forEach { message ->
        this.sendMessage(message.fixColors())
    }
}
