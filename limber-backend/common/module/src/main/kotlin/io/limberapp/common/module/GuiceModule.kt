package io.limberapp.common.module

import com.google.inject.AbstractModule

abstract class GuiceModule : AbstractModule() {
  abstract override fun configure()
  abstract fun unconfigure()
}
