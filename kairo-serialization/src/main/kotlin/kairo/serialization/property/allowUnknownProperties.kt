package kairo.serialization.property

import kairo.serialization.ObjectMapperFactory

/**
 * Unknown properties are prohibited by default by Jackson, and we respect that default here.
 * This is an appropriate choice for internal use.
 * However, it's not an appropriate choice for object mappers that communicate with 3rd-party APIs.
 */
@Suppress("NullableBooleanCheck")
public var ObjectMapperFactory<*, *>.allowUnknownProperties: Boolean
  get() = properties["allowUnknownProperties"] as Boolean? ?: false
  set(value) {
    properties["allowUnknownProperties"] = value
  }
