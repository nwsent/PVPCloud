package pl.pvpcloud.common.helpers

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

object InventorySerializerHelper {

    @Throws(IllegalStateException::class)
    fun serializeInventory(items: Array<ItemStack>): String {
        try {
            val outputStream = ByteArrayOutputStream()
            val dataOutput = BukkitObjectOutputStream(outputStream)
            dataOutput.writeObject(items)
            val encoded = Base64Coder.encodeLines(outputStream.toByteArray())
            outputStream.close()
            dataOutput.close()
            return encoded
        } catch (e: Exception) {
            throw IllegalStateException("Unable to save itemstack array", e)
        }

    }

    @Throws(IOException::class)
    fun deserializeInventory(data: String): Array<ItemStack> {
        try {
            val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(data))
            val dataInput = BukkitObjectInputStream(inputStream)
            val read = dataInput.readObject() as Array<ItemStack>
            inputStream.close()
            dataInput.close()
            return read
        } catch (e: ClassNotFoundException) {
            throw IOException("Unable to read class type", e)
        }
    }

}