package pl.pvpcloud.common.bossbar

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.chat.ComponentSerializer
import java.util.*


abstract class BossBarBuilder protected constructor(uuid: UUID?) {

    protected val packet = BossBarPacket
    fun uuid(): UUID? {
        return packet.getUuid()
    }

    class Add(uuid: UUID) : BossBarBuilder(uuid) {
        /**
         * Sets the title of this boss bar
         *
         * @param title the title of the bar
         */
        fun title(title: Array<BaseComponent?>): Add {
            packet.setTitle(ComponentSerializer.toString(*title))
            return this
        }

        /**
         * Sets the progress of the bar. Values should be between 0.0 (empty) and 1.0
         * (full)
         *
         * @param progress the progress of the bar
         */
        fun progress(progress: Float): Add {
            packet.setProgress(progress)
            return this
        }

        /**
         * Sets the color of this boss bar.
         *
         * @param color the color of the bar
         */
        fun color(color: BarColor?): Add {
            packet.setColor(color)
            return this
        }

        /**
         * Sets the bar style of this boss bar
         *
         * @param style the style of the bar
         */
        fun style(style: BarStyle?): Add {
            packet.setStyle(style)
            return this
        }

        override fun check() {
            requireNotNull(packet.getTitle()) { "missing title" }
            requireNotNull(packet.getColor()) { "missing color" }
            requireNotNull(packet.getStyle()) { "missing style" }
        }

        init {
            packet.setOperation(BarOperation.ADD)
        }
    }

    class Remove(uuid: UUID) : BossBarBuilder(uuid) {
        init {
            packet.setOperation(BarOperation.REMOVE)
        }
    }

    class UpdateProgress(uuid: UUID) : BossBarBuilder(uuid) {
        /**
         * Sets the progress of the bar. Values should be between 0.0 (empty) and 1.0
         * (full)
         *
         * @param progress the progress of the bar
         */
        fun progress(progress: Float): UpdateProgress {
            packet.setProgress(progress)
            return this
        }

        init {
            packet.setOperation(BarOperation.UPDATE_PCT)
        }
    }

    class UpdateStyle(uuid: UUID) : BossBarBuilder(uuid) {
        /**
         * Sets the color of this boss bar.
         *
         * @param color the color of the bar
         */
        fun color(color: BarColor?): UpdateStyle {
            packet.setColor(color)
            return this
        }

        /**
         * Sets the bar style of this boss bar
         *
         * @param style the style of the bar
         */
        fun style(style: BarStyle?): UpdateStyle {
            packet.setStyle(style)
            return this
        }

        override fun check() {
            requireNotNull(packet.getColor()) { "missing color" }
            requireNotNull(packet.getStyle()) { "missing style" }
        }

        init {
            packet.setOperation(BarOperation.UPDATE_STYLE)
        }
    }

    class UpdateTitle(uuid: UUID) : BossBarBuilder(uuid) {
        /**
         * Sets the title of this boss bar
         *
         * @param title the title of the bar
         */
        fun title(title: Array<BaseComponent?>): UpdateTitle {
            packet.setTitle(ComponentSerializer.toString(*title))
            return this
        }

        override fun check() {
            requireNotNull(packet.getTitle()) { "missing title" }
        }

        init {
            packet.setOperation(BarOperation.UPDATE_NAME)
        }
    }

    protected open fun check() {}
    fun buildPacket(): BossBarPacket {
        check()
        return packet
    }

    companion object {
        fun add(uuid: UUID): Add {
            return Add(uuid)
        }

        fun remove(uuid: UUID): Remove {
            return Remove(uuid)
        }

        fun updateProgress(uuid: UUID): UpdateProgress {
            return UpdateProgress(uuid)
        }

        fun updateStyle(uuid: UUID): UpdateStyle {
            return UpdateStyle(uuid)
        }

        fun updateTitle(uuid: UUID): UpdateTitle {
            return UpdateTitle(uuid)
        }
    }

    init {
        packet.setUuid(uuid)
    }
}
