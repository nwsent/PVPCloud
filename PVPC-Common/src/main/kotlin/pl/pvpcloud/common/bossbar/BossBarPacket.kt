package pl.pvpcloud.common.bossbar

import io.netty.buffer.Unpooled
import net.minecraft.server.v1_8_R3.PacketDataSerializer
import java.util.*

object BossBarPacket {

    internal var operation: BarOperation? = null
    internal var uuid: UUID? = null
    internal var title: String? = null
    internal var progress = 0f
    internal var color: BarColor? = null
    internal var style: BarStyle? = null

    fun getUuid(): UUID? {
        return uuid
    }

    fun setUuid(uuid: UUID?): BossBarPacket? {
        this.uuid = uuid
        return this
    }

    fun getOperation(): BarOperation? {
        return operation
    }

    fun setOperation(operation: BarOperation?) {
        this.operation = operation
    }

    /**
     * Returns the title of this boss bar
     *
     * @return the title of the bar
     */
    fun getTitle(): String? {
        return title
    }

    /**
     * Sets the title of this boss bar
     *
     * @param title the title of the bar
     * @return
     */
    fun setTitle(title: String?): BossBarPacket? {
        this.title = title
        return this
    }

    /**
     * Returns the progress of the bar between 0.0 and 1.0
     *
     * @return the progress of the bar
     */
    fun getProgress(): Float {
        return progress
    }

    /**
     * Sets the progress of the bar. Values should be between 0.0 (empty) and 1.0
     * (full)
     *
     * @param progress the progress of the bar
     */
    fun setProgress(progress: Float): BossBarPacket? {
        this.progress = progress
        return this
    }

    /**
     * Returns the color of this boss bar
     *
     * @return the color of the bar
     */
    fun getColor(): BarColor? {
        return color
    }

    /**
     * Sets the color of this boss bar.
     *
     * @param color the color of the bar
     */
    fun setColor(color: BarColor?): BossBarPacket? {
        this.color = color
        return this
    }

    /**
     * Returns the style of this boss bar
     *
     * @return the style of the bar
     */
    fun getStyle(): BarStyle? {
        return style
    }

    /**
     * Sets the bar style of this boss bar
     *
     * @param style the style of the bar
     */
    fun setStyle(style: BarStyle?): BossBarPacket? {
        this.style = style
        return this
    }

    fun serialize(): PacketDataSerializer? {
        val pds = PacketDataSerializer(Unpooled.buffer())
        pds.a(uuid) // writeUUID
        pds.a(operation) // writeEnum - varint
        when (operation) {
            BarOperation.ADD -> {
                pds.a(title) // writeVarString
                pds.writeFloat(progress)
                pds.a(color) // writeEnum - varint
                pds.a(style) // writeEnum - varint
                pds.writeByte(0)
            }
            BarOperation.REMOVE -> {
            }
            BarOperation.UPDATE_PCT -> pds.writeFloat(progress)
            BarOperation.UPDATE_NAME -> pds.a(title) // writeString
            BarOperation.UPDATE_STYLE -> {
                pds.a(color) // writeEnum - varint
                pds.a(style) // writeEnum - varint
            }
            BarOperation.UPDATE_PROPERTIES -> pds.writeByte(0)
            else -> {
            }
        }
        pds.capacity(pds.readableBytes())
        return pds
    }
}