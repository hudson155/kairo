package com.piperframework.mongo

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoClientURI
import com.piperframework.config.database.MongoDatabaseConfig
import org.bson.UuidRepresentation
import org.bson.codecs.UuidCodec
import org.bson.codecs.configuration.CodecRegistries
import org.litote.kmongo.KMongo

fun MongoDatabaseConfig.createClient(): MongoClient {
    val credentials = user?.let {
        listOfNotNull(it, password?.decryptedValue).joinToString(":")
    }
    val connectionString = "$protocol://${listOfNotNull(credentials, host).joinToString("@")}"
    val clientOptions = MongoClientOptions.builder()
        .codecRegistry(
            CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(UuidCodec(UuidRepresentation.STANDARD)),
                MongoClient.getDefaultCodecRegistry()
            )
        )
    return KMongo.createClient(MongoClientURI(connectionString, clientOptions))
}
