package io.limberapp.framework

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.WriteConcern
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import io.limberapp.framework.config.Config

fun Config.createClient(): MongoClient {
    val connectionString = with(database) {
        val credentials = user?.let {
            listOfNotNull(it, password?.decryptedValue).joinToString(":")
        }
        return@with "$protocol://${listOfNotNull(credentials, host).joinToString("@")}"
    }
    val clientSettings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(connectionString))
        .writeConcern(WriteConcern.MAJORITY)
        .build()
    return MongoClients.create(clientSettings)
}
