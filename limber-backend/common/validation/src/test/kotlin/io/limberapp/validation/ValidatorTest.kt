package io.limberapp.validation

import io.limberapp.util.uuid.base64Encode
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ValidatorTest {
  @Test
  fun auth0ClientId() {
    assertFalse(Validator.auth0ClientId(""))
    val id = "yDiVK18hoeddya8J"
    val valid = "org_$id"
    assertFalse(Validator.auth0ClientId(valid.dropLast(1)))
    assertTrue(Validator.auth0ClientId(valid))
    assertFalse(Validator.auth0ClientId(valid + "3"))
    assertFalse(Validator.auth0ClientId(valid.dropLast(1) + "."))
    assertFalse(Validator.auth0ClientId("abc_$id"))
  }

  @Test
  fun base64EncodedUuid() {
    assertFalse(Validator.base64EncodedUuid(""))
    repeat(10) { assertTrue(Validator.base64EncodedUuid(UUID.randomUUID().base64Encode())) }
    assertTrue(Validator.base64EncodedUuid("zEXf5JXLSQCcY9qHOKGjYg=="))
    assertTrue(Validator.base64EncodedUuid("hEXf5JXLSQCcY9qHOKGjYg=="))
    assertFalse(Validator.base64EncodedUuid("zEXf5JXLSQCcY9qHOKGjYh=="))
  }

  @Test
  fun darb() {
    assertFalse(Validator.darb(""))
    assertFalse(Validator.darb("0"))
    assertTrue(Validator.darb("0."))
    assertFalse(Validator.darb("1."))
    assertTrue(Validator.darb("1.0"))
    assertTrue(Validator.darb("4.0"))
    assertFalse(Validator.darb("1.00"))
    assertFalse(Validator.darb("4.00"))
    assertTrue(Validator.darb("5.00"))
    assertTrue(Validator.darb("8.FF"))
    assertFalse(Validator.darb("8.FG"))
  }

  @Test
  fun emailAddress() {
    assertFalse(Validator.emailAddress(""))
    assertTrue(Validator.emailAddress("jeff.hudson@limberapp.io"))
    assertTrue(Validator.emailAddress(".!#\$%&'*+/=?^_`{|}~-@limberapp.io"))
    assertFalse(Validator.emailAddress("@limberapp.io"))
    assertFalse(Validator.emailAddress("jeff@hudson@limberapp.io"))
    assertFalse(Validator.emailAddress("jeff(hudson@limberapp.io"))
    assertTrue(Validator.emailAddress("jeff.hudson@foo.bar.baz"))
    assertFalse(Validator.emailAddress("jeff.hudson@foo.bar.baz/homepage"))
  }

  @Test
  fun featureName() {
    assertFalse(Validator.featureName(""))
    assertFalse(Validator.featureName("A".repeat(2)))
    assertTrue(Validator.featureName("A".repeat(3)))
    assertTrue(Validator.featureName("A".repeat(20)))
    assertFalse(Validator.featureName("A".repeat(21)))
  }

  @Test
  fun hostname() {
    assertFalse(Validator.hostname(""))
    assertFalse(Validator.hostname("limberapp"))
    assertTrue(Validator.hostname("limberapp.io"))
    assertTrue(Validator.hostname("foo.bar.baz"))
    assertFalse(Validator.hostname("foo.bar.baz/homepage"))
    assertFalse(Validator.hostname("http://foo.bar.baz"))
  }

  @Test
  fun humanName() {
    assertFalse(Validator.humanName(""))
    assertFalse(Validator.humanName("A".repeat(0)))
    assertTrue(Validator.humanName("A".repeat(1)))
    assertTrue(Validator.humanName("A".repeat(60)))
    assertFalse(Validator.humanName("A".repeat(61)))
  }

  @Test
  fun length1hundred() {
    assertFalse(Validator.length1hundred("", allowEmpty = false))
    assertTrue(Validator.length1hundred("", allowEmpty = true))
    listOf(false, true).forEach { allowEmpty ->
      assertTrue(Validator.length1hundred("A".repeat(1), allowEmpty))
      assertTrue(Validator.length1hundred("A".repeat(100), allowEmpty))
      assertFalse(Validator.length1hundred("A".repeat(101), allowEmpty))
    }
  }

  @Test
  fun length10thousand() {
    assertFalse(Validator.length10thousand("", allowEmpty = false))
    assertTrue(Validator.length10thousand("", allowEmpty = true))
    listOf(false, true).forEach { allowEmpty ->
      assertTrue(Validator.length10thousand("A".repeat(1), allowEmpty))
      assertTrue(Validator.length10thousand("A".repeat(10_000), allowEmpty))
      assertFalse(Validator.length10thousand("A".repeat(10_001), allowEmpty))
    }
  }

  @Test
  fun orgName() {
    assertFalse(Validator.orgName(""))
    assertFalse(Validator.orgName("A".repeat(2)))
    assertTrue(Validator.orgName("A".repeat(3)))
    assertTrue(Validator.orgName("A".repeat(40)))
    assertFalse(Validator.orgName("A".repeat(41)))
  }

  @Test
  fun orgRoleName() {
    assertFalse(Validator.orgRoleName(""))
    assertFalse(Validator.orgRoleName("A".repeat(2)))
    assertTrue(Validator.orgRoleName("A".repeat(3)))
    assertTrue(Validator.orgRoleName("A".repeat(40)))
    assertFalse(Validator.orgRoleName("A".repeat(41)))
  }

  @Test
  fun path() {
    assertTrue(Validator.path(""))
    assertTrue(Validator.path("/"))
    assertTrue(Validator.path("/foo"))
    assertTrue(Validator.path("/foo/"))
    assertTrue(Validator.path("/foo/bar"))
    assertTrue(Validator.path("/foo/bar/"))
    assertTrue(Validator.path("/foo/bar/-_.%"))
    assertFalse(Validator.path("/foo/bar/$"))
  }

  @Test
  fun url() {
    assertFalse(Validator.url(""))
    assertFalse(Validator.url("limberapp.io"))
    assertFalse(Validator.url("http://limberapp"))
    assertTrue(Validator.url("http://limberapp.io"))
    assertTrue(Validator.url("http://limberapp.io/"))
    assertTrue(Validator.url("http://limberapp.io/foo/bar"))
    assertTrue(Validator.url("http://limberapp.io/foo/bar/"))
    assertTrue(Validator.url("http://limberapp.io/foo/bar/-_.%"))
    assertFalse(Validator.url("http://limberapp.io/foo/bar/$"))
    assertTrue(Validator.url("http://limberapp.io/foo?baz=qux"))
    assertFalse(Validator.url("http://limberapp.io/foo?baz=q~x"))
    assertTrue(Validator.url("http://limberapp.io/foo#asdf"))
    assertTrue(Validator.url("http://limberapp.io/foo?baz=qux#asdf"))
    assertTrue(Validator.url("http://limberapp.io/foo#asdf?baz=qux"))
    assertTrue(Validator.url("http://limberapp.io/foo=?##?bar"))
  }

  @Test
  fun uuid() {
    assertFalse(Validator.uuid(""))
    repeat(10) { assertTrue(Validator.uuid(UUID.randomUUID().toString())) }
    assertTrue(Validator.uuid("cc45dfe4-95cb-4900-9c63-da8738a1a362"))
    assertTrue(Validator.uuid("cc45dfe495cb49009c63da8738a1a362"))
    assertTrue(Validator.uuid("CC45DFE4-95CB-4900-9C63-da8738a1a362"))
    assertFalse(Validator.uuid("Gc45dfe4-95cb-4900-9c63-da8738a1a362"))
  }
}
