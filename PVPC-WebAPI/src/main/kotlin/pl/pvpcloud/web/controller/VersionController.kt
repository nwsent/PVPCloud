package pl.pvpcloud.web.controller

import pl.pvpcloud.web.WebApplication

class VersionController(private val webApplication: WebApplication) {

    init {
        webApplication.http.get("/api") {
            response.type("application/json")
            response.status(200)
            webApplication.gson.toJsonTree("ver: 0.2")
        }
    }
}