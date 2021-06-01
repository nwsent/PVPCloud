package pl.pvpcloud.app

import pl.pvpcloud.app.api.ManageApp
import pl.pvpcloud.app.impl.ManageAppImpl

object Starter {

    lateinit var manageApp: ManageApp

    @JvmStatic
    fun main(args: Array<String>) {
        this.manageApp = ManageAppImpl()
        this.manageApp.start()
    }
}