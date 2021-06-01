package pl.pvpcloud.auth

import pl.pvpcloud.auth.adapters.AuthPlayerUpdateAdapter
import pl.pvpcloud.auth.basic.AuthRepository
import pl.pvpcloud.auth.commands.AuthCommand
import pl.pvpcloud.auth.commands.ChangePasswordCommand
import pl.pvpcloud.auth.commands.LoginCommand
import pl.pvpcloud.auth.commands.RegisterCommand
import pl.pvpcloud.auth.config.AuthConfig
import pl.pvpcloud.auth.helpers.ServersHelper
import pl.pvpcloud.auth.listeners.*
import pl.pvpcloud.auth.packets.PacketAuthPlayerUpdate
import pl.pvpcloud.auth.tasks.OnlineServersTask
import pl.pvpcloud.auth.tasks.StatusLoginTask
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.nats.NetworkAPI
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class AuthPlugin : CloudPlugin() {

    lateinit var config: AuthConfig

    lateinit var serversHelper: ServersHelper

    lateinit var authRepository: AuthRepository

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, AuthConfig::class, "config")

        this.serversHelper = ServersHelper(this)

        this.authRepository = AuthRepository()

        val pluginManager = this.proxy.pluginManager
        pluginManager.registerCommand(this, ChangePasswordCommand(this))
        pluginManager.registerCommand(this, RegisterCommand(this))
        pluginManager.registerCommand(this, LoginCommand(this))
        pluginManager.registerCommand(this, AuthCommand(this))

        this.registerListeners(
                BlazingPackHandshakeAuthListener(this),
                DisconnectListener(this),
                LoginListener(this),
                ChatListener(this),
                PostLoginListener(this),
                PreLoginListener(this),
                ServerConnectListener(this)
        )

        this.proxy.scheduler.schedule(this, StatusLoginTask(this), 0, 3, TimeUnit.SECONDS)
        this.proxy.scheduler.schedule(this, OnlineServersTask(this), 0, 5, TimeUnit.SECONDS)

        NetworkAPI.registerAdapters(
                AuthPlayerUpdateAdapter(this)
        )
    }

    override fun onDisable() {
        this.authRepository.playersMap.values.forEach {
            NetworkAPI.publish { PacketAuthPlayerUpdate(it) }
        }
    }
}