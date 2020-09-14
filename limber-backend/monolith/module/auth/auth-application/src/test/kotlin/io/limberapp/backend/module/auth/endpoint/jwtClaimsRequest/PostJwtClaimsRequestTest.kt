package io.limberapp.backend.module.auth.endpoint.jwtClaimsRequest

import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermissions
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions.Companion.union
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.module.auth.api.jwtClaimsRequest.JwtClaimsRequestApi
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.rep.jwtClaimsRequest.JwtClaimsRequestRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleMembershipRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.model.account.AccountModel
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.common.testing.responseContent
import io.mockk.every
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

internal class PostJwtClaimsRequestTest : ResourceTest() {
  @Test
  fun happyPathUserDoesNotExist() {
    val userGuid = deterministicUuidGenerator[3]
    val emailAddress = "jhudson@jhudson.ca"
    val existingOrg = OrgModel(
      guid = UUID.randomUUID(),
      createdDate = LocalDateTime.now(fixedClock),
      name = "Cranky Pasta",
      ownerUserGuid = UUID.randomUUID()
    )
    every {
      mockedServices[UserService::class].findOnlyOrNull(any())
    } returns null
    every {
      mockedServices[UserService::class].create(any())
    } answers { firstArg() }
    every {
      mockedServices[OrgService::class].findOnlyOrThrow(any())
    } returns existingOrg
    every {
      mockedServices[FeatureService::class].findAsSet(any())
    } returns emptySet()

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, existingOrg.guid)
    limberTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(existingOrg.guid)))

    val membershipOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, 0)
    limberTest.setup(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.memberFixture.creation()))

    limberTest.setup(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.maintainerFixture.creation()))

    limberTest.setup(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.adminFixture.creation()))

    val orgPermissions = setOf(
      membershipOrgRoleRep.permissions, // This org role has isDefault = true.
    ).union()

    val jwtRequest = JwtClaimsRequestRep.Creation(
      auth0ClientId = tenantRep.auth0ClientId,
      firstName = "Jeff",
      lastName = "Hudson",
      emailAddress = emailAddress,
      profilePhotoUrl = null
    )
    limberTest.test(JwtClaimsRequestApi.Post(jwtRequest)) {
      val actual = responseContent
      val expected = "{\n" +
        "    \"org\": \"{" +
        "\\\"guid\\\":\\\"${existingOrg.guid}\\\"," +
        "\\\"name\\\":\\\"${existingOrg.name}\\\"," +
        "\\\"isOwner\\\":false," +
        "\\\"permissions\\\":\\\"${orgPermissions.asDarb()}\\\"," +
        "\\\"features\\\":{}" +
        "}\",\n" +
        "    \"roles\": \"[]\",\n" +
        "    \"user\": \"{" +
        "\\\"guid\\\":\\\"$userGuid\\\"," +
        "\\\"firstName\\\":\\\"${jwtRequest.firstName}\\\"," +
        "\\\"lastName\\\":\\\"${jwtRequest.lastName}\\\"}\"\n" +
        "}"
      assertEquals(expected, actual)
    }
  }

  @Test
  fun happyPathUserExists() {
    val existingOrg = OrgModel(
      guid = UUID.randomUUID(),
      createdDate = LocalDateTime.now(fixedClock),
      name = "Cranky Pasta",
      ownerUserGuid = UUID.randomUUID()
    )
    val existingFeature = FeatureModel(
      guid = UUID.randomUUID(),
      createdDate = LocalDateTime.now(fixedClock),
      orgGuid = existingOrg.guid,
      rank = 0,
      name = "Forms",
      path = "/forms",
      type = FeatureModel.Type.FORMS,
      isDefaultFeature = true
    )
    val existingAccount = AccountModel(
      guid = UUID.randomUUID(),
      createdDate = LocalDateTime.now(fixedClock),
      identityProvider = false,
      superuser = true,
    )
    val existingUser = UserModel(
      guid = existingAccount.guid,
      createdDate = existingAccount.createdDate,
      identityProvider = existingAccount.identityProvider,
      superuser = existingAccount.superuser,
      orgGuid = existingOrg.guid,
      firstName = "Summer",
      lastName = "Kavan",
      emailAddress = "jhudson@jhudson.ca",
      profilePhotoUrl = null
    )
    every {
      mockedServices[UserService::class].findOnlyOrNull(any())
    } returns existingUser
    every {
      mockedServices[OrgService::class].findOnlyOrThrow(any())
    } returns existingOrg
    every {
      mockedServices[FeatureService::class].findAsSet(any())
    } returns setOf(existingFeature)

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, existingOrg.guid)
    limberTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(existingOrg.guid)))

    val membershipOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, 0)
    limberTest.setup(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.memberFixture.creation()))

    val maintainerOrgRoleRep = OrgRoleRepFixtures.maintainerFixture.complete(this, 1)
    limberTest.setup(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.maintainerFixture.creation()))

    limberTest.setup(OrgRoleApi.Post(existingOrg.guid, OrgRoleRepFixtures.adminFixture.creation()))

    limberTest.setup(
      endpoint = OrgRoleMembershipApi.Post(
        orgGuid = existingOrg.guid,
        orgRoleGuid = maintainerOrgRoleRep.guid,
        rep = OrgRoleMembershipRepFixtures.fixture.creation(existingUser.guid)
      )
    )

    val orgPermissions = setOf(
      membershipOrgRoleRep.permissions, // This org role has isDefault = true.
      maintainerOrgRoleRep.permissions,
    ).union()

    val featurePermissions = FormsFeaturePermissions.none()

    val jwtRequest = JwtClaimsRequestRep.Creation(
      auth0ClientId = tenantRep.auth0ClientId,
      firstName = "Jeff",
      lastName = "Hudson",
      emailAddress = "jhudson@jhudson.ca",
      profilePhotoUrl = null
    )
    limberTest.test(JwtClaimsRequestApi.Post(jwtRequest)) {
      val actual = responseContent
      val expected = "{\n" +
        "    \"org\": \"{" +
        "\\\"guid\\\":\\\"${existingOrg.guid}\\\"," +
        "\\\"name\\\":\\\"${existingOrg.name}\\\"," +
        "\\\"isOwner\\\":false," +
        "\\\"permissions\\\":\\\"${orgPermissions.asDarb()}\\\"," +
        "\\\"features\\\":{" +
        "\\\"${existingFeature.guid}\\\":{" +
        "\\\"permissions\\\":\\\"${featurePermissions.asDarb()}\\\"" +
        "}" +
        "}" +
        "}\",\n" +
        "    \"roles\": \"[\\\"${JwtRole.SUPERUSER}\\\"]\",\n" +
        "    \"user\": \"{" +
        "\\\"guid\\\":\\\"${existingUser.guid}\\\"," +
        "\\\"firstName\\\":\\\"${existingUser.firstName}\\\"," +
        "\\\"lastName\\\":\\\"${existingUser.lastName}\\\"}\"\n" +
        "}"
      assertEquals(expected, actual)
    }
  }
}
