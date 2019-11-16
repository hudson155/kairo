package io.limberapp.framework

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.WriteConcern
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import io.limberapp.framework.config.database.DatabaseConfig

fun DatabaseConfig.createClient(): MongoClient {
    val credentials = user?.let {
        listOfNotNull(it, password?.decryptedValue).joinToString(":")
    }
    val connectionString = "$protocol://${listOfNotNull(credentials, host).joinToString("@")}"
    val clientSettings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(connectionString))
        .writeConcern(WriteConcern.MAJORITY)
        .build()
    return MongoClients.create(clientSettings)
}
