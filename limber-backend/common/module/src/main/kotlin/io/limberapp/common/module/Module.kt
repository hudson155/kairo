package io.limberapp.common.module

import com.google.inject.AbstractModule

abstract class Module : AbstractModule() {
  override fun configure() {
    bind()
  }

  abstract fun cleanUp()

  protected abstract fun bind()
}
