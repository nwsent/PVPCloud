package pl.pvpcloud.guild.impl

import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.guild.api.GuildsBootstrap
import pl.pvpcloud.guild.api.structure.GuildFactory
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.guild.impl.adapters.*
import pl.pvpcloud.guild.impl.command.GuildAdminCommands
import pl.pvpcloud.guild.impl.structure.GuildFactoryImpl
import pl.pvpcloud.guild.impl.structure.GuildImpl
import pl.pvpcloud.guild.impl.structure.GuildRepositoryImpl
import pl.pvpcloud.guild.impl.command.GuildCommands
import pl.pvpcloud.guild.impl.listener.GuildChatListener
import pl.pvpcloud.guild.impl.listener.GuildFightListener
import pl.pvpcloud.guild.impl.listener.GuildJoinListener
import pl.pvpcloud.guild.impl.listener.GuildTagListener
import pl.pvpcloud.guild.impl.task.*
import pl.pvpcloud.guild.impl.variable.profil.*
import pl.pvpcloud.guild.impl.variable.top.TopGuildByDeathsVariable
import pl.pvpcloud.guild.impl.variable.top.TopGuildByKillsVariable
import pl.pvpcloud.guild.impl.variable.top.TopGuildByPointsVariable
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tablist.TablistAPI
import java.util.logging.Logger

class GuildsBootstrap(private val plugin: GuildsPlugin) : GuildsBootstrap {

    override lateinit var guildRepository: GuildRepository
    override lateinit var guildFactory: GuildFactory

    lateinit var guildsMessageRecruitmentTask: GuildsMessageRecruitmentTask

    private val logger: Logger
        get() = this.plugin.logger

    val config = ConfigLoader.load(this.plugin.dataFolder, GuildsConfig::class, "config")

    override fun start() {
        this.guildRepository = GuildRepositoryImpl()
        this.guildFactory = GuildFactoryImpl(this.plugin, this.guildRepository)

        this.plugin.registerCommands(
                GuildCommands(this.guildRepository, this.guildFactory, this.plugin),
                GuildAdminCommands(this.guildRepository, this.guildFactory, this.plugin)
        )

        NetworkAPI.registerAdapters(
                GuildCreateAdapter(),
                GuildDeleteAdapter(this.guildRepository),
                GuildAddAdapter(this.guildRepository),
                GuildRemoveAdapter(this.guildRepository),
                GuildChatAdapter(),
                GuildAllyChatAdapter(),
                GuildRemoveAllyAdapter(this.guildRepository),
                GuildUpdateAdapter(this.guildRepository),
                GuildTimeOutAdapter(this.guildRepository),
                GuildAcceptAllyAdapter(this.guildRepository)
        )

        this.plugin.registerListeners(
                GuildJoinListener(this),
                GuildTagListener(this.guildRepository, this.guildFactory),
                GuildChatListener(this.guildRepository)
        )
        if (this.config.registerListenerForPvPInGuild)
            this.plugin.registerListeners(
                    GuildFightListener(this.guildRepository)
            )

        if (NetworkAPI.id == "lobby") {
        //    MessagesRekruTask(this.guildRepository).runTaskTimerAsynchronously(this.plugin, 60, 60)
            CheckTimeTask(this.guildRepository, this.guildFactory).runTaskTimerAsynchronously(this.plugin, 12000, 12000)
        }
        DatabaseTask(this.guildRepository).runTaskTimerAsynchronously(this.plugin, 0, 20 * 60)
        InvitesTask(this.guildRepository).runTaskTimerAsynchronously(this.plugin, 0, (20 * 60) - 2)
        this.guildsMessageRecruitmentTask = GuildsMessageRecruitmentTask(this.plugin, this.guildRepository)

        this.logger.info("Plugin has been enabled.")
        this.initTab()
    }

    override fun stop() {
        for (guild in this.guildRepository.guildsMap.values) {
            if (guild is GuildImpl) {
                guild.updateEntity()
            }
        }

        this.logger.info("Plugin has been disable.")
    }

    override fun initTab() {
        TablistAPI.registerVariables(
                GuildDeathsVariable("g-deaths"),
                GuildKillsVariable("g-kills"),
                GuildPointsVariable("g-rank", this.guildRepository),
                GuildTagVariable("g-tag"),
                GuildLeaderVariable("g-leader")
        )
        (1..16).forEach {
            TablistAPI.registerVariables(
                TopGuildByDeathsVariable("g-topdeaths$it", it, this.guildRepository),
                TopGuildByKillsVariable("g-topkills$it", it, this.guildRepository),
                TopGuildByPointsVariable("g-toppoints$it", it, this.guildRepository)
            )
        }

    }

}