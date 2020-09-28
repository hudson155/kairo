package io.limberapp.testing.integration

import io.limberapp.common.LimberApplication
import io.limberapp.common.util.uuid.DeterministicUuidGenerator
import io.limberapp.common.util.uuid.UuidGenerator
import java.time.Clock

internal val LimberApplication<*>.injector get() = checkNotNull(context).first

internal val LimberApplication<*>.uuidGenerator: DeterministicUuidGenerator
  get() = injector.getInstance(UuidGenerator::class.java) as DeterministicUuidGenerator

internal val LimberApplication<*>.clock: Clock
  get() = injector.getInstance(Clock::class.java)
