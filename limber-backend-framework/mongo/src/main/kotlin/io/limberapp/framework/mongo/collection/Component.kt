package io.limberapp.framework.mongo.collection

import io.limberapp.framework.store.LimberMongoObjectMapper
import org.bson.Document
import org.bson.conversions.Bson

data class Component(
    private val contents: MutableMap<String, Any> = mutableMapOf()
) : MutableMap<String, Any> by contents {

    private val objectMapper = LimberMongoObjectMapper()

    fun asBson(): Bson = Document.parse(objectMapper.writeValueAsString(contents))
}
