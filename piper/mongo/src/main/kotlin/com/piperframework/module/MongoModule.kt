package com.piperframework.module

import com.fasterxml.jackson.databind.module.SimpleModule
import com.google.inject.AbstractModule
import com.mongodb.client.MongoDatabase
import com.piperframework.config.database.MongoDatabaseConfig
import com.piperframework.mongo.BsonUuidDeserializer
import com.piperframework.mongo.BsonUuidSerializer
import com.piperframework.mongo.createClient
import org.litote.kmongo.util.KMongoConfiguration
import java.util.UUID

/**
 * MongoModule configures bindings for MongoDB.
 */
open class MongoModule(protected val mongoDatabaseConfig: MongoDatabaseConfig) : AbstractModule() {

    override fun configure() {
        bind(MongoDatabase::class.java)
            .toInstance(mongoDatabaseConfig.createClient().getDatabase(mongoDatabaseConfig.database))
    }

    companion object {
        init {
            KMongoConfiguration.registerBsonModule(object : SimpleModule() {
                init {
                    addSerializer(UUID::class.java, BsonUuidSerializer())
                    addDeserializer(UUID::class.java, BsonUuidDeserializer())
                }
            })
        }
    }
}
