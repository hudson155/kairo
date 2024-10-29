package kairo.serialization.property

import kairo.serialization.ObjectMapperFactory

/**
 * Pretty printing usually isn't desirable since it creates longer output,
 * but it can sometimes be nice.
 */
@Suppress("NullableBooleanCheck")
public var ObjectMapperFactory<*, *>.prettyPrint: Boolean
  get() = properties["prettyPrint"] as Boolean? ?: false
  set(value) {
    properties["prettyPrint"] = value
  }
