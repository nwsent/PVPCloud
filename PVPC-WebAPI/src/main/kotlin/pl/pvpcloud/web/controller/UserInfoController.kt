package pl.pvpcloud.web.controller

import pl.pvpcloud.web.WebApplication

class UserInfoController(private val webApplication: WebApplication) {

    init {
        webApplication.http.post("/api/user") {
            response.type("application/json")
            response.status(200)
            webApplication.playerSearchService.getPlayerInfo(request.queryParams("name"))
        }
    }

}