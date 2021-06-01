package pl.pvpcloud.auth.security

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*

object Cryptography {
    fun sha256(base: String): String? {
        var digest: MessageDigest? = null
        try {
            digest = MessageDigest.getInstance("SHA-256")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        }

        val hash = digest.digest(base.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(hash)
    }

    @Throws(NoSuchAlgorithmException::class)
    fun sha256v2(msg: String): String {
        val sha = MessageDigest.getInstance("SHA-256")
        sha.reset()
        sha.update(msg.toByteArray())
        val digest = sha.digest()
        return String.format("%0" + (digest.size shl 1) + "x", BigInteger(1, digest))
    }

    @Throws(NoSuchAlgorithmException::class)
    fun shaSalted(message: String, salt: String): String {
        return "\$SHA$" + salt + "$" + sha256v2(sha256v2(message) + salt)
    }

    @Throws(NoSuchAlgorithmException::class)
    fun createSalt(length: Int): String {
        val rnd = SecureRandom()
        val msg = ByteArray(40)
        rnd.nextBytes(msg)
        val sha = MessageDigest.getInstance("SHA1")
        sha.reset()
        val digest = sha.digest(msg)
        return String.format("%0" + (digest.size shl 1) + "x", BigInteger(1, digest)).substring(0, length)
    }

    fun cmpPassWithHash(pass: String, hash: String): Boolean {
        if (hash.contains("$")) {
            val line = hash.split("\\$".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (line.size > 3 && line[1] == "SHA") {
                try {
                    return hash == shaSalted(pass, line[2])
                } catch (ignored: NoSuchAlgorithmException) {
                    // empty
                }
            }
        }
        return false
    }
}