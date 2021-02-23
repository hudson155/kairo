package io.limberapp.testing.integration

import io.limberapp.server.Server
import io.limberapp.util.uuid.DeterministicUuidGenerator
import io.limberapp.util.uuid.UuidGenerator
import java.time.Clock

internal val Server<*>.uuidGenerator: DeterministicUuidGenerator
  get() = injector.getInstance(UuidGenerator::class.java) as DeterministicUuidGenerator

internal val Server<*>.clock: Clock
  get() = injector.getInstance(Clock::class.java)
