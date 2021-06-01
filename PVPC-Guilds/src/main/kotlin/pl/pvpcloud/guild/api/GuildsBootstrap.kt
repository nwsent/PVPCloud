package pl.pvpcloud.guild.api

import pl.pvpcloud.guild.api.structure.GuildFactory
import pl.pvpcloud.guild.api.structure.GuildRepository

interface GuildsBootstrap {

    val guildRepository: GuildRepository
    val guildFactory: GuildFactory

    fun start()
    fun stop()

    fun initTab()
}