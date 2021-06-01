package pl.pvpcloud.auth.holders

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.util.concurrent.TimeUnit

object PlayerPremiumHolder {

    val players: Cache<String, Boolean> = CacheBuilder
            .newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build()
}