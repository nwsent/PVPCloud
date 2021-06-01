package pl.pvpcloud.app.api

interface ManageApp {

    /**
     * When application start
     *
     * @author Krzysiekigry
     * @since 1.0
     */
    fun start()

    /**
     * When application stop
     *
     * @author Krzysiekigry
     * @since 1.0
     */
    fun stop()
}