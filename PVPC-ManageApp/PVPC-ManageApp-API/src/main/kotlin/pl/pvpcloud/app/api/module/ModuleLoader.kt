package pl.pvpcloud.app.api.module

import java.io.File

interface ModuleLoader {

    /**
     * Load module logic
     *
     * @param file set a modules folder
     *
     * @author Krzysiekigry
     * @since 1.0
     */
    fun loadModule(file: File): Module

    /**
     * Enable module
     *
     * @author Krzysiekigry
     * @since 1.0
     */
    fun enableModule()

    /**
     * Disable module
     *
     * @author Krzysiekigry
     * @since 1.0
     */
    fun disableModule()
}