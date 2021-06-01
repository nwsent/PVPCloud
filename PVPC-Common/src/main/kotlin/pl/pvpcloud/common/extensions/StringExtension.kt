package pl.pvpcloud.common.extensions

import net.md_5.bungee.api.ChatColor
import pl.pvpcloud.common.helpers.StringHelper

fun String.fixColors(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}

fun String.rep(search: String, replacement: String) = StringHelper.replace(this, search, replacement)