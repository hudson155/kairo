package io.limberapp.common.util.uuid

import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class UuidTest {
  @Test
  fun uuidFromString() {
    assertEquals(UUID.fromString("df56d76b-82e8-4d6c-897d-7b5400ef5cbb"),
        uuidFromString("df56d76b-82e8-4d6c-897d-7b5400ef5cbb"))
    assertEquals(UUID.fromString("df56d76b-82e8-4d6c-897d-7b5400ef5cbb"),
        uuidFromString("df56d76b82e84d6c897d7b5400ef5cbb"))
    assertEquals(UUID.fromString("df56d76b-82e8-4d6c-897d-7b5400ef5cbb"),
        uuidFromString("df56----d76b82e84d6c897d7b5400ef5cbb"))
    assertEquals(UUID.fromString("df56d76b-82e8-4d6c-897d-7b5400ef5cbb"),
        uuidFromString("DF56D76B-82E8-4D6C-897D-7B5400EF5CBB"))
    assertEquals(UUID.fromString("df56d76b-82e8-4d6c-897d-7b5400ef5cbb"),
        uuidFromString("df56-d76b-82e8-4d6c-897d-7b54-00ef-5cbb"))
    assertFails { uuidFromString("Gf56-d76b-82e8-4d6c-897d-7b54-00ef-5cbb") }
  }
}
