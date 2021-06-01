package pl.pvpcloud.database.api

import com.mongodb.async.SingleResultCallback

class DatabaseResult<T>(val database: Database) : SingleResultCallback<T> {

    override fun onResult(result: T, throwable: Throwable?) {
        throwable?.printStackTrace()
    }

}