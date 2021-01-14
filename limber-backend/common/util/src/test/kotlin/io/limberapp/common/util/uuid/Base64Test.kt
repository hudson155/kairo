package io.limberapp.common.util.uuid

import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Base64Test {
  @Test
  fun encode() {
    val encoded = UUID.fromString("8ef3c217-22fb-45b7-b68d-e05b4274bea6").base64Encode()
    assertEquals("jvPCFyL7Rbe2jeBbQnS+pg==", encoded)
  }

  @Test
  fun decode() {
    val uuid = uuidFromBase64Encoded("jvPCFyL7Rbe2jeBbQnS+pg==")
    assertEquals(UUID.fromString("8ef3c217-22fb-45b7-b68d-e05b4274bea6"), uuid)
  }
}
