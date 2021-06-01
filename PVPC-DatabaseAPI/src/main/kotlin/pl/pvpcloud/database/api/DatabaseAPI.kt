package pl.pvpcloud.database.api

object DatabaseAPI {

    lateinit var database: Database

    inline fun <reified T : DatabaseEntity> loadBySelector(name: String, id: String, selector: String, crossinline listener: (data: T?) -> Unit) =
            this.database.loadBySelector<T>(name, id, selector, listener)

    inline fun <reified T : DatabaseEntity> loadAll(name: String, crossinline listener: (data: Set<T>) -> Unit) =
            this.database.loadAll(name, listener)

    inline fun <reified T : DatabaseEntity> load(name: String, id: String, crossinline listener: (data: T?) -> Unit) =
            this.database.load<T>(name, id, listener)

    fun removeCollection(name: String) = this.database.removeCollection(name)

}