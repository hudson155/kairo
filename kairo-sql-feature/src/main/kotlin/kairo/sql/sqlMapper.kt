package kairo.sql

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.json.JsonMapper
import kairo.serialization.jsonMapper
import kairo.serialization.property.allowUnknownProperties

public val sqlReadMapper: JsonMapper =
  jsonMapper {
    allowUnknownProperties = true
  }.build {
    propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
  }

public val sqlWriteMapper: JsonMapper =
  jsonMapper {
    allowUnknownProperties = true
  }.build()
