package io.limberapp.testing.integration

import io.limberapp.module.Module

abstract class AbstractTypeConversionModule : Module() {
  final override fun bind(): Unit = Unit

  final override fun cleanUp(): Unit = Unit
}
