package pl.pvpcloud.redis

import io.lettuce.core.codec.RedisCodec
import java.io.*
import java.nio.ByteBuffer

class RedisCodec : RedisCodec<String, Any> {

    private val charset = Charsets.UTF_8

    override fun decodeKey(bytes: ByteBuffer): String {
        return this.charset.decode(bytes).toString()
    }

    override fun decodeValue(bytes: ByteBuffer): Any? {
        return try {
            val array = ByteArray(bytes.remaining())
            bytes[array]
            val inputStream = ObjectInputStream(ByteArrayInputStream(array))
            val packet = inputStream.readObject()
            inputStream.close()

            packet
        } catch (e: Exception) {
            null
        }
    }

    override fun encodeKey(key: String): ByteBuffer {
        return this.charset.encode(key)
    }

    override fun encodeValue(value: Any?): ByteBuffer? {
        return try {
            val bytes = ByteArrayOutputStream()
            val os = ObjectOutputStream(bytes)
            os.writeObject(value)
            val buffer = ByteBuffer.wrap(bytes.toByteArray())
            os.close()

            buffer
        } catch (e: IOException) {
            null
        }
    }

}