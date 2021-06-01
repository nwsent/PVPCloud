package pl.pvpcloud.database.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mongodb.Block
import com.mongodb.async.client.MongoClient
import com.mongodb.async.client.MongoClients
import com.mongodb.async.client.MongoCollection
import com.mongodb.async.client.MongoDatabase
import com.mongodb.client.result.DeleteResult
import org.bson.Document
import java.util.*

class Database(private val databaseCredentials: DatabaseCredentials) {

    private var connection: MongoClient
    private var database: MongoDatabase
    private val resultInsert = DatabaseResult<Void>(this)
    private val resultUpdate = DatabaseResult<Document>(this)
    private val resultDelete = DatabaseResult<DeleteResult>(this)
    val serializer: Gson = GsonBuilder()
            .create()

    init {
        /*val settings = MongoClientSettings.builder()
            .retryWrites(true)
            .applyConnectionString(ConnectionString(this.databaseCredentials.url))
            .applyToConnectionPoolSettings {
                it.minSize(5)
                it.maxSize(10)
                it.maxWaitQueueSize(10 * 7) // maxPoolSize * waitQueueMultiple
                it.maxWaitTime(150, TimeUnit.MILLISECONDS)
                it.maxConnectionLifeTime(300, TimeUnit.MILLISECONDS)
                it.maxConnectionIdleTime(200, TimeUnit.MILLISECONDS)
            }
            .applyToClusterSettings {
                it.mode(ClusterConnectionMode.MULTIPLE)
                it.serverSelectionTimeout(25000, TimeUnit.MILLISECONDS)
                it.maxWaitQueueSize(10 * 7) // maxPoolSize * waitQueueMultiple
                it.localThreshold(30, TimeUnit.MILLISECONDS)
                it.hosts(arrayListOf(
                    ServerAddress("cluster0-shard-00-00-kk4dj.mongodb.net", 27017),
                    ServerAddress("cluster0-shard-00-01-kk4dj.mongodb.net", 27017),
                    ServerAddress("cluster0-shard-00-02-kk4dj.mongodb.net", 27017)
                ))
            }
            .applyToSslSettings {
                it.enabled(true)
                it.invalidHostNameAllowed(true)
            }
            .applyToServerSettings {
                it.heartbeatFrequency(20000, TimeUnit.MILLISECONDS)
            }
            .applyToSocketSettings {
                it.connectTimeout(2500, TimeUnit.MILLISECONDS)
                it.readTimeout(5500, TimeUnit.MILLISECONDS)
            }
            .build()
        this.connection = MongoClients.create(settings)*/
        this.connection = MongoClients.create(this.databaseCredentials.url)
        this.database = this.connection.getDatabase(this.databaseCredentials.databaseName)
        DatabaseAPI.database = this

        this.database.listCollectionNames().forEach(Block {
            print(it)
        }, resultInsert)
    }

    fun getCollection(table: String): MongoCollection<Document> = database.getCollection(table)

    fun insertEntity(entity: DatabaseEntity) = getCollection(entity.collection).insertOne(serializeEntity(entity), resultInsert)

    fun updateEntity(entity: DatabaseEntity) = getCollection(entity.collection).findOneAndReplace(generateKey(entity), serializeEntity(entity), resultUpdate)

    fun deleteEntity(entity: DatabaseEntity) = getCollection(entity.collection).deleteOne(generateKey(entity), resultDelete)

    private fun serializeEntity(entity: DatabaseEntity) = Document.parse(serializer.toJson(entity))!!

    private fun generateKey(entity: DatabaseEntity) = Document(entity.key, entity.id)

    inline fun <reified T> loadObject(collection: String, id: String, crossinline listener: (data: T?) -> Unit) {
        getCollection(collection)
                .find(Document("id", id))
                .first { document, throwable ->
                    if (throwable == null)
                        listener.invoke(serializer.fromJson(serializer.toJson(document), T::class.java))
                    else
                        listener.invoke(null)
                }
    }

    inline fun <reified T> loadBySelector(collection: String, selector: String, id: String, crossinline listener: (data: T?) -> Unit) {
        getCollection(collection)
                .find(Document(selector, id))
                .first { document, throwable ->
                    if (throwable == null)
                        listener.invoke(serializer.fromJson(serializer.toJson(document), T::class.java))
                    else
                        listener.invoke(null)
                }
    }

    inline fun <reified T> loadObjects(collection: String, id: String, crossinline listener: (data: ArrayList<T>) -> Unit) {
        val list = arrayListOf<T>()
        getCollection(collection)
                .find(Document("id", id))
                .forEach({
                    list.add(serializer.fromJson(serializer.toJson(it), T::class.java))
                }, { _, err ->
                    if (err != null) {
                        err.printStackTrace()
                    } else {
                        listener.invoke(list)
                    }
                })
    }


    inline fun <reified T> loadCollection(collection: String, crossinline listener: (data: ArrayList<T>) -> Unit) {
        val list = arrayListOf<T>()
        getCollection(collection)
                .find()
                .forEach({
                    list.add(serializer.fromJson(serializer.toJson(it), T::class.java))
                }, { _, err ->
                    if (err != null) {
                        err.printStackTrace()
                    } else {
                        listener.invoke(list)
                    }
                })
    }

    fun removeCollection(name: String) = getCollection(name).drop { _, _ -> }
}