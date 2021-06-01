package pl.pvpcloud.common.helpers

import java.io.File
import java.nio.file.Files

object FileHelper {

    fun saveJson(file: File, string: String) {
        if (file.exists()) file.delete()
        Files.write(file.toPath(), string.toByteArray(Charsets.UTF_8))
    }

}