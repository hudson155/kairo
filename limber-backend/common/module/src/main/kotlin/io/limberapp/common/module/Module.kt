package io.limberapp.common.module

import com.google.inject.AbstractModule

abstract class Module : AbstractModule() {
  override fun configure() {
    bind()
  }

  protected abstract fun bind()

  abstract fun cleanUp()
}
