package pl.pvpcloud.auth.helpers

import java.net.URL
import javax.net.ssl.HttpsURLConnection

object PremiumHelper {

    fun hasPaid(name: String): Boolean {
        try {
            val url = URL("https://api.mojang.com/users/profiles/minecraft/$name")
            val conn = url.openConnection() as HttpsURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36"
            )
            conn.connect()
            return conn.responseCode == 200
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}