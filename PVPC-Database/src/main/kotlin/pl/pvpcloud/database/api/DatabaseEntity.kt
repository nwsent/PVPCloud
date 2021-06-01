package pl.pvpcloud.database.api

import java.io.Serializable

abstract class DatabaseEntity : Serializable {

    fun insertEntity() = DatabaseAPI.database.insertEntity(this)
    fun updateEntity() = DatabaseAPI.database.updateEntity(this)
    fun deleteEntity() = DatabaseAPI.database.deleteEntity(this)

    abstract val id: String
    abstract val key: String
    abstract val collection: String

}