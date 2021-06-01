package pl.pvpcloud.anticlicker

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import pl.pvpcloud.anticlicker.checks.CheckCPS
import pl.pvpcloud.anticlicker.config.AntiClickerConfig
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin

class AntiClickerPlugin : CloudPlugin() {

    lateinit var config: AntiClickerConfig

    lateinit var protocolManager: ProtocolManager

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, AntiClickerConfig::class, "config")

        this.protocolManager = ProtocolLibrary.getProtocolManager()

        this.registerListeners(
                CheckCPS(this)
        )
    }

}