package io.limberapp.permissions.org

import io.limberapp.permissions.feature.TestFeaturePermissions
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class OrgPermissionsTest {
  @Test
  fun all() {
    with(OrgPermissions.all()) {
      assertEquals("4.F", asDarb())
      assertEquals("1111", asBitString())
      assertTrue(OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS in this)
      assertTrue(OrgPermission.MANAGE_ORG_METADATA in this)
    }
  }

  @Test
  fun none() {
    with(OrgPermissions.none()) {
      assertEquals("4.0", asDarb())
      assertEquals("0000", asBitString())
      assertFalse(OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS in this)
      assertFalse(OrgPermission.MANAGE_ORG_METADATA in this)
    }
  }

  @Test
  fun fromDarb() {
    with(OrgPermissions.fromDarb("4.6")) {
      assertEquals("4.6", asDarb())
      assertEquals("0110", asBitString())
      assertTrue(OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS in this)
      assertTrue(OrgPermission.MANAGE_ORG_METADATA in this)
    }
    with(OrgPermissions.fromDarb("2.6")) {
      assertEquals("4.4", asDarb())
      assertEquals("0100", asBitString())
      assertTrue(OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS in this)
      assertFalse(OrgPermission.MANAGE_ORG_METADATA in this)
    }
    assertFails { TestFeaturePermissions.fromDarb("4.G") }
  }

  @Test
  fun fromBitString() {
    with(OrgPermissions.fromBitString("0100")) {
      assertEquals("4.4", asDarb())
      assertEquals("0100", asBitString())
      assertTrue(OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS in this)
      assertFalse(OrgPermission.MANAGE_ORG_METADATA in this)
    }
    with(OrgPermissions.fromBitString("01")) {
      assertEquals("4.4", asDarb())
      assertEquals("0100", asBitString())
      assertTrue(OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS in this)
      assertFalse(OrgPermission.MANAGE_ORG_METADATA in this)
    }
  }
}
