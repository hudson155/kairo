package io.limberapp.common.module

import com.google.inject.AbstractModule
import io.limberapp.common.typeConversion.TypeConverter

abstract class Module : AbstractModule() {
  open val typeConverters: Set<TypeConverter<*>> = emptySet()

  override fun configure() {
    bind()
  }

  protected abstract fun bind()

  abstract fun cleanUp()
}
