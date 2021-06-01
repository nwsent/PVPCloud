package pl.pvpcloud.app.api.module

import pl.pvpcloud.app.api.module.configuration.ModuleConfig
import java.util.logging.Logger

interface Module {

    /**
     * Return name module
     *
     * @author Krzysiekigry
     * @since 1.0
     */
    fun getName()

    /**
     * Call when module start
     *
     * @author Krzysiekigry
     * @since 1.0
     */
    fun onEnable()

    /**
     * Call when module stop
     *
     * @author Krzysiekigry
     * @since 1.0
     */
    fun onDisable()

    /**
     * Return logger module
     *
     * @author Krzysiekigry
     * @since 1.0
     */
    fun getLogger(): Logger

    fun getConfig(): ModuleConfig
}