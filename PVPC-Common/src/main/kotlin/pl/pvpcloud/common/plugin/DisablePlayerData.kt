package pl.pvpcloud.common.plugin

import pl.pvpcloud.common.helpers.ReflectionHelper
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class DisablePlayerData : InvocationHandler {

    private var original: Any? = null

    init {
        try {
            val clazz = ReflectionHelper.getNMSClass("IPlayerFileData")
            val proxyIPlayerFileData = Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz), this)

            val minecraftServer = ReflectionHelper.getNMSClass("MinecraftServer")
            val server = minecraftServer.getMethod("getServer").invoke(null)
            val playerList = minecraftServer.getMethod("getPlayerList").invoke(server)
            val f = playerList.javaClass.getField("playerFileData")

            original = f.get(playerList)
            f.set(playerList, proxyIPlayerFileData)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Throwable::class)
    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
        return null
    }

}