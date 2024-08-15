package kairo.serialization

import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.kotlinModule

internal fun ObjectMapperFactoryBuilder.configureKotlin() {
  addModule(
    kotlinModule {
      configure(KotlinFeature.SingletonSupport, true)
      configure(KotlinFeature.StrictNullChecks, true)
    },
  )
}
