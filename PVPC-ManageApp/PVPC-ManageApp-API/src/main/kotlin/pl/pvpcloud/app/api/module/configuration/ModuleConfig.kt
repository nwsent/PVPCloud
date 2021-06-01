package pl.pvpcloud.app.api.module.configuration

interface ModuleConfig {

    fun load()

    fun save()

    fun reload()

}