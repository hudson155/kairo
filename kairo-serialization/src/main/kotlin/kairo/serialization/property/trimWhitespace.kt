package kairo.serialization.property

import kairo.serialization.ObjectMapperFactory
import kairo.serialization.module.primitives.StringDeserializer
import kairo.serialization.module.primitives.TrimWhitespace

/**
 * See [TrimWhitespace] and [StringDeserializer].
 */
public var ObjectMapperFactory<*, *>.trimWhitespace: TrimWhitespace.Type
  get() = properties["trimWhitespace"] as TrimWhitespace.Type? ?: TrimWhitespace.Type.TrimNone
  set(value) {
    properties["trimWhitespace"] = value
  }
