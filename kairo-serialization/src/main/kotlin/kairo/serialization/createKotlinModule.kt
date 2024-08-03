package kairo.serialization

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.kotlinModule

internal fun createKotlinModule(): Module =
  kotlinModule {
    configure(KotlinFeature.SingletonSupport, true)
    configure(KotlinFeature.StrictNullChecks, true)
  }
