package com.piperframework.mongo

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.piperframework.config.database.MongoDatabaseConfig
import org.litote.kmongo.KMongo

fun MongoDatabaseConfig.createClient(): MongoClient {
    val credentials = user?.let {
        listOfNotNull(it, password?.decryptedValue).joinToString(":")
    }
    val connectionString = "$protocol://${listOfNotNull(credentials, host).joinToString("@")}"
    return KMongo.createClient(MongoClientURI(connectionString))
}
