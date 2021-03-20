package io.limberapp.permissions.limber

import io.limberapp.permissions.feature.TestFeaturePermissions
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class LimberPermissionsTest {
  @Test
  fun all() {
    with(LimberPermissions.all()) {
      assertEquals("2.C", asDarb())
      assertEquals("11", asBitString())
      assertTrue(LimberPermission.IDENTITY_PROVIDER in this)
      assertTrue(LimberPermission.SUPERUSER in this)
    }
  }

  @Test
  fun none() {
    with(LimberPermissions.none()) {
      assertEquals("2.0", asDarb())
      assertEquals("00", asBitString())
      assertFalse(LimberPermission.IDENTITY_PROVIDER in this)
      assertFalse(LimberPermission.SUPERUSER in this)
    }
  }

  @Test
  fun fromDarb() {
    with(LimberPermissions.fromDarb("2.C")) {
      assertEquals("2.C", asDarb())
      assertEquals("11", asBitString())
      assertTrue(LimberPermission.IDENTITY_PROVIDER in this)
      assertTrue(LimberPermission.SUPERUSER in this)
    }
    with(LimberPermissions.fromDarb("1.C")) {
      assertEquals("2.8", asDarb())
      assertEquals("10", asBitString())
      assertTrue(LimberPermission.IDENTITY_PROVIDER in this)
      assertFalse(LimberPermission.SUPERUSER in this)
    }
    assertFails { TestFeaturePermissions.fromDarb("2.G") }
  }

  @Test
  fun fromBitString() {
    with(LimberPermissions.fromBitString("10")) {
      assertEquals("2.8", asDarb())
      assertEquals("10", asBitString())
      assertTrue(LimberPermission.IDENTITY_PROVIDER in this)
      assertFalse(LimberPermission.SUPERUSER in this)
    }
    with(LimberPermissions.fromBitString("1")) {
      assertEquals("2.8", asDarb())
      assertEquals("10", asBitString())
      assertTrue(LimberPermission.IDENTITY_PROVIDER in this)
      assertFalse(LimberPermission.SUPERUSER in this)
    }
  }
}
