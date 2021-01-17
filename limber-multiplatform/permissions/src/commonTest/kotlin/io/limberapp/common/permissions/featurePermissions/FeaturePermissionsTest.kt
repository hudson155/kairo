package io.limberapp.common.permissions.featurePermissions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class FeaturePermissionsTest {
  @Test
  fun none() {
    with(TestFeaturePermissions.none()) {
      assertEquals("T.2.0", asDarb())
      assertEquals("T00", asBitString())
      assertFalse(TestFeaturePermission.TEST_FEATURE_PERMISSION_1 in this)
      assertFalse(TestFeaturePermission.TEST_FEATURE_PERMISSION_2 in this)
    }
  }

  @Test
  fun all() {
    with(TestFeaturePermissions.all()) {
      assertEquals("T.2.C", asDarb())
      assertEquals("T11", asBitString())
      assertTrue(TestFeaturePermission.TEST_FEATURE_PERMISSION_1 in this)
      assertTrue(TestFeaturePermission.TEST_FEATURE_PERMISSION_2 in this)
    }
  }

  @Test
  fun fromDarb() {
    with(TestFeaturePermissions.fromDarb("2.C")) {
      assertEquals("T.2.C", asDarb())
      assertEquals("T11", asBitString())
      assertTrue(TestFeaturePermission.TEST_FEATURE_PERMISSION_1 in this)
      assertTrue(TestFeaturePermission.TEST_FEATURE_PERMISSION_2 in this)
    }
    with(TestFeaturePermissions.fromDarb("1.C")) {
      assertEquals("T.2.8", asDarb())
      assertEquals("T10", asBitString())
      assertTrue(TestFeaturePermission.TEST_FEATURE_PERMISSION_1 in this)
      assertFalse(TestFeaturePermission.TEST_FEATURE_PERMISSION_2 in this)
    }
    assertFails { TestFeaturePermissions.fromDarb("T.2.C") }
    assertFails { TestFeaturePermissions.fromDarb("2.G") }
  }

  @Test
  fun fromBitString() {
    with(TestFeaturePermissions.fromBitString("10")) {
      assertEquals("T.2.8", asDarb())
      assertEquals("T10", asBitString())
      assertTrue(TestFeaturePermission.TEST_FEATURE_PERMISSION_1 in this)
      assertFalse(TestFeaturePermission.TEST_FEATURE_PERMISSION_2 in this)
    }
    with(TestFeaturePermissions.fromBitString("1")) {
      assertEquals("T.2.8", asDarb())
      assertEquals("T10", asBitString())
      assertTrue(TestFeaturePermission.TEST_FEATURE_PERMISSION_1 in this)
      assertFalse(TestFeaturePermission.TEST_FEATURE_PERMISSION_2 in this)
    }
    assertFails { TestFeaturePermissions.fromDarb("10") }
  }
}
