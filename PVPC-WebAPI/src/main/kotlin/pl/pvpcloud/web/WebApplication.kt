package pl.pvpcloud.web

import com.google.gson.GsonBuilder
import pl.pvpcloud.bukkit.WebAPIPlugin
import pl.pvpcloud.web.controller.UserInfoController
import pl.pvpcloud.web.controller.VersionController
import pl.pvpcloud.web.services.PlayerSearchService
import spark.kotlin.Http
import spark.kotlin.ignite

class WebApplication(private val plugin: WebAPIPlugin) {

    val http: Http = ignite()
    val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

    val key = "3aa1cba8-f58a-459e-9b2d-5784836373ac"

    lateinit var versionController: VersionController

    lateinit var playerSearchService: PlayerSearchService

    lateinit var userInfoController: UserInfoController

    init {
        http.port(6996)

        this.versionController = VersionController(this)
        this.playerSearchService = PlayerSearchService(this)
        this.userInfoController = UserInfoController(this)
    }
}